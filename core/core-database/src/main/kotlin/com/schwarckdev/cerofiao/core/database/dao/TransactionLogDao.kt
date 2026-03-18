package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.schwarckdev.cerofiao.core.database.entity.TransactionLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionLogDao {

    @Query("SELECT * FROM transaction_logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLogs(limit: Int = 50): Flow<List<TransactionLogEntity>>

    @Query("SELECT * FROM transaction_logs WHERE transactionId = :transactionId ORDER BY timestamp DESC")
    fun getLogsByTransactionId(transactionId: String): Flow<List<TransactionLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: TransactionLogEntity)

    @Query("DELETE FROM transaction_logs WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOlderThan(beforeTimestamp: Long)
}
