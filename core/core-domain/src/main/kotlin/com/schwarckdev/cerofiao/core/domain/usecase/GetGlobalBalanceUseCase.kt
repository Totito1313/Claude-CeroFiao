package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.AccountBalance
import com.schwarckdev.cerofiao.core.model.CurrencyBalance
import com.schwarckdev.cerofiao.core.model.GlobalBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGlobalBalanceUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<GlobalBalance> = flow {
        combine(
            accountRepository.getAccountsIncludedInTotal(),
            userPreferencesRepository.userPreferences,
        ) { accounts, prefs ->
            val displayCurrency = prefs.displayCurrencyCode

            val accountBalances = accounts.map { account ->
                val rateToDisplay = if (account.currencyCode == displayCurrency) {
                    1.0
                } else {
                    // Convert via USD as intermediate
                    val toUsd = if (account.currencyCode == "USD") 1.0 else {
                        exchangeRateRepository.getLatestRateBySource(
                            account.currencyCode, "USD", prefs.preferredRateSource,
                        )?.rate ?: exchangeRateRepository.getLatestRate(account.currencyCode, "USD")?.rate ?: 1.0
                    }
                    val fromUsd = if (displayCurrency == "USD") 1.0 else {
                        val rate = exchangeRateRepository.getLatestRateBySource(
                            "USD", displayCurrency, prefs.preferredRateSource,
                        )?.rate ?: exchangeRateRepository.getLatestRate("USD", displayCurrency)?.rate ?: 1.0
                        rate
                    }
                    toUsd * fromUsd
                }

                AccountBalance(
                    account = account,
                    balanceInOriginalCurrency = account.balance,
                    balanceInDisplayCurrency = MoneyCalculator.convert(account.balance, rateToDisplay),
                )
            }

            val breakdownByCurrency = accountBalances
                .groupBy { it.account.currencyCode }
                .map { (currencyCode, balances) ->
                    CurrencyBalance(
                        currencyCode = currencyCode,
                        totalInOriginalCurrency = balances.sumOf { it.balanceInOriginalCurrency },
                        totalInDisplayCurrency = balances.sumOf { it.balanceInDisplayCurrency },
                    )
                }

            GlobalBalance(
                totalInDisplayCurrency = accountBalances.sumOf { it.balanceInDisplayCurrency },
                displayCurrencyCode = displayCurrency,
                breakdownByAccount = accountBalances,
                breakdownByCurrency = breakdownByCurrency,
            )
        }.collect { emit(it) }
    }
}
