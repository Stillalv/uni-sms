package com.unisms.app.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivationRecordDao {

    @Query("SELECT * FROM activation_records ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<ActivationRecordEntity>>

    @Query("SELECT * FROM activation_records WHERE activationId = :activationId LIMIT 1")
    suspend fun getByActivationId(activationId: Long): ActivationRecordEntity?

    @Query("SELECT * FROM activation_records WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ActivationRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ActivationRecordEntity): Long

    @Update
    suspend fun update(record: ActivationRecordEntity)

    @Query("UPDATE activation_records SET otpCode = :otpCode WHERE activationId = :activationId")
    suspend fun updateOtp(activationId: Long, otpCode: String)

    @Query("UPDATE activation_records SET status = :status WHERE activationId = :activationId")
    suspend fun updateStatus(activationId: Long, status: String)

    @Query("UPDATE activation_records SET status = :status, otpCode = :otpCode WHERE activationId = :activationId")
    suspend fun updateStatusAndOtp(activationId: Long, status: String, otpCode: String)

    @Delete
    suspend fun delete(record: ActivationRecordEntity)

    @Query("DELETE FROM activation_records")
    suspend fun clearAll()
}
