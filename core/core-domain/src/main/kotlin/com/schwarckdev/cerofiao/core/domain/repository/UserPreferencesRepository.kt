package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.ThemeMode
import com.schwarckdev.cerofiao.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun setDisplayCurrency(currencyCode: String)
    suspend fun setPreferredRateSource(source: ExchangeRateSource)
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setUseDynamicColor(enabled: Boolean)
    suspend fun setOnboardingCompleted()
    suspend fun setConverterCurrencies(from: String, to: String)
    fun getConverterFromCurrency(): Flow<String>
    fun getConverterToCurrency(): Flow<String>
}
