package com.schwarckdev.cerofiao.feature.goals

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object GoalsRoute

@Serializable
data class AddEditGoalRoute(val goalId: String? = null)

@Serializable
data class GoalDetailRoute(val goalId: String)

fun NavController.navigateToGoals(navOptions: NavOptions? = null) {
    navigate(route = GoalsRoute, navOptions)
}

fun NavController.navigateToAddEditGoal(goalId: String? = null) {
    navigate(route = AddEditGoalRoute(goalId))
}

fun NavController.navigateToGoalDetail(goalId: String) {
    navigate(route = GoalDetailRoute(goalId))
}

fun NavGraphBuilder.goalsScreen(
    onAddGoal: () -> Unit,
    onGoalClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    composable<GoalsRoute> {
        // GoalsListScreen(onAddGoal = onAddGoal, onGoalClick = onGoalClick, onBack = onBack)
    }
}

fun NavGraphBuilder.addEditGoalScreen(
    onBack: () -> Unit,
) {
    composable<AddEditGoalRoute> {
        // AddEditGoalScreen(onBack = { onBack() }, onSaved = { onBack() })
    }
}

fun NavGraphBuilder.goalDetailScreen(
    onBack: () -> Unit,
    onEditGoal: (String) -> Unit,
) {
    composable<GoalDetailRoute> {
        // GoalDetailScreen(onBack = onBack, onEditClick = onEditGoal)
    }
}
