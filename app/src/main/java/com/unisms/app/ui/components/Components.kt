package com.unisms.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unisms.app.data.model.CountryItem
import com.unisms.app.data.model.ServiceItem
import com.unisms.app.ui.theme.AmberWarning
import com.unisms.app.ui.theme.CardDark
import com.unisms.app.ui.theme.CyanInfo
import com.unisms.app.ui.theme.EmeraldAccent
import com.unisms.app.ui.theme.IndigoPrimary
import com.unisms.app.ui.theme.MonospaceOtpStyle
import com.unisms.app.ui.theme.MonospacePhoneStyle
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(IndigoPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = IndigoPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Saldo SMSBower",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = if (balance != null) String.format(Locale.US, "$%.2f", balance) else "$0.00",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldAccent
                    )
                }
            }

            Row {
                IconButton(onClick = onRefresh, enabled = !isLoading) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = IndigoPrimary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Saldo",
                            tint = TextSecondary
                        )
                    }
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Pengaturan",
                        tint = TextSecondary
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
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) IndigoPrimary else CardDark,
        border = BorderStroke(
            1.dp,
            if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = service.name,
            color = if (isSelected) Color.White else TextPrimary,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
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
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = country.flagEmoji,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = country.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = country.dialCode,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (country.count > 0) EmeraldAccent.copy(alpha = 0.15f)
                                    else RubyError.copy(alpha = 0.15f)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${country.count} pcs",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (country.count > 0) EmeraldAccent else RubyError,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (country.cost > 0) String.format(Locale.US, "$%.2f", country.cost) else "-",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldAccent
                )
                Button(
                    onClick = onBuyClick,
                    enabled = country.count > 0,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IndigoPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Beli",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Beli", style = MaterialTheme.typography.labelLarge)
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
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RadarScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RadarAlpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(140.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .clip(CircleShape)
                .background(IndigoPrimary.copy(alpha = 0.3f))
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(IndigoPrimary.copy(alpha = 0.5f))
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(IndigoPrimary)
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
        colors = CardDefaults.cardColors(containerColor = EmeraldAccent.copy(alpha = 0.12f)),
        border = BorderStroke(2.dp, EmeraldAccent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "KODE VERIFIKASI DITERIMA",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldAccent
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = otpCode,
                style = MonospaceOtpStyle,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCopyOtp,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldAccent)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Salin Kode",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salin Kode OTP", style = MaterialTheme.typography.labelLarge)
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "${secondsRemaining}s",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AmberWarning
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AmberWarning,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Batal Terkunci (${secondsRemaining}s)")
            }
        } else {
            Button(
                onClick = onCancelClick,
                colors = ButtonDefaults.buttonColors(containerColor = RubyError),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Batalkan & Refund Saldo", fontWeight = FontWeight.Bold)
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
        "COMPLETED" -> Triple(EmeraldAccent.copy(alpha = 0.15f), EmeraldAccent, "Selesai")
        "CANCELLED" -> Triple(RubyError.copy(alpha = 0.15f), RubyError, "Dibatalkan")
        "TIMEOUT" -> Triple(AmberWarning.copy(alpha = 0.15f), AmberWarning, "Kadaluarsa")
        "ACTIVE" -> Triple(CyanInfo.copy(alpha = 0.15f), CyanInfo, "Menunggu SMS")
        else -> Triple(TextMuted.copy(alpha = 0.15f), TextMuted, status)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ConfirmBuyDialog(
    serviceName: String,
    countryName: String,
    flag: String,
    cost: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Konfirmasi Pembelian Nomor", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Anda akan membeli nomor virtual untuk:", color = TextSecondary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = flag, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                    Text(text = "$countryName — $serviceName", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Tarif:", color = TextSecondary)
                    Text(
                        text = String.format(Locale.US, "$%.2f", cost),
                        fontWeight = FontWeight.Bold,
                        color = EmeraldAccent
                    )
                }
                Text(
                    text = "Catatan: Jika dalam 20 menit SMS tidak masuk, biaya akan dikembalikan secara penuh (No Code No Pay).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Text("Beli Sekarang")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
