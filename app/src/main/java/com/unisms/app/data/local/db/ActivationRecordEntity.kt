package com.unisms.app.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activation_records")
data class ActivationRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val activationId: Long,
    val phoneNumber: String,
    val serviceCode: String,
    val serviceName: String,
    val countryId: String,
    val countryName: String,
    val flagEmoji: String,
    val cost: Double,
    val otpCode: String? = null,
    val status: String = "ACTIVE", // ACTIVE, COMPLETED, CANCELLED, TIMEOUT
    val createdAt: Long = System.currentTimeMillis(),
    val canGetAnotherSms: Boolean = false
)
