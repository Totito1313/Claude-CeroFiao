package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.ExpressionEvaluator
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RecordTransactionUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Category
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

data class TransactionEntryUiState(
    val expression: String = "0",
    val evaluatedAmount: Double = 0.0,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedAccountId: String? = null,
    val selectedCategoryId: String? = null,
    val note: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
)

@HiltViewModel
class TransactionEntryViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val recordTransactionUseCase: RecordTransactionUseCase,
) : ViewModel() {

    private val formState = MutableStateFlow(FormState())

    val uiState: StateFlow<TransactionEntryUiState> = combine(
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

        TransactionEntryUiState(
            expression = form.expression,
            evaluatedAmount = form.evaluatedAmount,
            transactionType = form.transactionType,
            accounts = accounts,
            categories = filteredCategories,
            selectedAccountId = form.selectedAccountId ?: accounts.firstOrNull()?.id,
            selectedCategoryId = form.selectedCategoryId,
            note = form.note,
            isSaving = form.isSaving,
            isSaved = form.isSaved,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionEntryUiState(),
    )

    fun onNumpadInput(char: String) {
        formState.update { state ->
            val newExpr = if (state.expression == "0" && char.first().isDigit()) {
                char
            } else {
                state.expression + char
            }
            val evaluated = try {
                ExpressionEvaluator.evaluate(newExpr) ?: state.evaluatedAmount
            } catch (_: Exception) {
                state.evaluatedAmount
            }
            state.copy(expression = newExpr, evaluatedAmount = evaluated)
        }
    }

    fun onBackspace() {
        formState.update { state ->
            val newExpr = if (state.expression.length <= 1) "0" else state.expression.dropLast(1)
            val evaluated = try {
                ExpressionEvaluator.evaluate(newExpr) ?: 0.0
            } catch (_: Exception) {
                0.0
            }
            state.copy(expression = newExpr, evaluatedAmount = evaluated)
        }
    }

    fun onClear() {
        formState.update { it.copy(expression = "0", evaluatedAmount = 0.0) }
    }

    fun onEquals() {
        formState.update { state ->
            val result = try {
                ExpressionEvaluator.evaluate(state.expression) ?: state.evaluatedAmount
            } catch (_: Exception) {
                state.evaluatedAmount
            }
            state.copy(expression = result.toString(), evaluatedAmount = result)
        }
    }

    fun setTransactionType(type: TransactionType) {
        formState.update { it.copy(transactionType = type, selectedCategoryId = null) }
    }

    fun selectAccount(accountId: String) {
        formState.update { it.copy(selectedAccountId = accountId) }
    }

    fun selectCategory(categoryId: String) {
        formState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun setNote(note: String) {
        formState.update { it.copy(note = note) }
    }

    fun save() {
        val current = formState.value
        val accountId = uiState.value.selectedAccountId ?: return
        val amount = current.evaluatedAmount
        if (amount <= 0) return

        val account = uiState.value.accounts.find { it.id == accountId } ?: return

        viewModelScope.launch {
            formState.update { it.copy(isSaving = true) }
            try {
                recordTransactionUseCase(
                    amount = amount,
                    currencyCode = account.currencyCode,
                    accountId = accountId,
                    categoryId = current.selectedCategoryId,
                    type = current.transactionType,
                    note = current.note.ifBlank { null },
                )
                formState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (_: Exception) {
                formState.update { it.copy(isSaving = false) }
            }
        }
    }

    private data class FormState(
        val expression: String = "0",
        val evaluatedAmount: Double = 0.0,
        val transactionType: TransactionType = TransactionType.EXPENSE,
        val selectedAccountId: String? = null,
        val selectedCategoryId: String? = null,
        val note: String = "",
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
    )
}
