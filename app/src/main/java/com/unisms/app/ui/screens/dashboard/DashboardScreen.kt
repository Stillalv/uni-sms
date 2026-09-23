package com.unisms.app.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unisms.app.ui.components.BalanceCard
import com.unisms.app.ui.components.ConfirmBuyDialog
import com.unisms.app.ui.components.CountryItemRow
import com.unisms.app.ui.components.ServiceFilterChip
import com.unisms.app.ui.components.ServicePickerDialog
import com.unisms.app.ui.theme.AppleBlue
import com.unisms.app.ui.theme.AppleHairline
import com.unisms.app.ui.theme.AppleSecondaryBg
import com.unisms.app.ui.theme.AppleSystemBg
import com.unisms.app.ui.theme.AppleTertiaryBg
import com.unisms.app.ui.theme.AppleTextPrimary
import com.unisms.app.ui.theme.AppleTextSecondary
import com.unisms.app.ui.theme.CardDark
import com.unisms.app.ui.theme.IndigoPrimary
import com.unisms.app.ui.theme.LucideIcons
import com.unisms.app.ui.theme.TextPrimary
import com.unisms.app.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToActiveOtp: (Long) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.purchasedRecord) {
        uiState.purchasedRecord?.let { record ->
            viewModel.clearPurchasedRecord()
            onNavigateToActiveOtp(record.id)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppleSystemBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header: Balance + Settings (Apple Inset Glass Card)
            BalanceCard(
                balance = uiState.balance,
                isLoading = uiState.isBalanceLoading,
                onRefresh = { viewModel.refreshBalance() },
                onSettingsClick = onNavigateToSettings
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Apple Large Title Navigation Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "SMS GATEWAY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppleTextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Katalog OTP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppleTextPrimary
                    )
                }
                TextButton(
                    onClick = onNavigateToHistory,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        painter = LucideIcons.History,
                        contentDescription = "Riwayat",
                        tint = AppleBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Riwayat", color = AppleBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Apple Search Bar (Inset Capsule)
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Cari negara (Indonesia, Korea, USA)...", color = AppleTextSecondary, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(painter = LucideIcons.Search, contentDescription = "Search", tint = AppleTextSecondary, modifier = Modifier.size(16.dp))
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(painter = LucideIcons.X, contentDescription = "Clear", tint = AppleTextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppleBlue,
                    unfocusedBorderColor = AppleHairline,
                    focusedContainerColor = AppleSecondaryBg,
                    unfocusedContainerColor = AppleSecondaryBg
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Apple App Store Style Horizontal Services Quick Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "PILIH LAYANAN",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppleTextSecondary,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Semua (1063) ›",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppleBlue,
                    modifier = Modifier.clickable { viewModel.openServicePicker() }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                items(uiState.services) { service ->
                    ServiceFilterChip(
                        service = service,
                        isSelected = service.code == uiState.selectedService.code,
                        onClick = { viewModel.selectService(service) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filtered Countries List
            val filteredCountries = remember(uiState.countries, uiState.searchQuery) {
                if (uiState.searchQuery.isBlank()) {
                    uiState.countries
                } else {
                    val q = uiState.searchQuery.trim().lowercase()
                    uiState.countries.filter {
                        it.name.lowercase().contains(q) ||
                        it.dialCode.contains(q) ||
                        it.id.contains(q)
                    }
                }
            }

            if (uiState.isCatalogLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = AppleBlue)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Memuat stok negara untuk ${uiState.selectedService.name}...",
                            color = AppleTextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else if (filteredCountries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (uiState.searchQuery.isNotEmpty()) "Tidak ada negara yang cocok dengan '${uiState.searchQuery}'"
                        else "Tidak ada stok nomor untuk layanan ini saat ini.",
                        color = AppleTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredCountries, key = { it.id }) { country ->
                        CountryItemRow(
                            country = country,
                            onBuyClick = { viewModel.initiateBuy(country) }
                        )
                    }
                }
            }
        }

        // Apple Bottom Sheet Confirmation Dialog
        uiState.confirmBuyCountry?.let { country ->
            ConfirmBuyDialog(
                serviceName = uiState.selectedService.name,
                countryName = country.name,
                flag = country.flagEmoji,
                baseCost = country.cost,
                providers = uiState.providers,
                isLoadingProviders = uiState.isLoadingProviders,
                selectedProvider = uiState.selectedProvider,
                onProviderSelected = { viewModel.selectProvider(it) },
                onConfirm = { viewModel.confirmBuy() },
                onDismiss = { viewModel.dismissConfirmBuy() },
                countryIso = country.isoCode,
                serviceCode = uiState.selectedService.code
            )
        }

        // Searchable Service Picker Dialog
        if (uiState.isServicePickerOpen) {
            ServicePickerDialog(
                services = uiState.services,
                selectedService = uiState.selectedService,
                onSelectService = { viewModel.selectService(it) },
                onDismiss = { viewModel.closeServicePicker() }
            )
        }
    }
}
