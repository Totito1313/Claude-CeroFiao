package com.schwarckdev.cerofiao.feature.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.usecase.GetActiveGoalsUseCase
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class GoalsUiState(
    val goals: List<SavingsGoal> = emptyList()
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    getActiveGoalsUseCase: GetActiveGoalsUseCase
) : ViewModel() {

    val uiState: StateFlow<GoalsUiState> = getActiveGoalsUseCase()
        .map { GoalsUiState(goals = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GoalsUiState()
        )
}
