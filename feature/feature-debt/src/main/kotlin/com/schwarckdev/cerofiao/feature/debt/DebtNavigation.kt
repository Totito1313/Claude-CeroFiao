package com.schwarckdev.cerofiao.feature.debt

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object DebtListRoute

@Serializable
data class AddDebtRoute(val debtId: String? = null)

@Serializable
data class DebtDetailRoute(val debtId: String)

fun NavGraphBuilder.debtListScreen(
    onBack: () -> Unit,
    onAddDebt: () -> Unit,
    onDebtClick: (String) -> Unit,
) {
    composable<DebtListRoute> {
        DebtListScreen(
            onBack = onBack,
            onAddDebt = onAddDebt,
            onDebtClick = onDebtClick,
        )
    }
}

fun NavGraphBuilder.addDebtScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
) {
    composable<AddDebtRoute> {
        AddDebtScreen(
            onBack = onBack,
            onSaved = onSaved,
        )
    }
}

fun NavGraphBuilder.debtDetailScreen(
    onBack: () -> Unit,
) {
    composable<DebtDetailRoute> {
        DebtDetailScreen(
            onBack = onBack,
        )
    }
}

fun NavController.navigateToDebtList() {
    navigate(DebtListRoute)
}

fun NavController.navigateToAddDebt(debtId: String? = null) {
    navigate(AddDebtRoute(debtId))
}

fun NavController.navigateToDebtDetail(debtId: String) {
    navigate(DebtDetailRoute(debtId))
}
