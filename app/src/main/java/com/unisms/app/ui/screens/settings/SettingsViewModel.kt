package com.unisms.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.unisms.app.data.repository.SmsBowerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val maskedApiKey: String = "",
    val isNotificationEnabled: Boolean = true,
    val isHapticEnabled: Boolean = true,
    val isKeyCleared: Boolean = false
)

class SettingsViewModel(
    private val repository: SmsBowerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val key = repository.getApiKey().orEmpty()
        val masked = if (key.length > 8) {
            "${key.take(4)}••••••••${key.takeLast(4)}"
        } else if (key.isNotEmpty()) {
            "••••••••"
        } else {
            "Belum disetel"
        }

        _uiState.value = _uiState.value.copy(
            maskedApiKey = masked,
            isNotificationEnabled = repository.isNotificationEnabled(),
            isHapticEnabled = repository.isHapticEnabled()
        )
    }

    fun setNotificationEnabled(enabled: Boolean) {
        repository.setNotificationEnabled(enabled)
        _uiState.value = _uiState.value.copy(isNotificationEnabled = enabled)
    }

    fun setHapticEnabled(enabled: Boolean) {
        repository.setHapticEnabled(enabled)
        _uiState.value = _uiState.value.copy(isHapticEnabled = enabled)
    }

    fun clearApiKey() {
        repository.clearApiKey()
        _uiState.value = _uiState.value.copy(
            maskedApiKey = "Belum disetel",
            isKeyCleared = true
        )
    }
}
