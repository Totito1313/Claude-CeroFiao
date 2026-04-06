package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.AccountBalance
import com.schwarckdev.cerofiao.core.model.CurrencyBalance
import com.schwarckdev.cerofiao.core.model.GlobalBalance
import com.schwarckdev.cerofiao.core.model.MultiCurrencyAmount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGlobalBalanceUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val buildRateTable: BuildRateTableUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<GlobalBalance> = flow {
        combine(
            accountRepository.getAccountsIncludedInTotal(),
            userPreferencesRepository.userPreferences,
        ) { accounts, prefs ->
            val displayCurrency = prefs.displayCurrencyCode
            val rateTable = buildRateTable.build(prefs.preferredRateSource)

            val accountBalances = accounts.map { account ->
                val multi = rateTable.convertToAll(account.balance, account.currencyCode)
                AccountBalance(
                    account = account,
                    balanceInOriginalCurrency = account.balance,
                    balanceInDisplayCurrency = multi.inCurrency(displayCurrency),
                    multiCurrency = multi,
                )
            }

            val totalMulti = accountBalances.fold(MultiCurrencyAmount.ZERO) { acc, ab ->
                acc + ab.multiCurrency
            }

            val breakdownByCurrency = accountBalances
                .groupBy { it.account.currencyCode }
                .map { (currencyCode, balances) ->
                    val groupMulti = balances.fold(MultiCurrencyAmount.ZERO) { acc, ab ->
                        acc + ab.multiCurrency
                    }
                    CurrencyBalance(
                        currencyCode = currencyCode,
                        totalInOriginalCurrency = balances.sumOf { it.balanceInOriginalCurrency },
                        totalInDisplayCurrency = balances.sumOf { it.balanceInDisplayCurrency },
                        multiCurrency = groupMulti,
                    )
                }

            GlobalBalance(
                totalInDisplayCurrency = totalMulti.inCurrency(displayCurrency),
                displayCurrencyCode = displayCurrency,
                breakdownByAccount = accountBalances,
                breakdownByCurrency = breakdownByCurrency,
                total = totalMulti,
            )
        }.collect { emit(it) }
    }
}
