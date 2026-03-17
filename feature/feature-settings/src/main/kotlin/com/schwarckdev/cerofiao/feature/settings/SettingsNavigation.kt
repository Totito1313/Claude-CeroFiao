package com.schwarckdev.cerofiao.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

fun NavGraphBuilder.settingsScreen(
    onNavigateToCategories: () -> Unit = {},
    onNavigateToExchangeRates: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToDebts: () -> Unit = {},
) {
    composable<SettingsRoute> {
        SettingsScreen(
            onNavigateToCategories = onNavigateToCategories,
            onNavigateToExchangeRates = onNavigateToExchangeRates,
            onNavigateToBudgets = onNavigateToBudgets,
            onNavigateToDebts = onNavigateToDebts,
        )
    }
}

fun NavController.navigateToSettings() {
    navigate(SettingsRoute)
}
