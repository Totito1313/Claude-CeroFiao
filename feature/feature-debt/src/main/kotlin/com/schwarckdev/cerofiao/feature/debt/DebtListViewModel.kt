package com.schwarckdev.cerofiao.feature.debt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.repository.DebtRepository
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DebtListUiState(
    val debts: List<Debt> = emptyList(),
    val selectedFilter: DebtFilter = DebtFilter.ALL,
    val totalTheyOwe: Double = 0.0,
    val totalIOwe: Double = 0.0,
    val isLoading: Boolean = true,
)

enum class DebtFilter {
    ALL,
    THEY_OWE,
    I_OWE,
}

@HiltViewModel
class DebtListViewModel @Inject constructor(
    private val debtRepository: DebtRepository,
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(DebtFilter.ALL)

    val uiState: StateFlow<DebtListUiState> = combine(
        debtRepository.getAllDebts(),
        selectedFilter,
    ) { allDebts, filter ->
        val activeDebts = allDebts.filter { !it.isSettled }

        val totalTheyOwe = activeDebts
            .filter { it.type == DebtType.THEY_OWE }
            .sumOf { it.remainingAmount }

        val totalIOwe = activeDebts
            .filter { it.type == DebtType.I_OWE }
            .sumOf { it.remainingAmount }

        val filteredDebts = when (filter) {
            DebtFilter.ALL -> allDebts
            DebtFilter.THEY_OWE -> allDebts.filter { it.type == DebtType.THEY_OWE }
            DebtFilter.I_OWE -> allDebts.filter { it.type == DebtType.I_OWE }
        }

        DebtListUiState(
            debts = filteredDebts,
            selectedFilter = filter,
            totalTheyOwe = totalTheyOwe,
            totalIOwe = totalIOwe,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DebtListUiState(),
    )

    fun setFilter(filter: DebtFilter) {
        selectedFilter.update { filter }
    }

    fun deleteDebt(debtId: String) {
        viewModelScope.launch {
            debtRepository.deleteDebt(debtId)
        }
    }
}
