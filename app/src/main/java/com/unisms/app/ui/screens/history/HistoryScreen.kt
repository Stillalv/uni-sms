package com.unisms.app.ui.screens.history

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.unisms.app.data.local.db.ActivationRecordEntity
import com.unisms.app.data.model.ActivationFilter
import com.unisms.app.ui.components.StatusBadge
import com.unisms.app.ui.theme.AppleBlue
import com.unisms.app.ui.theme.AppleGreen
import com.unisms.app.ui.theme.AppleHairline
import com.unisms.app.ui.theme.AppleRed
import com.unisms.app.ui.theme.AppleSecondaryBg
import com.unisms.app.ui.theme.AppleSystemBg
import com.unisms.app.ui.theme.AppleTertiaryBg
import com.unisms.app.ui.theme.AppleTextPrimary
import com.unisms.app.ui.theme.AppleTextSecondary
import com.unisms.app.ui.theme.LucideIcons
import com.unisms.app.ui.util.CountryCatalog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBackClick: () -> Unit,
    onNavigateToActiveOtp: (Long) -> Unit
) {
    val records by viewModel.filteredRecords.collectAsState()
    val currentFilter by viewModel.filter.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Aktivasi", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painter = LucideIcons.ArrowLeft, contentDescription = "Kembali", modifier = Modifier.size(20.dp), tint = AppleBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppleSystemBg,
                    titleContentColor = AppleTextPrimary,
                    navigationIconContentColor = AppleBlue
                )
            )
        },
        containerColor = AppleSystemBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Apple Filter Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                items(ActivationFilter.entries.toTypedArray()) { filter ->
                    val label = when (filter) {
                        ActivationFilter.ALL -> "Semua"
                        ActivationFilter.ACTIVE -> "Aktif"
                        ActivationFilter.COMPLETED -> "Selesai"
                        ActivationFilter.CANCELLED -> "Dibatalkan"
                    }
                    FilterChip(
                        selected = currentFilter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = { Text(label, fontSize = 12.sp) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppleBlue,
                            selectedLabelColor = AppleTextPrimary,
                            containerColor = AppleSecondaryBg,
                            labelColor = AppleTextSecondary
                        ),
                        border = BorderStroke(0.5.dp, if (currentFilter == filter) AppleBlue else AppleHairline)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (records.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada riwayat aktivasi.",
                        color = AppleTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(records, key = { it.id }) { record ->
                        HistoryRecordCard(
                            record = record,
                            onClick = { onNavigateToActiveOtp(record.id) },
                            onCopyPhone = {
                                clipboardManager.setText(AnnotatedString(record.phoneNumber))
                                Toast.makeText(context, "Nomor disalin ke clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onCopyOtp = {
                                record.otpCode?.let {
                                    clipboardManager.setText(AnnotatedString(it))
                                    Toast.makeText(context, "OTP disalin ke clipboard", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onDelete = { viewModel.deleteRecord(record) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryRecordCard(
    record: ActivationRecordEntity,
    onClick: () -> Unit,
    onCopyPhone: () -> Unit,
    onCopyOtp: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }
    val formattedDate = remember(record.createdAt) { dateFormat.format(Date(record.createdAt)) }
    val iso = CountryCatalog.getCountryIso(record.countryId)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
        border = BorderStroke(0.5.dp, AppleHairline)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (iso != null) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("file:///android_asset/countries/${iso.lowercase()}.svg")
                                .crossfade(true)
                                .build(),
                            contentDescription = record.countryName,
                            modifier = Modifier
                                .size(width = 28.dp, height = 18.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop,
                            error = { Text(text = record.flagEmoji, fontSize = 18.sp) }
                        )
                    } else {
                        Text(text = record.flagEmoji, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${record.countryName} — ${record.serviceName}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppleTextPrimary
                    )
                }
                StatusBadge(status = record.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "+${record.phoneNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppleTextPrimary
                    )
                    IconButton(onClick = onCopyPhone, modifier = Modifier.size(24.dp)) {
                        Icon(
                            painter = LucideIcons.Copy,
                            contentDescription = "Copy Phone",
                            tint = AppleBlue,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                Text(
                    text = String.format(Locale.US, "$%.2f", record.cost),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppleGreen
                )
            }

            if (!record.otpCode.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "OTP: ",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppleTextSecondary
                        )
                        Text(
                            text = record.otpCode.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppleGreen
                        )
                    }
                    IconButton(onClick = onCopyOtp, modifier = Modifier.size(24.dp)) {
                        Icon(
                            painter = LucideIcons.Copy,
                            contentDescription = "Copy OTP",
                            tint = AppleGreen,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppleTextSecondary,
                    fontSize = 11.sp
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = LucideIcons.Trash2,
                        contentDescription = "Hapus Record",
                        tint = AppleRed.copy(alpha = 0.8f),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
