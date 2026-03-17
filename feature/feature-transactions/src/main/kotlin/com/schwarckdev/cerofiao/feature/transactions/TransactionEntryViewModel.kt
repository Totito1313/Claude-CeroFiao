package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.ExpressionEvaluator
import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RecordTransactionUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    val isEditMode: Boolean = false,
)

@HiltViewModel
class TransactionEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAccountsUseCase: GetAccountsUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val recordTransactionUseCase: RecordTransactionUseCase,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<TransactionEntryRoute>()
    private val editTransactionId: String? = route.transactionId
    private var originalTransaction: Transaction? = null

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
            isEditMode = editTransactionId != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionEntryUiState(),
    )

    init {
        if (editTransactionId != null) {
            loadTransaction(editTransactionId)
        }
    }

    private fun loadTransaction(id: String) {
        viewModelScope.launch {
            val transaction = transactionRepository.getTransactionByIdOnce(id) ?: return@launch
            originalTransaction = transaction
            formState.update {
                val amountStr = if (transaction.amount == transaction.amount.toLong().toDouble()) {
                    transaction.amount.toLong().toString()
                } else {
                    transaction.amount.toString()
                }
                it.copy(
                    expression = amountStr,
                    evaluatedAmount = transaction.amount,
                    transactionType = transaction.type,
                    selectedAccountId = transaction.accountId,
                    selectedCategoryId = transaction.categoryId,
                    note = transaction.note ?: "",
                )
            }
        }
    }

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
                if (editTransactionId != null && originalTransaction != null) {
                    updateExistingTransaction(current, account)
                } else {
                    recordTransactionUseCase(
                        amount = amount,
                        currencyCode = account.currencyCode,
                        accountId = accountId,
                        categoryId = current.selectedCategoryId,
                        type = current.transactionType,
                        note = current.note.ifBlank { null },
                    )
                }
                formState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (_: Exception) {
                formState.update { it.copy(isSaving = false) }
            }
        }
    }

    private suspend fun updateExistingTransaction(current: FormState, account: Account) {
        val original = originalTransaction ?: return
        val now = DateUtils.now()
        val prefs = userPreferencesRepository.userPreferences.first()

        // Recalculate exchange rate
        val rateToUsd: Double
        val rateSource: ExchangeRateSource

        if (account.currencyCode == "USD") {
            rateToUsd = 1.0
            rateSource = prefs.preferredRateSource
        } else {
            val rate = exchangeRateRepository.getLatestRateBySource(
                from = account.currencyCode,
                to = "USD",
                source = prefs.preferredRateSource,
            ) ?: exchangeRateRepository.getLatestRate(account.currencyCode, "USD")
            rateToUsd = rate?.rate ?: 1.0
            rateSource = rate?.source ?: ExchangeRateSource.MANUAL
        }

        val amountInUsd = MoneyCalculator.toUsd(current.evaluatedAmount, account.currencyCode, rateToUsd)

        val updated = original.copy(
            type = current.transactionType,
            amount = current.evaluatedAmount,
            currencyCode = account.currencyCode,
            accountId = account.id,
            categoryId = current.selectedCategoryId,
            note = current.note.ifBlank { null },
            updatedAt = now,
            exchangeRateToUsd = rateToUsd,
            exchangeRateSource = rateSource,
            amountInUsd = amountInUsd,
        )

        transactionRepository.updateTransaction(updated)

        // Adjust account balances if amount/account/type changed
        val oldAccount = accountRepository.getAccountByIdOnce(original.accountId)
        val newAccount = accountRepository.getAccountByIdOnce(account.id)

        // Reverse original transaction effect
        if (oldAccount != null) {
            val revertedBalance = when (original.type) {
                TransactionType.INCOME -> oldAccount.balance - original.amount
                TransactionType.EXPENSE -> oldAccount.balance + original.amount
                TransactionType.TRANSFER -> oldAccount.balance + original.amount
            }
            accountRepository.updateBalance(original.accountId, revertedBalance)
        }

        // Apply new transaction effect
        val targetAccount = if (account.id == original.accountId) {
            // Re-fetch after revert
            accountRepository.getAccountByIdOnce(account.id)
        } else {
            newAccount
        }
        if (targetAccount != null) {
            val targetBalance = if (account.id == original.accountId) {
                // Balance was just reverted, use reverted balance
                val reverted = when (original.type) {
                    TransactionType.INCOME -> targetAccount.balance - original.amount
                    TransactionType.EXPENSE -> targetAccount.balance + original.amount
                    TransactionType.TRANSFER -> targetAccount.balance + original.amount
                }
                when (current.transactionType) {
                    TransactionType.INCOME -> reverted + current.evaluatedAmount
                    TransactionType.EXPENSE -> reverted - current.evaluatedAmount
                    TransactionType.TRANSFER -> reverted - current.evaluatedAmount
                }
            } else {
                when (current.transactionType) {
                    TransactionType.INCOME -> targetAccount.balance + current.evaluatedAmount
                    TransactionType.EXPENSE -> targetAccount.balance - current.evaluatedAmount
                    TransactionType.TRANSFER -> targetAccount.balance - current.evaluatedAmount
                }
            }
            accountRepository.updateBalance(account.id, targetBalance)
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
