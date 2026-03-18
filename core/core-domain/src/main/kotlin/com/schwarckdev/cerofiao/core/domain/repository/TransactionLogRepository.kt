package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.TransactionLog
import kotlinx.coroutines.flow.Flow

interface TransactionLogRepository {
    fun getRecentLogs(limit: Int = 50): Flow<List<TransactionLog>>
    fun getLogsByTransactionId(transactionId: String): Flow<List<TransactionLog>>
    suspend fun logAction(log: TransactionLog)
}
