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
    val preferredRateSource: ExchangeRateSource = ExchangeRateSource.PARALLEL,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColor: Boolean = true,
    val hasCompletedOnboarding: Boolean = false,
)
