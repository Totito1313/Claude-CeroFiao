package com.schwarckdev.cerofiao.feature.dashboard

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute

fun NavGraphBuilder.dashboardScreen(
    onAddTransaction: () -> Unit,
    onViewAllTransactions: () -> Unit,
) {
    composable<DashboardRoute> {
        DashboardScreen(
            onAddTransaction = onAddTransaction,
            onViewAllTransactions = onViewAllTransactions,
        )
    }
}
