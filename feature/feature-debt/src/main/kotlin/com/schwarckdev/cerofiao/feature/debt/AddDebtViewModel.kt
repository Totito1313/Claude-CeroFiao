package com.schwarckdev.cerofiao.feature.debt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.DebtRepository
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddDebtUiState(
    val debtType: DebtType = DebtType.THEY_OWE,
    val personName: String = "",
    val amount: String = "",
    val currencyCode: String = "USD",
    val note: String = "",
    val dueDate: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
)

@HiltViewModel
class AddDebtViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val debtRepository: DebtRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<AddDebtRoute>()
    private val editDebtId: String? = route.debtId
    private var originalDebt: Debt? = null

    private val formState = MutableStateFlow(FormState())

    val uiState: StateFlow<AddDebtUiState> = formState.map { form ->
        AddDebtUiState(
            debtType = form.debtType,
            personName = form.personName,
            amount = form.amount,
            currencyCode = form.currencyCode,
            note = form.note,
            dueDate = form.dueDate,
            isSaving = form.isSaving,
            isSaved = form.isSaved,
            isEditMode = editDebtId != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddDebtUiState(),
    )

    init {
        if (editDebtId != null) {
            loadDebt(editDebtId)
        }
    }

    private fun loadDebt(id: String) {
        viewModelScope.launch {
            val debt = debtRepository.getDebtById(id).first() ?: return@launch
            originalDebt = debt
            formState.update {
                val amountStr = if (debt.originalAmount == debt.originalAmount.toLong().toDouble()) {
                    debt.originalAmount.toLong().toString()
                } else {
                    debt.originalAmount.toString()
                }
                it.copy(
                    debtType = debt.type,
                    personName = debt.personName,
                    amount = amountStr,
                    currencyCode = debt.currencyCode,
                    note = debt.note ?: "",
                    dueDate = debt.dueDate?.let { ts -> DateUtils.formatDisplayDate(ts) } ?: "",
                )
            }
        }
    }

    fun setDebtType(type: DebtType) {
        formState.update { it.copy(debtType = type) }
    }

    fun setPersonName(name: String) {
        formState.update { it.copy(personName = name) }
    }

    fun setAmount(amount: String) {
        formState.update { it.copy(amount = amount) }
    }

    fun setCurrencyCode(code: String) {
        formState.update { it.copy(currencyCode = code) }
    }

    fun setNote(note: String) {
        formState.update { it.copy(note = note) }
    }

    fun setDueDate(date: String) {
        formState.update { it.copy(dueDate = date) }
    }

    fun save() {
        val current = formState.value
        if (current.personName.isBlank()) return
        val amount = current.amount.toDoubleOrNull() ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            formState.update { it.copy(isSaving = true) }
            try {
                val now = DateUtils.now()
                val dueDate = parseDueDate(current.dueDate)

                if (editDebtId != null && originalDebt != null) {
                    val updated = originalDebt!!.copy(
                        personName = current.personName,
                        type = current.debtType,
                        originalAmount = amount,
                        remainingAmount = amount - (originalDebt!!.originalAmount - originalDebt!!.remainingAmount),
                        currencyCode = current.currencyCode,
                        note = current.note.ifBlank { null },
                        dueDate = dueDate,
                        updatedAt = now,
                    )
                    debtRepository.updateDebt(updated)
                } else {
                    val debt = Debt(
                        id = UuidGenerator.generate(),
                        personName = current.personName,
                        type = current.debtType,
                        originalAmount = amount,
                        remainingAmount = amount,
                        currencyCode = current.currencyCode,
                        note = current.note.ifBlank { null },
                        dueDate = dueDate,
                        createdAt = now,
                        updatedAt = now,
                    )
                    debtRepository.insertDebt(debt)
                }
                formState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (_: Exception) {
                formState.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun parseDueDate(dateStr: String): Long? {
        if (dateStr.isBlank()) return null
        return DateUtils.parseDdMmYyyy(dateStr)
    }

    private data class FormState(
        val debtType: DebtType = DebtType.THEY_OWE,
        val personName: String = "",
        val amount: String = "",
        val currencyCode: String = "USD",
        val note: String = "",
        val dueDate: String = "",
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
    )
}
