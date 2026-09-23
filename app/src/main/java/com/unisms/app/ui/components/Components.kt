package com.unisms.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.unisms.app.data.model.CountryItem
import com.unisms.app.data.model.ProviderItem
import com.unisms.app.data.model.ServiceItem
import com.unisms.app.ui.theme.AmberWarning
import com.unisms.app.ui.theme.AppleBlue
import com.unisms.app.ui.theme.AppleGreen
import com.unisms.app.ui.theme.AppleHairline
import com.unisms.app.ui.theme.AppleRed
import com.unisms.app.ui.theme.AppleSecondaryBg
import com.unisms.app.ui.theme.AppleSeparator
import com.unisms.app.ui.theme.AppleTertiaryBg
import com.unisms.app.ui.theme.AppleTextMuted
import com.unisms.app.ui.theme.AppleTextPrimary
import com.unisms.app.ui.theme.AppleTextSecondary
import com.unisms.app.ui.theme.CardDark
import com.unisms.app.ui.theme.EmeraldAccent
import com.unisms.app.ui.theme.IndigoPrimary
import com.unisms.app.ui.theme.LucideIcons
import com.unisms.app.ui.theme.MonospaceOtpStyle
import com.unisms.app.ui.theme.RubyError
import com.unisms.app.ui.theme.TextMuted
import com.unisms.app.ui.theme.TextPrimary
import com.unisms.app.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun BalanceCard(
    balance: Double?,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
        border = BorderStroke(0.5.dp, AppleHairline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AppleBlue.copy(alpha = 0.15f))
                        .border(0.5.dp, AppleBlue.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = LucideIcons.CreditCard,
                        contentDescription = "Wallet",
                        tint = AppleBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Saldo Akun SMSBower",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppleTextSecondary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (balance != null) String.format(Locale.US, "$%.2f", balance) else "$0.00",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppleTextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(AppleGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Aktif",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AppleGreen
                            )
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onRefresh,
                    enabled = !isLoading,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(AppleTertiaryBg)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = AppleBlue
                        )
                    } else {
                        Icon(
                            painter = LucideIcons.RefreshCw,
                            contentDescription = "Refresh Saldo",
                            tint = AppleTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(AppleTertiaryBg)
                ) {
                    Icon(
                        painter = LucideIcons.Sliders,
                        contentDescription = "Pengaturan",
                        tint = AppleTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceFilterChip(
    service: ServiceItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        color = if (isSelected) AppleBlue else AppleSecondaryBg,
        border = BorderStroke(
            0.5.dp,
            if (isSelected) AppleBlue else AppleHairline
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(service.assetUri)
                    .crossfade(true)
                    .build(),
                contentDescription = service.name,
                modifier = Modifier.size(15.dp),
                contentScale = ContentScale.Fit,
                error = {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color.White.copy(alpha = 0.2f) else AppleTertiaryBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = service.name.take(1).uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else AppleBlue
                        )
                    }
                }
            )
            Text(
                text = service.name,
                color = if (isSelected) Color.White else AppleTextPrimary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun CountryItemRow(
    country: CountryItem,
    onBuyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onBuyClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
        border = BorderStroke(0.5.dp, AppleHairline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Official Vector SVG Flag with fallback to Emoji
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(country.assetUri ?: country.remoteUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = country.name,
                    modifier = Modifier
                        .size(width = 32.dp, height = 21.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(0.5.dp, AppleHairline, RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Text(text = country.flagEmoji, fontSize = 20.sp)
                    },
                    error = {
                        Text(text = country.flagEmoji, fontSize = 20.sp)
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = country.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppleTextPrimary
                        )
                        Text(
                            text = country.dialCode,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppleTextSecondary
                        )
                    }
                    Text(
                        text = "${country.count} stok tersedia",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (country.count > 0) AppleGreen else AppleRed,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (country.cost > 0) String.format(Locale.US, "$%.2f", country.cost) else "-",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppleTextPrimary
                )
                Button(
                    onClick = onBuyClick,
                    enabled = country.count > 0,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppleBlue,
                        disabledContainerColor = AppleTertiaryBg
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(text = "Beli", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RadarPulseAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RadarScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RadarAlpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .clip(CircleShape)
                .background(AppleBlue.copy(alpha = 0.25f))
        )
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(AppleBlue.copy(alpha = 0.4f))
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(AppleBlue)
        )
    }
}

@Composable
fun HeroOtpCard(
    otpCode: String,
    onCopyOtp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
        border = BorderStroke(0.5.dp, AppleHairline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(AppleGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "KODE VERIFIKASI DITERIMA",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppleGreen
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppleTertiaryBg)
                    .border(0.5.dp, AppleHairline, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "KODE OTP",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppleTextSecondary
                        )
                        Text(
                            text = otpCode,
                            style = MonospaceOtpStyle,
                            color = AppleGreen,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Button(
                        onClick = onCopyOtp,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = AppleGreen),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            painter = LucideIcons.Copy,
                            contentDescription = "Salin Kode",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Salin", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CancellationLockBar(
    secondsRemaining: Int,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (120 - secondsRemaining).coerceIn(0, 120) / 120f
    val isLocked = secondsRemaining > 0

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLocked) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Batal otomatis terkunci (Aturan 2 Menit):",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppleTextSecondary
                )
                Text(
                    text = "${secondsRemaining}s",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = AmberWarning
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(50)),
                color = AmberWarning,
                trackColor = AppleTertiaryBg
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50)
            ) {
                Text("Batal Terkunci (${secondsRemaining}s)", style = MaterialTheme.typography.labelMedium)
            }
        } else {
            Button(
                onClick = onCancelClick,
                colors = ButtonDefaults.buttonColors(containerColor = AppleRed),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Batalkan & Refund Saldo", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        "COMPLETED" -> Triple(AppleGreen.copy(alpha = 0.15f), AppleGreen, "Selesai")
        "CANCELLED" -> Triple(AppleRed.copy(alpha = 0.15f), AppleRed, "Dibatalkan")
        "TIMEOUT" -> Triple(AmberWarning.copy(alpha = 0.15f), AmberWarning, "Kadaluarsa")
        "ACTIVE" -> Triple(AppleBlue.copy(alpha = 0.15f), AppleBlue, "Menunggu SMS")
        else -> Triple(AppleTextMuted.copy(alpha = 0.15f), AppleTextMuted, status)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ServicePickerDialog(
    services: List<ServiceItem>,
    selectedService: ServiceItem,
    onSelectService: (ServiceItem) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredServices = remember(services, searchQuery) {
        if (searchQuery.isBlank()) services
        else {
            val q = searchQuery.trim().lowercase()
            services.filter { it.name.lowercase().contains(q) || it.code.lowercase().contains(q) }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppleSecondaryBg,
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Pilih Layanan SMS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppleTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari layanan (WhatsApp, KakaoTalk)...", color = AppleTextSecondary, style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = {
                        Icon(painter = LucideIcons.Search, contentDescription = "Search", tint = AppleBlue, modifier = Modifier.size(16.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(painter = LucideIcons.X, contentDescription = "Clear", tint = AppleTextSecondary, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppleBlue,
                        unfocusedBorderColor = AppleHairline,
                        focusedContainerColor = AppleTertiaryBg,
                        unfocusedContainerColor = AppleTertiaryBg
                    )
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (filteredServices.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada layanan yang cocok.", color = AppleTextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                } else {
                    items(filteredServices, key = { it.code }) { service ->
                        val isSelected = service.code == selectedService.code
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onSelectService(service)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) AppleBlue.copy(alpha = 0.15f) else AppleTertiaryBg,
                            border = BorderStroke(
                                0.5.dp,
                                if (isSelected) AppleBlue else AppleHairline
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(service.assetUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = service.name,
                                        modifier = Modifier.size(20.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                    Column {
                                        Text(
                                            text = service.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) AppleBlue else AppleTextPrimary
                                        )
                                        Text(
                                            text = "Kode: ${service.code} · ${service.category}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AppleTextSecondary
                                        )
                                    }
                                }
                                if (isSelected) {
                                    Icon(
                                        painter = LucideIcons.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = AppleBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", color = AppleBlue, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

@Composable
fun ConfirmBuyDialog(
    serviceName: String,
    countryName: String,
    flag: String,
    baseCost: Double,
    providers: List<ProviderItem>,
    isLoadingProviders: Boolean,
    selectedProvider: ProviderItem?,
    onProviderSelected: (ProviderItem?) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    countryIso: String? = null,
    serviceCode: String? = null
) {
    val effectiveCost = selectedProvider?.price ?: baseCost

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppleSecondaryBg,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Apple Modal Drag Handle Pill
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(AppleSeparator)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Konfirmasi Nomor Baru",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppleTextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Selected Target Pill
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AppleTertiaryBg),
                    border = BorderStroke(0.5.dp, AppleHairline)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (countryIso != null) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("file:///android_asset/countries/${countryIso.lowercase()}.svg")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = countryName,
                                modifier = Modifier
                                    .size(width = 30.dp, height = 20.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop,
                                error = { Text(flag, fontSize = 20.sp) }
                            )
                        } else {
                            Text(text = flag, fontSize = 22.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "$countryName — $serviceName",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppleTextPrimary
                            )
                            Text(
                                text = "Layanan Verifikasi SMS Virtual",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppleTextSecondary
                            )
                        }
                    }
                }

                // Operator Selection Title
                Text(
                    text = "Pilih Operator / Provider (Vertikal):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppleTextSecondary
                )

                if (isLoadingProviders) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = AppleBlue)
                        Text("Memuat daftar operator...", style = MaterialTheme.typography.bodySmall, color = AppleTextSecondary)
                    }
                } else {
                    // Vertical Operator List (Apple Inset Grouped List)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AppleTertiaryBg),
                        border = BorderStroke(0.5.dp, AppleHairline)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp)
                        ) {
                            // Option 1: Automatic
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onProviderSelected(null) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        RadioButton(
                                            selected = selectedProvider == null,
                                            onClick = { onProviderSelected(null) },
                                            colors = RadioButtonDefaults.colors(selectedColor = AppleBlue, unselectedColor = AppleSeparator)
                                        )
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text("⚡ Otomatis (Termurah)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = AppleTextPrimary)
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(50))
                                                        .background(AppleBlue.copy(alpha = 0.2f))
                                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                                ) {
                                                    Text("Rekomendasi", fontSize = 9.sp, color = AppleBlue, fontWeight = FontWeight.SemiBold)
                                                }
                                            }
                                            Text("Sistem memilih operator stok terbanyak", style = MaterialTheme.typography.labelSmall, color = AppleTextSecondary)
                                        }
                                    }
                                    Text(
                                        text = String.format(Locale.US, "$%.3f", baseCost),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleGreen
                                    )
                                }
                            }

                            // Provider Items
                            items(providers) { provider ->
                                val isSel = selectedProvider?.id == provider.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onProviderSelected(provider) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        RadioButton(
                                            selected = isSel,
                                            onClick = { onProviderSelected(provider) },
                                            colors = RadioButtonDefaults.colors(selectedColor = AppleBlue, unselectedColor = AppleSeparator)
                                        )
                                        Column {
                                            Text(provider.name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = AppleTextPrimary)
                                            Text("Stok: ${provider.count} pcs", style = MaterialTheme.typography.labelSmall, color = AppleTextSecondary)
                                        }
                                    }
                                    Text(
                                        text = String.format(Locale.US, "$%.3f", provider.price),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleGreen
                                    )
                                }
                            }
                        }
                    }
                }

                // Summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppleTertiaryBg.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Total Biaya:", color = AppleTextSecondary, style = MaterialTheme.typography.bodySmall)
                        Text(text = "Garansi No Code No Pay (20m)", style = MaterialTheme.typography.labelSmall, color = AppleGreen)
                    }
                    Text(
                        text = String.format(Locale.US, "$%.3f", effectiveCost),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppleGreen
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = AppleBlue),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(painter = LucideIcons.Check, contentDescription = "Beli", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Beli Sekarang", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Batal", color = AppleTextSecondary)
            }
        }
    )
}
