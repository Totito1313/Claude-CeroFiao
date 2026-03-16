package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(): Flow<List<Transaction>> = transactionRepository.getAllTransactions()

    fun recent(limit: Int = 10): Flow<List<Transaction>> =
        transactionRepository.getRecentTransactions(limit)

    fun byAccount(accountId: String): Flow<List<Transaction>> =
        transactionRepository.getTransactionsByAccount(accountId)

    fun byDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionRepository.getTransactionsByDateRange(startDate, endDate)
}
