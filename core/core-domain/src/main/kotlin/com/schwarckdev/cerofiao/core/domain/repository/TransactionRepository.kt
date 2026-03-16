package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>
    fun getTransactionsByAccount(accountId: String): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getTransactionsByTypeAndDateRange(type: String, startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getTransactionById(id: String): Flow<Transaction?>
    fun getTotalExpensesInUsdForPeriod(startDate: Long, endDate: Long): Flow<Double?>
    fun getTotalIncomeInUsdForPeriod(startDate: Long, endDate: Long): Flow<Double?>
    fun getExpensesByCategoryForPeriod(startDate: Long, endDate: Long): Flow<List<Pair<String, Double>>>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun insertTransferPair(outgoing: Transaction, incoming: Transaction)
    suspend fun deleteTransaction(id: String)
}
