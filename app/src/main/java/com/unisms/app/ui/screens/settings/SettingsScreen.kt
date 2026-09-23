package com.unisms.app.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unisms.app.ui.theme.AppleBlue
import com.unisms.app.ui.theme.AppleGreen
import com.unisms.app.ui.theme.AppleHairline
import com.unisms.app.ui.theme.AppleIndigo
import com.unisms.app.ui.theme.AppleOrange
import com.unisms.app.ui.theme.ApplePurple
import com.unisms.app.ui.theme.AppleRed
import com.unisms.app.ui.theme.AppleSecondaryBg
import com.unisms.app.ui.theme.AppleSeparator
import com.unisms.app.ui.theme.AppleSystemBg
import com.unisms.app.ui.theme.AppleTertiaryBg
import com.unisms.app.ui.theme.AppleTextMuted
import com.unisms.app.ui.theme.AppleTextPrimary
import com.unisms.app.ui.theme.AppleTextSecondary
import com.unisms.app.ui.theme.LucideIcons

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
                title = { Text("Pengaturan & Informasi", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Section 1: API Key Management (Apple Inset Grouped)
            Text(
                text = "KREDENSIAL AKUN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AppleTextSecondary,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
                border = BorderStroke(0.5.dp, AppleHairline)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppleSettingIconBox(icon = LucideIcons.KeyRound, color = AppleBlue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("API Key SMSBower", style = MaterialTheme.typography.labelSmall, color = AppleTextSecondary)
                                Text(
                                    text = uiState.maskedApiKey,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppleTextPrimary
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(AppleGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Tersambung", fontSize = 10.sp, color = AppleGreen, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToOnboarding,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(0.5.dp, AppleHairline)
                        ) {
                            Text("Ganti Key", fontSize = 12.sp, color = AppleTextPrimary)
                        }

                        Button(
                            onClick = { viewModel.clearApiKey() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = AppleRed)
                        ) {
                            Text("Hapus Key", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Section 2: Notifications & Haptic (Apple Inset Grouped with Clickable Rows)
            Text(
                text = "PEMBERITAHUAN & GETARAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AppleTextSecondary,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
                border = BorderStroke(0.5.dp, AppleHairline)
            ) {
                Column {
                    // Row 1: Heads-up Notification (Full-row clickable)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setNotificationEnabled(!uiState.isNotificationEnabled) }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f).padding(end = 12.dp)
                        ) {
                            AppleSettingIconBox(icon = LucideIcons.Bell, color = AppleOrange)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Notifikasi OTP Masuk", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AppleTextPrimary)
                                Text("Banner heads-up saat SMS tiba", style = MaterialTheme.typography.labelSmall, color = AppleTextSecondary)
                            }
                        }
                        Switch(
                            checked = uiState.isNotificationEnabled,
                            onCheckedChange = { viewModel.setNotificationEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AppleGreen,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = AppleTertiaryBg
                            )
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(start = 54.dp), color = AppleSeparator, thickness = 0.5.dp)

                    // Row 2: Haptic Vibration (Full-row clickable)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setHapticEnabled(!uiState.isHapticEnabled) }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f).padding(end = 12.dp)
                        ) {
                            AppleSettingIconBox(icon = LucideIcons.Vibrate, color = ApplePurple)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Getaran Haptic Taptic", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AppleTextPrimary)
                                Text("Getar halus saat kode verifikasi tiba", style = MaterialTheme.typography.labelSmall, color = AppleTextSecondary)
                            }
                        }
                        Switch(
                            checked = uiState.isHapticEnabled,
                            onCheckedChange = { viewModel.setHapticEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AppleGreen,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = AppleTertiaryBg
                            )
                        )
                    }
                }
            }

            // Section 3: Upstream Technical Info
            Text(
                text = "INFORMASI GATEWAY & TEKNIS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AppleTextSecondary,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
                border = BorderStroke(0.5.dp, AppleHairline)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppleInfoRow(label = "Upstream Gateway", value = "smsbower.page")
                    AppleInfoRow(label = "Webhook Whitelist IP", value = "167.235.198.205")
                    AppleInfoRow(label = "Early Cancel Lock", value = "120 Detik (2 Menit)")
                    AppleInfoRow(label = "Total Session Expiry", value = "20 Menit")
                    AppleInfoRow(label = "Keamanan Kredensial", value = "Android Keystore AES-256", highlight = true)
                }
            }

            // Section 4: Support & Documentation Links
            Text(
                text = "DOKUMENTASI & DUKUNGAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AppleTextSecondary,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
                border = BorderStroke(0.5.dp, AppleHairline)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openUrl("https://smsbower.app/api?page=client") }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppleSettingIconBox(icon = LucideIcons.BookOpen, color = AppleIndigo)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Dokumentasi Resmi Client API", color = AppleTextPrimary, fontSize = 13.sp)
                        }
                        Icon(painter = LucideIcons.ChevronRight, contentDescription = "Open", tint = AppleTextSecondary, modifier = Modifier.size(16.dp))
                    }

                    HorizontalDivider(modifier = Modifier.padding(start = 54.dp), color = AppleSeparator, thickness = 0.5.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openUrl("https://documenter.getpostman.com/view/16514200/2sAYdkFTue") }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppleSettingIconBox(icon = LucideIcons.Compass, color = AppleOrange)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Postman API Collection", color = AppleTextPrimary, fontSize = 13.sp)
                        }
                        Icon(painter = LucideIcons.ChevronRight, contentDescription = "Open", tint = AppleTextSecondary, modifier = Modifier.size(16.dp))
                    }

                    HorizontalDivider(modifier = Modifier.padding(start = 54.dp), color = AppleSeparator, thickness = 0.5.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openUrl("https://t.me/smsbower_support_bot") }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppleSettingIconBox(icon = LucideIcons.Send, color = AppleGreen)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Customer Support Telegram", color = AppleTextPrimary, fontSize = 13.sp)
                        }
                        Icon(painter = LucideIcons.ChevronRight, contentDescription = "Open", tint = AppleTextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Uni-SMS v1.3.0 — Apple Human Interface Look-Alike",
                style = MaterialTheme.typography.labelSmall,
                color = AppleTextMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AppleSettingIconBox(icon: Painter, color: Color) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Icon(painter = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun AppleInfoRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = AppleTextSecondary, fontSize = 12.sp)
        Text(
            text = value,
            color = if (highlight) AppleGreen else AppleTextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}
