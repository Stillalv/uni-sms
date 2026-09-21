package com.unisms.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisms.app.data.local.db.ActivationRecordEntity
import com.unisms.app.data.model.CountryItem
import com.unisms.app.data.model.ProviderItem
import com.unisms.app.data.model.Resource
import com.unisms.app.data.model.ServiceItem
import com.unisms.app.data.repository.SmsBowerRepository
import com.unisms.app.ui.util.ServiceCatalog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val balance: Double? = null,
    val isBalanceLoading: Boolean = false,
    val services: List<ServiceItem> = ServiceCatalog.defaultServices,
    val selectedService: ServiceItem = ServiceCatalog.defaultServices.first(),
    val isServicePickerOpen: Boolean = false,
    val countries: List<CountryItem> = emptyList(),
    val topCountryIds: List<String> = emptyList(),
    val isCatalogLoading: Boolean = false,
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val isPurchasing: Boolean = false,
    val confirmBuyCountry: CountryItem? = null,
    val providers: List<ProviderItem> = emptyList(),
    val isLoadingProviders: Boolean = false,
    val selectedProvider: ProviderItem? = null,
    val purchasedRecord: ActivationRecordEntity? = null
)

class DashboardViewModel(
    private val repository: SmsBowerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        refreshBalance()
        loadServices()
    }

    fun refreshBalance() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBalanceLoading = true, errorMessage = null)
            when (val result = repository.getBalance()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(balance = result.data, isBalanceLoading = false)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isBalanceLoading = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun loadServices() {
        viewModelScope.launch {
            when (val result = repository.getServices()) {
                is Resource.Success -> {
                    val list = if (result.data.isNotEmpty()) result.data else ServiceCatalog.defaultServices
                    _uiState.value = _uiState.value.copy(
                        services = list,
                        selectedService = list.first()
                    )
                    loadPricesForSelectedService()
                }
                else -> {
                    loadPricesForSelectedService()
                }
            }
        }
    }

    fun selectService(service: ServiceItem) {
        if (_uiState.value.selectedService.code == service.code) return
        _uiState.value = _uiState.value.copy(selectedService = service)
        loadPricesForSelectedService()
    }

    fun loadPricesForSelectedService() {
        val currentService = _uiState.value.selectedService
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCatalogLoading = true, errorMessage = null)
            val topCountries = repository.getTopCountries(currentService.code)
            _uiState.value = _uiState.value.copy(topCountryIds = topCountries)

            when (val result = repository.getPrices(currentService.code)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        countries = result.data,
                        isCatalogLoading = false
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        countries = emptyList(),
                        isCatalogLoading = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun openServicePicker() {
        _uiState.value = _uiState.value.copy(isServicePickerOpen = true)
    }

    fun closeServicePicker() {
        _uiState.value = _uiState.value.copy(isServicePickerOpen = false)
    }

    fun selectProvider(provider: ProviderItem?) {
        _uiState.value = _uiState.value.copy(selectedProvider = provider)
    }

    fun initiateBuy(country: CountryItem) {
        val currentService = _uiState.value.selectedService
        _uiState.value = _uiState.value.copy(
            confirmBuyCountry = country,
            selectedProvider = null,
            providers = emptyList(),
            isLoadingProviders = true
        )

        viewModelScope.launch {
            val fetchedProviders = repository.getProviders(currentService.code, country.id)
            _uiState.value = _uiState.value.copy(
                providers = fetchedProviders,
                isLoadingProviders = false
            )
        }
    }

    fun dismissConfirmBuy() {
        _uiState.value = _uiState.value.copy(
            confirmBuyCountry = null,
            selectedProvider = null,
            providers = emptyList(),
            isLoadingProviders = false
        )
    }

    fun confirmBuy() {
        val country = _uiState.value.confirmBuyCountry ?: return
        val service = _uiState.value.selectedService
        val provider = _uiState.value.selectedProvider
        _uiState.value = _uiState.value.copy(isPurchasing = true, confirmBuyCountry = null, errorMessage = null)

        viewModelScope.launch {
            when (val result = repository.buyNumberV2(
                serviceCode = service.code,
                serviceName = service.name,
                countryId = country.id,
                countryName = country.name,
                flag = country.flagEmoji,
                providerId = provider?.id
            )) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isPurchasing = false,
                        purchasedRecord = result.data
                    )
                    refreshBalance()
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isPurchasing = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearPurchasedRecord() {
        _uiState.value = _uiState.value.copy(purchasedRecord = null)
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
