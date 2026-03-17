package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RecordTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val resolveExchangeRate: ResolveExchangeRateUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(
        amount: Double,
        currencyCode: String,
        accountId: String,
        categoryId: String?,
        type: TransactionType,
        note: String?,
        date: Long = DateUtils.now(),
        manualRateToUsd: Double? = null,
    ): Transaction {
        val now = DateUtils.now()
        val prefs = userPreferencesRepository.userPreferences.first()

        // Get exchange rate to USD
        val rateToUsd: Double
        val rateSource: ExchangeRateSource

        if (manualRateToUsd != null) {
            rateToUsd = manualRateToUsd
            rateSource = ExchangeRateSource.MANUAL
        } else {
            val result = resolveExchangeRate.toUsd(currencyCode, prefs.preferredRateSource)
            rateToUsd = result.rate
            rateSource = result.source
        }

        val amountInUsd = MoneyCalculator.toUsd(amount, currencyCode, rateToUsd)

        val transaction = Transaction(
            id = UuidGenerator.generate(),
            type = type,
            amount = amount,
            currencyCode = currencyCode,
            accountId = accountId,
            categoryId = categoryId,
            note = note,
            date = date,
            createdAt = now,
            updatedAt = now,
            exchangeRateToUsd = rateToUsd,
            exchangeRateSource = rateSource,
            amountInUsd = amountInUsd,
        )

        transactionRepository.insertTransaction(transaction)

        // Update account balance
        val account = accountRepository.getAccountByIdOnce(accountId)
        if (account != null) {
            val newBalance = when (type) {
                TransactionType.INCOME -> account.balance + amount
                TransactionType.EXPENSE -> account.balance - amount
                TransactionType.TRANSFER -> account.balance - amount
            }
            accountRepository.updateBalance(accountId, newBalance)
        }

        return transaction
    }
}
