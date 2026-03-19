package com.schwarckdev.cerofiao.feature.goals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.SavingsGoalRepository
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditGoalUiState(
    val name: String = "",
    val amountText: String = "",
    val currencyCode: String = "USD",
    val deadline: Long? = null,
    val iconName: String = "Target",
    val colorHex: String = "#FF5722",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
)

@HiltViewModel
class AddEditGoalViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val savingsGoalRepository: SavingsGoalRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<AddEditGoalRoute>()
    private val editGoalId: String? = route.goalId

    private val _uiState = MutableStateFlow(AddEditGoalUiState(isEditMode = editGoalId != null))
    val uiState: StateFlow<AddEditGoalUiState> = _uiState

    init {
        if (editGoalId != null) {
            loadGoal(editGoalId)
        }
    }

    private fun loadGoal(id: String) {
        viewModelScope.launch {
            savingsGoalRepository.getGoalById(id).collect { goal ->
                if (goal != null) {
                    _uiState.update {
                        it.copy(
                            name = goal.name,
                            amountText = goal.targetAmount.toString(),
                            currencyCode = goal.currencyCode,
                            deadline = goal.deadline,
                            iconName = goal.iconName ?: "Target",
                            colorHex = goal.colorHex ?: "#FF5722",
                        )
                    }
                }
            }
        }
    }

    fun setName(name: String) { _uiState.update { it.copy(name = name) } }
    fun setAmount(amount: String) { _uiState.update { it.copy(amountText = amount) } }
    fun setCurrency(code: String) { _uiState.update { it.copy(currencyCode = code) } }
    fun setDeadline(date: Long?) { _uiState.update { it.copy(deadline = date) } }
    fun setIcon(icon: String) { _uiState.update { it.copy(iconName = icon) } }
    fun setColor(color: String) { _uiState.update { it.copy(colorHex = color) } }

    fun save() {
        val state = _uiState.value
        val amount = state.amountText.toDoubleOrNull() ?: return
        if (state.name.isBlank() || amount <= 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val now = DateUtils.now()

            if (editGoalId != null) {
                // Not perfectly atomic but fine for this scope
                savingsGoalRepository.getGoalById(editGoalId).collect { existing ->
                    if (existing != null) {
                        savingsGoalRepository.updateGoal(
                            existing.copy(
                                name = state.name.trim(),
                                targetAmount = amount,
                                currencyCode = state.currencyCode,
                                deadline = state.deadline,
                                iconName = state.iconName,
                                colorHex = state.colorHex,
                            )
                        )
                    }
                    _uiState.update { state.copy(isSaving = false, isSaved = true) }
                }
            } else {
                savingsGoalRepository.insertGoal(
                    SavingsGoal(
                        id = UuidGenerator.generate(),
                        name = state.name.trim(),
                        targetAmount = amount,
                        currencyCode = state.currencyCode,
                        currentAmountInUsd = 0.0,
                        iconName = state.iconName,
                        colorHex = state.colorHex,
                        deadline = state.deadline,
                        createdAt = now,
                        updatedAt = now,
                        isCompleted = false,
                        completedAt = null,
                    )
                )
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            }
        }
    }
}
