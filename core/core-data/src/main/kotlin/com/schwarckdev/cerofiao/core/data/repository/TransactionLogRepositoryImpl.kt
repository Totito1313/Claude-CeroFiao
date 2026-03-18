package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.database.dao.TransactionLogDao
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.TransactionLogRepository
import com.schwarckdev.cerofiao.core.model.TransactionLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionLogRepositoryImpl @Inject constructor(
    private val transactionLogDao: TransactionLogDao,
) : TransactionLogRepository {

    override fun getRecentLogs(limit: Int): Flow<List<TransactionLog>> =
        transactionLogDao.getRecentLogs(limit).map { list -> list.map { it.toModel() } }

    override fun getLogsByTransactionId(transactionId: String): Flow<List<TransactionLog>> =
        transactionLogDao.getLogsByTransactionId(transactionId).map { list -> list.map { it.toModel() } }

    override suspend fun logAction(log: TransactionLog) {
        transactionLogDao.insert(log.toEntity())
    }
}
