package com.unisms.app.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisms.app.data.model.Resource
import com.unisms.app.data.repository.SmsBowerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val apiKey: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class OnboardingViewModel(
    private val repository: SmsBowerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        val existingKey = repository.getApiKey()
        if (!existingKey.isNullOrEmpty()) {
            _uiState.value = _uiState.value.copy(apiKey = existingKey)
        }
    }

    fun onApiKeyChanged(newKey: String) {
        _uiState.value = _uiState.value.copy(apiKey = newKey, errorMessage = null)
    }

    fun validateAndSave() {
        val key = _uiState.value.apiKey.trim()
        if (key.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Silakan masukkan API Key SMSBower Anda.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = repository.validateApiKey(key)) {
                is Resource.Success -> {
                    repository.saveApiKey(key)
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }
}
