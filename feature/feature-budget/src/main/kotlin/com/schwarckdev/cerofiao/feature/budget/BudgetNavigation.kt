package com.schwarckdev.cerofiao.feature.budget

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object BudgetListRoute

@Serializable
data class AddBudgetRoute(val budgetId: String? = null)

fun NavGraphBuilder.budgetListScreen(
    onBack: () -> Unit,
    onAddBudget: () -> Unit,
    onEditBudget: (String) -> Unit,
) {
    composable<BudgetListRoute> {
        BudgetListScreen(
            onBack = onBack,
            onAddBudget = onAddBudget,
            onEditBudget = onEditBudget,
        )
    }
}

fun NavGraphBuilder.addBudgetScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
) {
    composable<AddBudgetRoute> {
        AddBudgetScreen(
            onBack = onBack,
            onSaved = onSaved,
        )
    }
}

fun NavController.navigateToBudgetList() {
    navigate(BudgetListRoute)
}

fun NavController.navigateToAddBudget(budgetId: String? = null) {
    navigate(AddBudgetRoute(budgetId))
}

@Serializable
object AnalyticsRoute

@Serializable
object AlcanciaRoute

fun NavGraphBuilder.analyticsScreen(
    onBack: () -> Unit,
) {
    composable<AnalyticsRoute> {
        AnalyticsScreen(onBack = onBack)
    }
}

fun NavGraphBuilder.alcanciaScreen(
    onBack: () -> Unit,
) {
    composable<AlcanciaRoute> {
        AlcanciaScreen(onBack = onBack)
    }
}

fun NavController.navigateToAnalytics() {
    navigate(AnalyticsRoute)
}

fun NavController.navigateToAlcancia() {
    navigate(AlcanciaRoute)
}
