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
    onNavigateToTransfer: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToExchangeRates: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToAddAccount: () -> Unit,
    onNavigateToAccountDetail: (String) -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToAddBudget: () -> Unit,
) {
    composable<DashboardRoute> {
        DashboardScreen(
            onAddTransaction = onAddTransaction,
            onViewAllTransactions = onViewAllTransactions,
            onTransactionClick = onTransactionClick,
            onNavigateToTransfer = onNavigateToTransfer,
            onNavigateToCategories = onNavigateToCategories,
            onNavigateToExchangeRates = onNavigateToExchangeRates,
            onNavigateToAccounts = onNavigateToAccounts,
            onNavigateToAddAccount = onNavigateToAddAccount,
            onNavigateToAccountDetail = onNavigateToAccountDetail,
            onNavigateToBudgets = onNavigateToBudgets,
            onNavigateToAddBudget = onNavigateToAddBudget,
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
