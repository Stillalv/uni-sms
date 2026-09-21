package com.unisms.app.ui.screens.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.unisms.app.ui.components.BalanceCard
import com.unisms.app.ui.components.ConfirmBuyDialog
import com.unisms.app.ui.components.CountryItemRow
import com.unisms.app.ui.components.ServiceFilterChip
import com.unisms.app.ui.theme.CardDark
import com.unisms.app.ui.theme.IndigoPrimary
import com.unisms.app.ui.theme.TextPrimary
import com.unisms.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
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
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Header: Balance + Settings
            BalanceCard(
                balance = uiState.balance,
                isLoading = uiState.isBalanceLoading,
                onRefresh = { viewModel.refreshBalance() },
                onSettingsClick = onNavigateToSettings
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Nav Row (Riwayat button & Search)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Katalog Aktivasi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                TextButton(onClick = onNavigateToHistory) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Riwayat",
                        tint = IndigoPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Riwayat", color = IndigoPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            // Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Cari negara atau layanan...", color = TextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TextSecondary)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IndigoPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = CardDark,
                    unfocusedContainerColor = CardDark
                )
            )

            // Horizontal Services Selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                items(uiState.services) { service ->
                    ServiceFilterChip(
                        service = service,
                        isSelected = service.code == uiState.selectedService.code,
                        onClick = { viewModel.selectService(service) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filtered Countries list
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
                        CircularProgressIndicator(color = IndigoPrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Memuat stok negara untuk ${uiState.selectedService.name}...",
                            color = TextSecondary,
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
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
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

        // Purchase Confirmation Dialog
        uiState.confirmBuyCountry?.let { country ->
            ConfirmBuyDialog(
                serviceName = uiState.selectedService.name,
                countryName = country.name,
                flag = country.flagEmoji,
                cost = country.cost,
                onConfirm = { viewModel.confirmBuy() },
                onDismiss = { viewModel.dismissConfirmBuy() }
            )
        }
    }
}
