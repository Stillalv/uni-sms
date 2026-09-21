package com.unisms.app.ui.screens.activation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisms.app.data.local.db.ActivationRecordEntity
import com.unisms.app.data.model.OtpStatus
import com.unisms.app.data.model.Resource
import com.unisms.app.data.repository.SmsBowerRepository
import com.unisms.app.ui.util.HapticHelper
import com.unisms.app.ui.util.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ActiveOtpUiState(
    val record: ActivationRecordEntity? = null,
    val isLoading: Boolean = true,
    val otpCode: String? = null,
    val isPolling: Boolean = false,
    val secondsUntilCanCancel: Int = 120, // 2-minute lock (120s)
    val secondsUntilExpire: Int = 1200,   // 20-minute session (1200s)
    val statusMessage: String = "Menunggu SMS masuk...",
    val isCancelled: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null,
    val actionMessage: String? = null,
    val isActionInProgress: Boolean = false
)

class ActiveOtpViewModel(
    private val recordId: Long,
    private val repository: SmsBowerRepository,
    private val notificationHelper: NotificationHelper,
    private val hapticHelper: HapticHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveOtpUiState())
    val uiState: StateFlow<ActiveOtpUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null
    private var timerJob: Job? = null

    init {
        loadRecord()
    }

    private fun loadRecord() {
        viewModelScope.launch {
            val record = repository.getRecordById(recordId)
            if (record != null) {
                val elapsedSeconds = ((System.currentTimeMillis() - record.createdAt) / 1000).toInt()
                val initialCancelSecs = (120 - elapsedSeconds).coerceAtLeast(0)
                val initialExpireSecs = (1200 - elapsedSeconds).coerceAtLeast(0)

                _uiState.value = _uiState.value.copy(
                    record = record,
                    isLoading = false,
                    otpCode = record.otpCode,
                    secondsUntilCanCancel = initialCancelSecs,
                    secondsUntilExpire = initialExpireSecs,
                    isCompleted = record.status == "COMPLETED",
                    isCancelled = record.status == "CANCELLED"
                )

                startTimers()

                if (record.status == "ACTIVE" && record.otpCode.isNullOrEmpty()) {
                    startPolling(record.activationId)
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Data aktivasi tidak ditemukan."
                )
            }
        }
    }

    private fun startTimers() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                val currentCancel = _uiState.value.secondsUntilCanCancel
                val currentExpire = _uiState.value.secondsUntilExpire

                val nextCancel = (currentCancel - 1).coerceAtLeast(0)
                val nextExpire = (currentExpire - 1).coerceAtLeast(0)

                _uiState.value = _uiState.value.copy(
                    secondsUntilCanCancel = nextCancel,
                    secondsUntilExpire = nextExpire
                )

                if (nextExpire <= 0 && !_uiState.value.isCompleted && !_uiState.value.isCancelled) {
                    stopPolling()
                    _uiState.value = _uiState.value.copy(
                        statusMessage = "Sesi 20 menit berakhir. Saldo dikembalikan (No Code No Pay).",
                        isCancelled = true
                    )
                    break
                }
            }
        }
    }

    private fun startPolling(activationId: Long) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPolling = true)
            while (isActive && !_uiState.value.isCompleted && !_uiState.value.isCancelled) {
                delay(3500) // 3.5s safe polling interval

                when (val result = repository.pollOtpStatus(activationId)) {
                    is Resource.Success -> {
                        when (val status = result.data) {
                            is OtpStatus.CodeReceived -> {
                                onOtpReceived(status.code)
                                break
                            }
                            is OtpStatus.WaitingCode -> {
                                _uiState.value = _uiState.value.copy(statusMessage = "Menunggu SMS masuk...")
                            }
                            is OtpStatus.RetryWaiting -> {
                                _uiState.value = _uiState.value.copy(statusMessage = "Menunggu SMS kedua...")
                            }
                            is OtpStatus.Cancelled -> {
                                stopPolling()
                                _uiState.value = _uiState.value.copy(
                                    isCancelled = true,
                                    statusMessage = "Aktivasi telah dibatalkan."
                                )
                                break
                            }
                            is OtpStatus.Error -> {
                                _uiState.value = _uiState.value.copy(statusMessage = status.message)
                            }
                            else -> {}
                        }
                    }
                    is Resource.Error -> {
                        // Keep polling silently unless terminal
                    }
                    is Resource.Loading -> {}
                }
            }
            _uiState.value = _uiState.value.copy(isPolling = false)
        }
    }

    private fun onOtpReceived(code: String) {
        val record = _uiState.value.record
        _uiState.value = _uiState.value.copy(
            otpCode = code,
            isCompleted = true,
            isPolling = false,
            statusMessage = "Kode verifikasi berhasil diterima!"
        )

        // Trigger notification and haptic vibration
        if (record != null) {
            if (repository.isNotificationEnabled()) {
                notificationHelper.showOtpNotification(record.serviceName, code, record.phoneNumber)
            }
            if (repository.isHapticEnabled()) {
                hapticHelper.vibrateSuccess()
            }
        }
    }

    fun cancelOrder() {
        val record = _uiState.value.record ?: return
        if (_uiState.value.secondsUntilCanCancel > 0) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Nomor baru bisa dibatalkan setelah 2 menit sejak pembelian."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isActionInProgress = true, errorMessage = null)
            when (val result = repository.cancelOrder(record.activationId)) {
                is Resource.Success -> {
                    stopPolling()
                    _uiState.value = _uiState.value.copy(
                        isActionInProgress = false,
                        isCancelled = true,
                        actionMessage = "Nomor berhasil dibatalkan dan saldo dikembalikan."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isActionInProgress = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun completeOrder() {
        val record = _uiState.value.record ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isActionInProgress = true, errorMessage = null)
            when (val result = repository.completeOrder(record.activationId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isActionInProgress = false,
                        actionMessage = "Aktivasi selesai dikonfirmasi."
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isActionInProgress = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun requestSecondSms() {
        val record = _uiState.value.record ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isActionInProgress = true, errorMessage = null)
            when (val result = repository.requestSecondSms(record.activationId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isActionInProgress = false,
                        actionMessage = "Permintaan SMS kedua dikirim. Menunggu SMS baru..."
                    )
                    startPolling(record.activationId)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isActionInProgress = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, actionMessage = null)
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
        timerJob?.cancel()
    }
}
