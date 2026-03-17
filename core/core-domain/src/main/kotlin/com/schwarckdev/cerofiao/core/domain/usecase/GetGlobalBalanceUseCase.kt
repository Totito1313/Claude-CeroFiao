package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.AccountBalance
import com.schwarckdev.cerofiao.core.model.CurrencyBalance
import com.schwarckdev.cerofiao.core.model.GlobalBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGlobalBalanceUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val resolveExchangeRate: ResolveExchangeRateUseCase,
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
                    // Convert via resolver: account currency → USD → display currency
                    val toUsd = resolveExchangeRate.toUsd(
                        account.currencyCode, prefs.preferredRateSource,
                    ).rate
                    val fromUsd = resolveExchangeRate.fromUsd(
                        displayCurrency, prefs.preferredRateSource,
                    ).rate
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
