package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class CeroFiaoWidgetData(
    val totalBalanceUsd: Double,
    val bcvRate: Double,
    val timestampMs: Long
)

class WidgetDataRepository @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val resolveExchangeRateUseCase: ResolveExchangeRateUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend fun getWidgetData(): CeroFiaoWidgetData {
        val prefs = userPreferencesRepository.userPreferences.first()
        val accounts = getAccountsUseCase().first()
        
        var totalBalanceUsd = 0.0
        for (account in accounts) {
            val rateResult = resolveExchangeRateUseCase.toUsd(account.currencyCode, prefs.preferredRateSource)
            val usdValue = com.schwarckdev.cerofiao.core.common.MoneyCalculator.toUsd(
                amount = account.balance,
                currencyCode = account.currencyCode,
                rateToUsd = rateResult.rate
            )
            totalBalanceUsd += usdValue
        }
        
        val bcvRateResult = resolveExchangeRateUseCase.fromUsd("VES", ExchangeRateSource.BCV)
        
        return CeroFiaoWidgetData(
            totalBalanceUsd = totalBalanceUsd,
            bcvRate = bcvRateResult.rate,
            timestampMs = System.currentTimeMillis()
        )
    }
}
