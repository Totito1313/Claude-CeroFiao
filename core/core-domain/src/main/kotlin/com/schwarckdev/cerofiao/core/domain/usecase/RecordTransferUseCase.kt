package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RecordTransferUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(
        fromAccountId: String,
        toAccountId: String,
        amount: Double,
        receivedAmount: Double? = null,
        commissionPercent: Double? = null,
        note: String?,
    ) {
        val now = DateUtils.now()
        val prefs = userPreferencesRepository.userPreferences.first()
        val fromAccount = accountRepository.getAccountByIdOnce(fromAccountId) ?: return
        val toAccount = accountRepository.getAccountByIdOnce(toAccountId) ?: return

        val outgoingId = UuidGenerator.generate()
        val incomingId = UuidGenerator.generate()

        // Calculate commission
        val commission = commissionPercent?.let {
            MoneyCalculator.calculateCommission(amount, it)
        }

        // Get exchange rate for outgoing currency
        val outRate = if (fromAccount.currencyCode == "USD") 1.0 else {
            exchangeRateRepository.getLatestRateBySource(
                fromAccount.currencyCode, "USD", prefs.preferredRateSource,
            )?.rate ?: 1.0
        }

        val outAmountInUsd = MoneyCalculator.toUsd(amount, fromAccount.currencyCode, outRate)

        // Received amount (handles cross-currency transfers)
        val actualReceived = receivedAmount ?: (amount - (commission ?: 0.0))

        val inRate = if (toAccount.currencyCode == "USD") 1.0 else {
            exchangeRateRepository.getLatestRateBySource(
                toAccount.currencyCode, "USD", prefs.preferredRateSource,
            )?.rate ?: 1.0
        }

        val outgoing = Transaction(
            id = outgoingId,
            type = TransactionType.TRANSFER,
            amount = amount,
            currencyCode = fromAccount.currencyCode,
            accountId = fromAccountId,
            categoryId = null,
            note = note,
            date = now,
            createdAt = now,
            updatedAt = now,
            exchangeRateToUsd = outRate,
            exchangeRateSource = ExchangeRateSource.valueOf(prefs.preferredRateSource.name),
            amountInUsd = outAmountInUsd,
            transferLinkedId = incomingId,
            transferToAccountId = toAccountId,
            transferCommission = commission,
            transferCommissionCurrency = fromAccount.currencyCode,
        )

        val incoming = Transaction(
            id = incomingId,
            type = TransactionType.TRANSFER,
            amount = actualReceived,
            currencyCode = toAccount.currencyCode,
            accountId = toAccountId,
            categoryId = null,
            note = note,
            date = now,
            createdAt = now,
            updatedAt = now,
            exchangeRateToUsd = inRate,
            exchangeRateSource = ExchangeRateSource.valueOf(prefs.preferredRateSource.name),
            amountInUsd = MoneyCalculator.toUsd(actualReceived, toAccount.currencyCode, inRate),
            transferLinkedId = outgoingId,
            transferToAccountId = fromAccountId,
        )

        transactionRepository.insertTransferPair(outgoing, incoming)

        // Update balances
        accountRepository.updateBalance(fromAccountId, fromAccount.balance - amount)
        accountRepository.updateBalance(toAccountId, toAccount.balance + actualReceived)
    }
}
