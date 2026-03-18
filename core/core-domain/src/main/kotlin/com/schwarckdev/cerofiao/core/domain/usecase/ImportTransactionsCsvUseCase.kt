package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ImportTransactionsCsvUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
) {
    data class ImportResult(
        val imported: Int,
        val skipped: Int,
        val errors: List<String>,
    )

    /**
     * Imports CSV rows. Each row is an array of strings matching the export format:
     * [fecha, tipo, monto, moneda, monto_usd, tasa_usd, fuente_tasa, categoria, cuenta, nota]
     */
    suspend operator fun invoke(rows: List<Array<String>>): ImportResult {
        val accounts = accountRepository.getAllAccounts().first().associateBy { it.name }
        val categories = categoryRepository.getActiveCategories().first().associateBy { it.name }

        var imported = 0
        var skipped = 0
        val errors = mutableListOf<String>()

        for ((index, row) in rows.withIndex()) {
            try {
                if (row.size < 10) {
                    errors.add("Fila ${index + 1}: columnas insuficientes (${row.size}/10)")
                    skipped++
                    continue
                }

                val fecha = row[0].trim()
                val tipo = row[1].trim()
                val monto = row[2].trim().toDoubleOrNull()
                val moneda = row[3].trim()
                val montoUsd = row[4].trim().toDoubleOrNull() ?: 0.0
                val tasaUsd = row[5].trim().toDoubleOrNull() ?: 1.0
                val fuenteTasa = row[6].trim()
                val categoriaName = row[7].trim()
                val cuentaName = row[8].trim()
                val nota = row[9].trim()

                if (monto == null || monto <= 0) {
                    errors.add("Fila ${index + 1}: monto inválido")
                    skipped++
                    continue
                }

                val type = try {
                    TransactionType.valueOf(tipo)
                } catch (_: Exception) {
                    errors.add("Fila ${index + 1}: tipo '$tipo' no reconocido")
                    skipped++
                    continue
                }

                val account = accounts[cuentaName]
                if (account == null) {
                    errors.add("Fila ${index + 1}: cuenta '$cuentaName' no encontrada")
                    skipped++
                    continue
                }

                val categoryId = if (categoriaName.isNotBlank()) categories[categoriaName]?.id else null
                val rateSource = fuenteTasa.toExchangeRateSourceOrNull()
                val date = DateUtils.parseDdMmYyyy(fecha) ?: DateUtils.now()
                val now = DateUtils.now()

                val transaction = Transaction(
                    id = UuidGenerator.generate(),
                    type = type,
                    amount = monto,
                    currencyCode = moneda.ifBlank { account.currencyCode },
                    accountId = account.id,
                    categoryId = categoryId,
                    note = nota.ifBlank { null },
                    date = date,
                    createdAt = now,
                    updatedAt = now,
                    exchangeRateToUsd = tasaUsd,
                    exchangeRateSource = rateSource,
                    amountInUsd = montoUsd,
                )

                transactionRepository.insertTransaction(transaction)

                // Update account balance
                val currentAccount = accountRepository.getAccountByIdOnce(account.id)
                if (currentAccount != null) {
                    val newBalance = when (type) {
                        TransactionType.INCOME -> currentAccount.balance + monto
                        TransactionType.EXPENSE -> currentAccount.balance - monto
                        TransactionType.TRANSFER -> currentAccount.balance - monto
                    }
                    accountRepository.updateBalance(account.id, newBalance)
                }

                imported++
            } catch (e: Exception) {
                errors.add("Fila ${index + 1}: ${e.message}")
                skipped++
            }
        }

        return ImportResult(imported = imported, skipped = skipped, errors = errors)
    }

    private fun String.toExchangeRateSourceOrNull(): ExchangeRateSource? {
        return try {
            if (isBlank()) null else ExchangeRateSource.valueOf(this)
        } catch (_: Exception) {
            null
        }
    }
}
