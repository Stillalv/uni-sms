package com.unisms.app.ui.screens.activation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.unisms.app.ui.components.CancellationLockBar
import com.unisms.app.ui.components.HeroOtpCard
import com.unisms.app.ui.components.RadarPulseAnimation
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
import com.unisms.app.ui.theme.MonospacePhoneStyle
import com.unisms.app.ui.util.CountryCatalog
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveOtpScreen(
    activationId: Long,
    viewModel: ActiveOtpViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(activationId) {
        viewModel.loadActivation(activationId)
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aktivasi Nomor Virtual", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppleSystemBg
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppleBlue)
            }
        } else {
            val record = uiState.record
            if (record == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Data aktivasi tidak ditemukan.", color = AppleTextSecondary)
                }
            } else {
                val iso = CountryCatalog.getCountryIso(record.countryId)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Service & Country Info Row (Apple Inset Card)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
                        border = BorderStroke(0.5.dp, AppleHairline)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
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
                                            .size(width = 30.dp, height = 20.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        error = { Text(text = record.flagEmoji, fontSize = 20.sp) }
                                    )
                                } else {
                                    Text(text = record.flagEmoji, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = record.countryName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleTextPrimary
                                    )
                                    Text(
                                        text = record.serviceName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AppleTextSecondary
                                    )
                                }
                            }
                            StatusBadge(status = if (uiState.otpCode != null) "COMPLETED" else if (uiState.isCancelled) "CANCELLED" else "ACTIVE")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Number Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
                        border = BorderStroke(0.5.dp, AppleHairline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "NOMOR TELEPON VIRTUAL",
                                fontSize = 10.sp,
                                color = AppleTextSecondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "+${record.phoneNumber}",
                                style = MonospacePhoneStyle,
                                color = AppleTextPrimary,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(record.phoneNumber))
                                    Toast.makeText(context, "Nomor disalin ke clipboard", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = AppleBlue),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(
                                    painter = LucideIcons.Copy,
                                    contentDescription = "Salin Nomor",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Salin Nomor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Session Countdown Timer
                    val minutes = uiState.secondsUntilExpire / 60
                    val seconds = uiState.secondsUntilExpire % 60
                    Text(
                        text = String.format(Locale.US, "Sisa Waktu Sesi: %02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (uiState.secondsUntilExpire < 180) AppleRed else AppleTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dynamic Section: Waiting Radar OR Received OTP Hero Card
                    if (uiState.otpCode != null) {
                        HeroOtpCard(
                            otpCode = uiState.otpCode!!,
                            onCopyOtp = {
                                clipboardManager.setText(AnnotatedString(uiState.otpCode!!))
                                Toast.makeText(context, "Kode OTP disalin ke clipboard", Toast.LENGTH_SHORT).show()
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Actions for received OTP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.completeOrder() },
                                modifier = Modifier.weight(1f).height(38.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = AppleGreen)
                            ) {
                                Icon(painter = LucideIcons.Check, contentDescription = "Selesai", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Selesai", fontWeight = FontWeight.Bold)
                            }

                            if (record.canGetAnotherSms) {
                                OutlinedButton(
                                    onClick = { viewModel.requestSecondSms() },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    shape = RoundedCornerShape(50),
                                    border = BorderStroke(0.5.dp, AppleHairline)
                                ) {
                                    Icon(painter = LucideIcons.RefreshCw, contentDescription = "SMS 2", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("SMS Kedua", color = AppleTextPrimary)
                                }
                            }
                        }
                    } else if (!uiState.isCancelled) {
                        RadarPulseAnimation()

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = uiState.statusMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppleTextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 2-Minute Cancellation Lock Rule
                        CancellationLockBar(
                            secondsRemaining = uiState.secondsUntilCanCancel,
                            onCancelClick = { viewModel.cancelOrder() }
                        )
                    } else {
                        Text(
                            text = "Aktivasi telah dibatalkan / kadaluarsa.",
                            style = MaterialTheme.typography.titleMedium,
                            color = AppleRed,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
