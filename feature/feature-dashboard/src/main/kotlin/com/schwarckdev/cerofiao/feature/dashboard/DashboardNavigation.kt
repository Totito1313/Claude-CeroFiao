package com.schwarckdev.cerofiao.feature.dashboard

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute

@Serializable
object AdminSettingsRoute

fun NavGraphBuilder.dashboardScreen(
    onAddTransaction: () -> Unit,
    onViewAllTransactions: () -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    composable<DashboardRoute> {
        DashboardScreen(
            onAddTransaction = onAddTransaction,
            onViewAllTransactions = onViewAllTransactions,
            onTransactionClick = onTransactionClick,
        )
    }
}

fun NavGraphBuilder.adminSettingsScreen(
    onBack: () -> Unit,
) {
    composable<AdminSettingsRoute> {
        AdminSettingsScreen(
            onNavigateBack = onBack,
        )
    }
}

fun NavController.navigateToAdminSettings() {
    navigate(AdminSettingsRoute)
}
