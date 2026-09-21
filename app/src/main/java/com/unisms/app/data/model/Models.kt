package com.unisms.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivationOrderV2(
    @SerialName("activationId")
    val activationId: Long,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("activationCost")
    val activationCost: String,
    @SerialName("countryCode")
    val countryCode: String? = null,
    @SerialName("canGetAnotherSms")
    val canGetAnotherSms: String? = "0",
    @SerialName("activationTime")
    val activationTime: String? = null,
    @SerialName("activationOperator")
    val activationOperator: String? = null
)

@Serializable
data class ServiceItem(
    val code: String,
    val name: String,
    val category: String = "Popular"
)

data class CountryItem(
    val id: String,
    val name: String,
    val flagEmoji: String,
    val dialCode: String,
    val cost: Double = 0.0,
    val count: Int = 0
)

sealed class OtpStatus {
    data object WaitingCode : OtpStatus()
    data class CodeReceived(val code: String) : OtpStatus()
    data class RetryWaiting(val lastCode: String? = null) : OtpStatus()
    data object Cancelled : OtpStatus()
    data object Timeout : OtpStatus()
    data object Completed : OtpStatus()
    data class Error(val message: String) : OtpStatus()
}

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: String? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}

enum class ActivationFilter {
    ALL,
    ACTIVE,
    COMPLETED,
    CANCELLED
}
