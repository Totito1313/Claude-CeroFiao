package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.database.dao.TransactionDao
import com.schwarckdev.cerofiao.core.database.dao.TransactionLogDao
import com.schwarckdev.cerofiao.core.database.entity.TransactionLogEntity
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionLogAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val transactionLogDao: TransactionLogDao,
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions().map { list -> list.map { it.toModel() } }

    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> =
        transactionDao.getRecentTransactions(limit).map { list -> list.map { it.toModel() } }

    override fun getTransactionsByAccount(accountId: String): Flow<List<Transaction>> =
        transactionDao.getTransactionsByAccount(accountId).map { list -> list.map { it.toModel() } }

    override fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate).map { list -> list.map { it.toModel() } }

    override fun getTransactionsByTypeAndDateRange(type: String, startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByTypeAndDateRange(type, startDate, endDate).map { list -> list.map { it.toModel() } }

    override fun getTransactionById(id: String): Flow<Transaction?> =
        transactionDao.getTransactionById(id).map { it?.toModel() }

    override fun getTotalExpensesInUsdForPeriod(startDate: Long, endDate: Long): Flow<Double?> =
        transactionDao.getTotalExpensesInUsdForPeriod(startDate, endDate)

    override fun getTotalIncomeInUsdForPeriod(startDate: Long, endDate: Long): Flow<Double?> =
        transactionDao.getTotalIncomeInUsdForPeriod(startDate, endDate)

    override fun getExpensesByCategoryForPeriod(startDate: Long, endDate: Long): Flow<List<Pair<String, Double>>> =
        transactionDao.getExpensesByCategoryForPeriod(startDate, endDate).map { list ->
            list.map { it.categoryId to it.total }
        }

    override suspend fun getTransactionByIdOnce(id: String): Transaction? =
        transactionDao.getTransactionByIdOnce(id)?.toModel()

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insert(transaction.toEntity())
        logTransaction(transaction, TransactionLogAction.CREATED)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.insert(transaction.toEntity())
        logTransaction(transaction, TransactionLogAction.EDITED)
    }

    override suspend fun insertTransferPair(outgoing: Transaction, incoming: Transaction) {
        transactionDao.insertTransferPair(outgoing.toEntity(), incoming.toEntity())
        logTransaction(outgoing, TransactionLogAction.CREATED)
        logTransaction(incoming, TransactionLogAction.CREATED)
    }

    override suspend fun deleteTransaction(id: String) {
        val existing = transactionDao.getTransactionByIdOnce(id)?.toModel()
        transactionDao.softDelete(id, DateUtils.now())
        if (existing != null) {
            logTransaction(existing, TransactionLogAction.DELETED)
        }
    }

    private suspend fun logTransaction(transaction: Transaction, action: TransactionLogAction) {
        val snapshot = Json.encodeToString(transaction)
        transactionLogDao.insert(
            TransactionLogEntity(
                id = UuidGenerator.generate(),
                transactionId = transaction.id,
                action = action.name,
                timestamp = DateUtils.now(),
                snapshotJson = snapshot,
            ),
        )
    }
}
