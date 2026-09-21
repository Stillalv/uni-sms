package com.unisms.app.data.repository

import com.unisms.app.data.api.ApiClient
import com.unisms.app.data.api.SmsBowerResponseParser
import com.unisms.app.data.local.SecurePreferencesManager
import com.unisms.app.data.local.db.ActivationRecordDao
import com.unisms.app.data.local.db.ActivationRecordEntity
import com.unisms.app.data.model.ActivationOrderV2
import com.unisms.app.data.model.CountryItem
import com.unisms.app.data.model.OtpStatus
import com.unisms.app.data.model.Resource
import com.unisms.app.data.model.ServiceItem
import com.unisms.app.ui.util.ServiceCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SmsBowerRepository(
    private val securePrefs: SecurePreferencesManager,
    private val recordDao: ActivationRecordDao
) {
    private val apiService = ApiClient.apiService

    fun getApiKey(): String? = securePrefs.getApiKey()

    fun saveApiKey(key: String) = securePrefs.saveApiKey(key)

    fun clearApiKey() = securePrefs.clearApiKey()

    fun hasApiKey(): Boolean = securePrefs.hasApiKey()

    fun isNotificationEnabled(): Boolean = securePrefs.isNotificationEnabled()
    fun setNotificationEnabled(enabled: Boolean) = securePrefs.setNotificationEnabled(enabled)

    fun isHapticEnabled(): Boolean = securePrefs.isHapticEnabled()
    fun setHapticEnabled(enabled: Boolean) = securePrefs.setHapticEnabled(enabled)

    suspend fun getBalance(): Resource<Double> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.getBalance(apiKey)
            SmsBowerResponseParser.parseBalance(response)
        } catch (e: Exception) {
            Resource.Error("Gagal menghubungi server: ${e.localizedMessage}")
        }
    }

    suspend fun validateApiKey(keyToTest: String): Resource<Double> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBalance(keyToTest.trim())
            SmsBowerResponseParser.parseBalance(response)
        } catch (e: Exception) {
            Resource.Error("Koneksi gagal: ${e.localizedMessage}")
        }
    }

    suspend fun getServices(): Resource<List<ServiceItem>> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.getServicesList(apiKey)
            val services = SmsBowerResponseParser.parseServices(response)
            Resource.Success(services)
        } catch (_: Exception) {
            // Fallback to built-in default popular services
            Resource.Success(ServiceCatalog.defaultServices)
        }
    }

    suspend fun getPrices(serviceCode: String): Resource<List<CountryItem>> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.getPrices(apiKey = apiKey, service = serviceCode)
            SmsBowerResponseParser.parsePrices(response, serviceCode)
        } catch (e: Exception) {
            Resource.Error("Gagal memuat katalog negara: ${e.localizedMessage}")
        }
    }

    suspend fun getTopCountries(serviceCode: String): List<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext emptyList()
        try {
            val response = apiService.getTopCountriesByService(apiKey = apiKey, service = serviceCode)
            SmsBowerResponseParser.parseTopCountries(response)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun buyNumberV2(
        serviceCode: String,
        serviceName: String,
        countryId: String,
        countryName: String,
        flag: String
    ): Resource<ActivationRecordEntity> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.getNumberV2(apiKey = apiKey, service = serviceCode, country = countryId)
            val parseResult = SmsBowerResponseParser.parseBuyNumber(response)
            when (parseResult) {
                is Resource.Success -> {
                    val order = parseResult.data
                    val entity = ActivationRecordEntity(
                        activationId = order.activationId,
                        phoneNumber = order.phoneNumber,
                        serviceCode = serviceCode,
                        serviceName = serviceName,
                        countryId = countryId,
                        countryName = countryName,
                        flagEmoji = flag,
                        cost = order.activationCost.toDoubleOrNull() ?: 0.0,
                        status = "ACTIVE",
                        createdAt = System.currentTimeMillis(),
                        canGetAnotherSms = order.canGetAnotherSms == "1"
                    )
                    val insertedId = recordDao.insert(entity)
                    Resource.Success(entity.copy(id = insertedId))
                }
                is Resource.Error -> Resource.Error(parseResult.message, parseResult.code)
                is Resource.Loading -> Resource.Loading
            }
        } catch (e: Exception) {
            Resource.Error("Koneksi gagal saat memesan nomor: ${e.localizedMessage}")
        }
    }

    suspend fun pollOtpStatus(activationId: Long): Resource<OtpStatus> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.getStatus(apiKey = apiKey, activationId = activationId)
            val statusResult = SmsBowerResponseParser.parseOtpStatus(response)
            if (statusResult is Resource.Success) {
                when (val status = statusResult.data) {
                    is OtpStatus.CodeReceived -> {
                        recordDao.updateStatusAndOtp(activationId, "COMPLETED", status.code)
                    }
                    is OtpStatus.Cancelled -> {
                        recordDao.updateStatus(activationId, "CANCELLED")
                    }
                    else -> {}
                }
            }
            statusResult
        } catch (e: Exception) {
            Resource.Error("Polling error: ${e.localizedMessage}")
        }
    }

    suspend fun cancelOrder(activationId: Long): Resource<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.setStatus(apiKey = apiKey, activationId = activationId, status = 8)
            val result = SmsBowerResponseParser.parseSetStatusResponse(response, 8)
            if (result is Resource.Success) {
                recordDao.updateStatus(activationId, "CANCELLED")
            }
            result
        } catch (e: Exception) {
            Resource.Error("Gagal membatalkan pesanan: ${e.localizedMessage}")
        }
    }

    suspend fun completeOrder(activationId: Long): Resource<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.setStatus(apiKey = apiKey, activationId = activationId, status = 6)
            val result = SmsBowerResponseParser.parseSetStatusResponse(response, 6)
            if (result is Resource.Success) {
                recordDao.updateStatus(activationId, "COMPLETED")
            }
            result
        } catch (e: Exception) {
            Resource.Error("Gagal menyelesaikan aktivasi: ${e.localizedMessage}")
        }
    }

    suspend fun requestSecondSms(activationId: Long): Resource<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: return@withContext Resource.Error("API Key belum disetel.", "NO_KEY")
        try {
            val response = apiService.setStatus(apiKey = apiKey, activationId = activationId, status = 3)
            SmsBowerResponseParser.parseSetStatusResponse(response, 3)
        } catch (e: Exception) {
            Resource.Error("Gagal meminta SMS kedua: ${e.localizedMessage}")
        }
    }

    fun getHistory(): Flow<List<ActivationRecordEntity>> = recordDao.getAllFlow()

    suspend fun getRecordById(id: Long): ActivationRecordEntity? = withContext(Dispatchers.IO) {
        recordDao.getById(id)
    }

    suspend fun deleteRecord(record: ActivationRecordEntity) = withContext(Dispatchers.IO) {
        recordDao.delete(record)
    }
}
