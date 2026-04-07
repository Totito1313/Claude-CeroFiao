package com.schwarckdev.cerofiao.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.ThemeMode
import com.schwarckdev.cerofiao.core.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val DISPLAY_CURRENCY = stringPreferencesKey("display_currency")
        val PREFERRED_RATE_SOURCE = stringPreferencesKey("preferred_rate_source")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val CONVERTER_FROM_CURRENCY = stringPreferencesKey("converter_from_currency")
        val CONVERTER_TO_CURRENCY = stringPreferencesKey("converter_to_currency")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            displayCurrencyCode = prefs[Keys.DISPLAY_CURRENCY] ?: "USD",
            preferredRateSource = prefs[Keys.PREFERRED_RATE_SOURCE]?.let {
                try { ExchangeRateSource.valueOf(it) } catch (_: Exception) { ExchangeRateSource.USDT }
            } ?: ExchangeRateSource.USDT,
            themeMode = prefs[Keys.THEME_MODE]?.let {
                try { ThemeMode.valueOf(it) } catch (_: Exception) { ThemeMode.SYSTEM }
            } ?: ThemeMode.SYSTEM,
            useDynamicColor = prefs[Keys.USE_DYNAMIC_COLOR] ?: false,
            hasCompletedOnboarding = prefs[Keys.HAS_COMPLETED_ONBOARDING] ?: false,
        )
    }

    suspend fun setDisplayCurrency(currencyCode: String) {
        context.dataStore.edit { it[Keys.DISPLAY_CURRENCY] = currencyCode }
    }

    suspend fun setPreferredRateSource(source: ExchangeRateSource) {
        context.dataStore.edit { it[Keys.PREFERRED_RATE_SOURCE] = source.name }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setUseDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[Keys.USE_DYNAMIC_COLOR] = enabled }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { it[Keys.HAS_COMPLETED_ONBOARDING] = true }
    }

    suspend fun setConverterCurrencies(from: String, to: String) {
        context.dataStore.edit {
            it[Keys.CONVERTER_FROM_CURRENCY] = from
            it[Keys.CONVERTER_TO_CURRENCY] = to
        }
    }

    fun getConverterFromCurrency(): kotlinx.coroutines.flow.Flow<String> =
        context.dataStore.data.map { it[Keys.CONVERTER_FROM_CURRENCY] ?: "USD" }

    fun getConverterToCurrency(): kotlinx.coroutines.flow.Flow<String> =
        context.dataStore.data.map { it[Keys.CONVERTER_TO_CURRENCY] ?: "VES" }
}
