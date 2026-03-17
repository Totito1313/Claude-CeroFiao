package com.schwarckdev.cerofiao.feature.debt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.DebtRepository
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtPayment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DebtDetailUiState(
    val debt: Debt? = null,
    val payments: List<DebtPayment> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class DebtDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val debtRepository: DebtRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<DebtDetailRoute>()
    private val debtId = route.debtId

    val uiState: StateFlow<DebtDetailUiState> = combine(
        debtRepository.getDebtById(debtId),
        debtRepository.getPaymentsForDebt(debtId),
    ) { debt, payments ->
        DebtDetailUiState(
            debt = debt,
            payments = payments,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DebtDetailUiState(),
    )

    fun registerPayment(amountStr: String, note: String) {
        val amount = amountStr.toDoubleOrNull() ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            val debt = debtRepository.getDebtById(debtId).first() ?: return@launch
            val now = DateUtils.now()

            val payment = DebtPayment(
                id = UuidGenerator.generate(),
                debtId = debtId,
                amount = amount,
                currencyCode = debt.currencyCode,
                paidAt = now,
                note = note.ifBlank { null },
            )
            debtRepository.insertPayment(payment)

            val newRemaining = (debt.remainingAmount - amount).coerceAtLeast(0.0)
            val isSettled = newRemaining <= 0.0

            val updatedDebt = debt.copy(
                remainingAmount = newRemaining,
                isSettled = isSettled,
                settledAt = if (isSettled) now else debt.settledAt,
                updatedAt = now,
            )
            debtRepository.updateDebt(updatedDebt)
        }
    }

    fun markAsSettled() {
        viewModelScope.launch {
            val debt = debtRepository.getDebtById(debtId).first() ?: return@launch
            val now = DateUtils.now()
            val updatedDebt = debt.copy(
                remainingAmount = 0.0,
                isSettled = true,
                settledAt = now,
                updatedAt = now,
            )
            debtRepository.updateDebt(updatedDebt)
        }
    }

    fun deleteDebt() {
        viewModelScope.launch {
            debtRepository.deleteDebt(debtId)
        }
    }
}
