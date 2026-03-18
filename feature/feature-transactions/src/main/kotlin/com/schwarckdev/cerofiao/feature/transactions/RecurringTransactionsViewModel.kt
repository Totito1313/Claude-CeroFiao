package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.RecurringTransactionRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.RecurrenceType
import com.schwarckdev.cerofiao.core.model.RecurringTransaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecurringListUiState(
    val recurringTransactions: List<RecurringTransaction> = emptyList(),
    val upcomingTransactions: List<RecurringTransaction> = emptyList(),
)

data class RecurringFormUiState(
    val title: String = "",
    val amount: String = "",
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val recurrence: RecurrenceType = RecurrenceType.MONTHLY,
    val periodLength: Int = 1,
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedAccountId: String? = null,
    val selectedCategoryId: String? = null,
    val note: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
)

@HiltViewModel
class RecurringListViewModel @Inject constructor(
    private val recurringTransactionRepository: RecurringTransactionRepository,
) : ViewModel() {

    val uiState: StateFlow<RecurringListUiState> = recurringTransactionRepository.getAllActive()
        .combine(MutableStateFlow(Unit)) { list, _ ->
            val now = DateUtils.now()
            // Next 30 days
            val thirtyDaysLater = now + 30L * 24 * 60 * 60 * 1000
            val upcoming = list.filter { it.isActive && it.nextDueDate <= thirtyDaysLater }
                .sortedBy { it.nextDueDate }
            RecurringListUiState(
                recurringTransactions = list,
                upcomingTransactions = upcoming,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RecurringListUiState())

    fun delete(id: String) {
        viewModelScope.launch { recurringTransactionRepository.delete(id) }
    }

    fun toggleActive(id: String, isActive: Boolean) {
        viewModelScope.launch { recurringTransactionRepository.setActive(id, isActive) }
    }
}

@HiltViewModel
class RecurringFormViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val recurringTransactionRepository: RecurringTransactionRepository,
) : ViewModel() {

    private val formState = MutableStateFlow(RecurringFormUiState())

    val uiState: StateFlow<RecurringFormUiState> = combine(
        getAccountsUseCase(),
        getCategoriesUseCase(),
        formState,
    ) { accounts, categories, form ->
        val filteredCategories = categories.filter { cat ->
            when (form.transactionType) {
                TransactionType.EXPENSE -> cat.type.name == "EXPENSE"
                TransactionType.INCOME -> cat.type.name == "INCOME"
                TransactionType.TRANSFER -> false
            }
        }
        form.copy(
            accounts = accounts,
            categories = filteredCategories,
            selectedAccountId = form.selectedAccountId ?: accounts.firstOrNull()?.id,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RecurringFormUiState())

    fun setTitle(title: String) { formState.update { it.copy(title = title) } }
    fun setAmount(amount: String) { formState.update { it.copy(amount = amount) } }
    fun setTransactionType(type: TransactionType) { formState.update { it.copy(transactionType = type, selectedCategoryId = null) } }
    fun setRecurrence(recurrence: RecurrenceType) { formState.update { it.copy(recurrence = recurrence) } }
    fun selectAccount(id: String) { formState.update { it.copy(selectedAccountId = id) } }
    fun selectCategory(id: String) { formState.update { it.copy(selectedCategoryId = id) } }
    fun setNote(note: String) { formState.update { it.copy(note = note) } }

    fun save() {
        val form = formState.value
        val accountId = uiState.value.selectedAccountId ?: return
        val amount = form.amount.toDoubleOrNull() ?: return
        if (amount <= 0 || form.title.isBlank()) return

        viewModelScope.launch {
            formState.update { it.copy(isSaving = true) }
            val now = DateUtils.now()
            val recurring = RecurringTransaction(
                id = UuidGenerator.generate(),
                title = form.title.trim(),
                amount = amount,
                currencyCode = uiState.value.accounts.find { it.id == accountId }?.currencyCode ?: "USD",
                categoryId = form.selectedCategoryId,
                accountId = accountId,
                type = form.transactionType,
                recurrence = form.recurrence,
                periodLength = form.periodLength,
                startDate = now,
                nextDueDate = now,
                note = form.note.ifBlank { null },
                createdAt = now,
                updatedAt = now,
            )
            recurringTransactionRepository.insert(recurring)
            formState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
