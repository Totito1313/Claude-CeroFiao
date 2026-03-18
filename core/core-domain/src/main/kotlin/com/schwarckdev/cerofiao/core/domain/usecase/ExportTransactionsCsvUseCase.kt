package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExportTransactionsCsvUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
) {
    data class CsvRow(
        val fecha: String,
        val tipo: String,
        val monto: Double,
        val moneda: String,
        val montoUsd: Double,
        val tasaUsd: Double,
        val fuenteTasa: String,
        val categoria: String,
        val cuenta: String,
        val nota: String,
    )

    suspend operator fun invoke(
        startDate: Long? = null,
        endDate: Long? = null,
    ): List<CsvRow> {
        val transactions = if (startDate != null && endDate != null) {
            transactionRepository.getTransactionsByDateRange(startDate, endDate).first()
        } else {
            transactionRepository.getAllTransactions().first()
        }

        val accounts = accountRepository.getAllAccounts().first().associateBy { it.id }
        val categories = categoryRepository.getActiveCategories().first().associateBy { it.id }

        return transactions.map { tx ->
            CsvRow(
                fecha = formatDate(tx.date),
                tipo = tx.type.name,
                monto = tx.amount,
                moneda = tx.currencyCode,
                montoUsd = tx.amountInUsd,
                tasaUsd = tx.exchangeRateToUsd,
                fuenteTasa = tx.exchangeRateSource?.name ?: "",
                categoria = tx.categoryId?.let { categories[it]?.name } ?: "",
                cuenta = accounts[tx.accountId]?.name ?: "",
                nota = tx.note ?: "",
            )
        }
    }

    private fun formatDate(epochMillis: Long): String = DateUtils.formatDisplayDate(epochMillis)
}
