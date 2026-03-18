package com.schwarckdev.cerofiao.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

@Serializable
object CsvExportRoute

fun NavGraphBuilder.settingsScreen(
    onNavigateToCategories: () -> Unit = {},
    onNavigateToExchangeRates: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToDebts: () -> Unit = {},
    onNavigateToCsvExport: () -> Unit = {},
) {
    composable<SettingsRoute> {
        SettingsScreen(
            onNavigateToCategories = onNavigateToCategories,
            onNavigateToExchangeRates = onNavigateToExchangeRates,
            onNavigateToBudgets = onNavigateToBudgets,
            onNavigateToDebts = onNavigateToDebts,
            onNavigateToCsvExport = onNavigateToCsvExport,
        )
    }
}

fun NavGraphBuilder.csvExportScreen(
    onBack: () -> Unit,
) {
    composable<CsvExportRoute> {
        CsvExportScreen(onBack = onBack)
    }
}

fun NavController.navigateToSettings() {
    navigate(SettingsRoute)
}

fun NavController.navigateToCsvExport() {
    navigate(CsvExportRoute)
}
