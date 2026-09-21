package com.unisms.app.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unisms.app.ui.theme.EmeraldAccent
import com.unisms.app.ui.theme.IndigoPrimary
import com.unisms.app.ui.theme.RubyError
import com.unisms.app.ui.theme.TextMuted
import com.unisms.app.ui.theme.TextPrimary
import com.unisms.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isKeyCleared) {
        if (uiState.isKeyCleared) {
            onNavigateToOnboarding()
        }
    }

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan & Informasi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Section 1: API Key Management
            Text(
                text = "Kredensial API",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IndigoPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Key, contentDescription = "API Key", tint = IndigoPrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("API Key Tersimpan", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text(
                                text = uiState.maskedApiKey,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToOnboarding,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ganti Key")
                        }

                        Button(
                            onClick = { viewModel.clearApiKey() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RubyError)
                        ) {
                            Text("Hapus Key")
                        }
                    }
                }
            }

            // Section 2: Notifications & Haptic
            Text(
                text = "Preferensi Notifikasi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IndigoPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = EmeraldAccent)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Notifikasi OTP Masuk", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text("Tampilkan notifikasi heads-up saat SMS tiba", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                        Switch(
                            checked = uiState.isNotificationEnabled,
                            onCheckedChange = { viewModel.setNotificationEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldAccent, checkedTrackColor = EmeraldAccent.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Vibration, contentDescription = "Getaran", tint = EmeraldAccent)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Getaran Haptic Feedback", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text("Getar perangkat saat kode OTP diterima", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                        Switch(
                            checked = uiState.isHapticEnabled,
                            onCheckedChange = { viewModel.setHapticEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldAccent, checkedTrackColor = EmeraldAccent.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            // Section 3: Upstream Technical Info
            Text(
                text = "Informasi Gateway & Teknis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IndigoPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Upstream Gateway", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text("smsbower.page", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Webhook Whitelist IP", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text("167.235.198.205", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Early Cancel Lock", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text("120 Detik (2 Menit)", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Session Expiry", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text("20 Menit", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Keamanan Client", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text("Android Keystore AES-256", color = EmeraldAccent, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Section 4: Support & Documentation Links
            Text(
                text = "Dokumentasi & Dukungan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IndigoPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openUrl("https://smsbower.app/api?page=client") }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = "Docs", tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Dokumentasi Resmi Client API", color = TextPrimary)
                        }
                        Icon(Icons.Default.OpenInNew, contentDescription = "Open", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openUrl("https://documenter.getpostman.com/view/16514200/2sAYdkFTue") }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = "Postman", tint = IndigoPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Postman API Collection", color = TextPrimary)
                        }
                        Icon(Icons.Default.OpenInNew, contentDescription = "Open", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openUrl("https://t.me/smsbower_support_bot") }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SupportAgent, contentDescription = "Telegram", tint = EmeraldAccent)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Official Telegram Support Bot", color = TextPrimary)
                        }
                        Icon(Icons.Default.OpenInNew, contentDescription = "Open", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Uni-SMS v1.0.0 — Native Android (Kotlin + Jetpack Compose)",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
