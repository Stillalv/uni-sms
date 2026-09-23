package com.unisms.app.ui.screens.onboarding

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unisms.app.ui.theme.AppleBlue
import com.unisms.app.ui.theme.AppleHairline
import com.unisms.app.ui.theme.AppleRed
import com.unisms.app.ui.theme.AppleSecondaryBg
import com.unisms.app.ui.theme.AppleSystemBg
import com.unisms.app.ui.theme.AppleTertiaryBg
import com.unisms.app.ui.theme.AppleTextPrimary
import com.unisms.app.ui.theme.AppleTextSecondary
import com.unisms.app.ui.theme.LucideIcons

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onNavigateToDashboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToDashboard()
        }
    }

    Scaffold(
        containerColor = AppleSystemBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AppleBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = LucideIcons.KeyRound,
                    contentDescription = "API Key",
                    tint = AppleBlue,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selamat Datang di Uni-SMS",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppleTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Penerima kode OTP nomor virtual instan langsung dari SMSBower dengan perlindungan enkripsi.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppleTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AppleSecondaryBg),
                border = BorderStroke(0.5.dp, AppleHairline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SMSBower API Key",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppleTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = uiState.apiKey,
                        onValueChange = { viewModel.onApiKeyChanged(it) },
                        placeholder = { Text("Contoh: a1b2c3d4e5f6...", color = AppleTextSecondary, fontSize = 13.sp) },
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                val text = clipboardManager.getText()?.text
                                if (!text.isNullOrEmpty()) {
                                    viewModel.onApiKeyChanged(text.trim())
                                }
                            },
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(0.5.dp, AppleHairline),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                painter = LucideIcons.Clipboard,
                                contentDescription = "Paste",
                                modifier = Modifier.size(14.dp),
                                tint = AppleTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tempel", fontSize = 12.sp, color = AppleTextPrimary)
                        }

                        TextButton(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://smsbower.app/api?page=client")
                                )
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Dapatkan Key", fontSize = 12.sp, color = AppleBlue)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = LucideIcons.ExternalLink,
                                contentDescription = "Open Web",
                                modifier = Modifier.size(13.dp),
                                tint = AppleBlue
                            )
                        }
                    }

                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.errorMessage!!,
                            color = AppleRed,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { viewModel.validateAndSave() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = AppleBlue)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Verifikasi & Masuk",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
