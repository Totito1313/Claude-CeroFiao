package com.schwarckdev.cerofiao.feature.goals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.domain.repository.SavingsGoalRepository
import com.schwarckdev.cerofiao.core.model.SavingsContribution
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalDetailUiState(
    val goal: SavingsGoal? = null,
    val contributions: List<SavingsContribution> = emptyList(),
)

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val savingsGoalRepository: SavingsGoalRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<GoalDetailRoute>()
    val goalId: String = route.goalId

    val uiState: StateFlow<GoalDetailUiState> = combine(
        savingsGoalRepository.getGoalById(goalId),
        savingsGoalRepository.getContributionsForGoal(goalId)
    ) { goal, contributions ->
        GoalDetailUiState(goal = goal, contributions = contributions)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GoalDetailUiState()
    )

    fun deleteGoal() {
        viewModelScope.launch {
            savingsGoalRepository.deleteGoal(goalId)
        }
    }
}
