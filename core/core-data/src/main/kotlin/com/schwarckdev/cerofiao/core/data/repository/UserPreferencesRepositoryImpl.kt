package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.datastore.UserPreferencesDataStore
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.ThemeMode
import com.schwarckdev.cerofiao.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
) : UserPreferencesRepository {

    override val userPreferences: Flow<UserPreferences> = dataStore.userPreferences

    override suspend fun setDisplayCurrency(currencyCode: String) {
        dataStore.setDisplayCurrency(currencyCode)
    }

    override suspend fun setPreferredRateSource(source: ExchangeRateSource) {
        dataStore.setPreferredRateSource(source)
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.setThemeMode(mode)
    }

    override suspend fun setUseDynamicColor(enabled: Boolean) {
        dataStore.setUseDynamicColor(enabled)
    }

    override suspend fun setOnboardingCompleted() {
        dataStore.setOnboardingCompleted()
    }

    override suspend fun setConverterCurrencies(from: String, to: String) {
        dataStore.setConverterCurrencies(from, to)
    }

    override fun getConverterFromCurrency(): Flow<String> = dataStore.getConverterFromCurrency()

    override fun getConverterToCurrency(): Flow<String> = dataStore.getConverterToCurrency()
}
