package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
}

@Serializable
data class UserPreferences(
    val displayCurrencyCode: String = "USD",
    val preferredRateSource: ExchangeRateSource = ExchangeRateSource.USDT,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColor: Boolean = false,
    val hasCompletedOnboarding: Boolean = false,
)
