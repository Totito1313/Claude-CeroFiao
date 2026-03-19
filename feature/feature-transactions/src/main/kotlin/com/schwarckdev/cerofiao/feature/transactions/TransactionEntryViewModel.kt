package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetActiveGoalsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RecordTransactionUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.SuggestCategoryByTitleUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryNode
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val amountText: String = "",
    val amount: Double = 0.0,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val accounts: List<Account> = emptyList(),
    val categories: List<CategoryNode> = emptyList(),
    val goals: List<SavingsGoal> = emptyList(),
    val selectedAccountId: String? = null,
    val selectedCategoryId: String? = null,
    val selectedGoalId: String? = null,
    val selectedCurrencyCode: String? = null,
    val note: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
    val suggestedCategoryId: String? = null,
    val currencyEquivalents: Map<String, Double> = emptyMap(),
)

@HiltViewModel
class TransactionEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAccountsUseCase: GetAccountsUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    getActiveGoalsUseCase: GetActiveGoalsUseCase,
    private val recordTransactionUseCase: RecordTransactionUseCase,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val resolveExchangeRate: ResolveExchangeRateUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val suggestCategoryByTitle: SuggestCategoryByTitleUseCase,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<TransactionEntryRoute>()
    private val editTransactionId: String? = route.transactionId
    private var originalTransaction: Transaction? = null
    private var suggestionJob: Job? = null

    private val formState = MutableStateFlow(FormState())

    val uiState: StateFlow<TransactionEntryUiState> = combine(
        getAccountsUseCase(),
        getCategoriesUseCase(),
        getActiveGoalsUseCase(),
        formState,
    ) { accounts, categories, goals, form ->
        val filteredCategories = categories.filter { cat ->
            when (form.transactionType) {
                TransactionType.EXPENSE -> cat.type.name == "EXPENSE"
                TransactionType.INCOME -> cat.type.name == "INCOME"
                TransactionType.TRANSFER -> false
            }
        }

        val rootCategories = filteredCategories.filter { it.parentId == null }.sortedBy { it.sortOrder }
        val categoryNodes = rootCategories.map { root ->
            val subs = filteredCategories.filter { it.parentId == root.id }.sortedBy { it.sortOrder }
            CategoryNode(root, subs)
        }

        val resolvedAccountId = form.selectedAccountId ?: accounts.firstOrNull()?.id
        val resolvedCurrency = form.selectedCurrencyCode
            ?: accounts.find { it.id == resolvedAccountId }?.currencyCode

        TransactionEntryUiState(
            amountText = form.amountText,
            amount = form.amountText.toDoubleOrNull() ?: 0.0,
            transactionType = form.transactionType,
            accounts = accounts,
            categories = categoryNodes,
            goals = goals,
            selectedAccountId = resolvedAccountId,
            selectedCategoryId = form.selectedCategoryId,
            selectedGoalId = form.selectedGoalId,
            selectedCurrencyCode = resolvedCurrency,
            note = form.note,
            isSaving = form.isSaving,
            isSaved = form.isSaved,
            isEditMode = editTransactionId != null,
            suggestedCategoryId = form.suggestedCategoryId,
            currencyEquivalents = form.currencyEquivalents,
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

    private var equivalentsJob: Job? = null

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
                    amountText = amountStr,
                    transactionType = transaction.type,
                    selectedAccountId = transaction.accountId,
                    selectedCategoryId = transaction.categoryId,
                    selectedCurrencyCode = transaction.currencyCode,
                    note = transaction.note ?: "",
                )
            }
        }
    }

    fun setAmount(text: String) {
        formState.update { it.copy(amountText = text) }
        updateCurrencyEquivalents()
    }

    fun selectCurrency(code: String) {
        formState.update { it.copy(selectedCurrencyCode = code) }
        updateCurrencyEquivalents()
    }

    private fun updateCurrencyEquivalents() {
        equivalentsJob?.cancel()
        equivalentsJob = viewModelScope.launch {
            delay(400)
            val amount = formState.value.amountText.toDoubleOrNull() ?: return@launch
            if (amount <= 0) {
                formState.update { it.copy(currencyEquivalents = emptyMap()) }
                return@launch
            }
            val selectedCurrency = uiState.value.selectedCurrencyCode ?: return@launch
            val allCurrencies = listOf("USD", "VES", "USDT", "EUR")
            val equivalents = mutableMapOf<String, Double>()
            val prefs = userPreferencesRepository.userPreferences.first()

            for (targetCurrency in allCurrencies) {
                if (targetCurrency == selectedCurrency) continue
                try {
                    // Convert to USD first, then to target
                    val toUsdResult = resolveExchangeRate.toUsd(selectedCurrency, prefs.preferredRateSource)
                    val amountUsd = MoneyCalculator.toUsd(amount, selectedCurrency, toUsdResult.rate)
                    if (targetCurrency == "USD") {
                        equivalents[targetCurrency] = amountUsd
                    } else {
                        val fromUsdResult = resolveExchangeRate.toUsd(targetCurrency, prefs.preferredRateSource)
                        if (fromUsdResult.rate > 0) {
                            equivalents[targetCurrency] = MoneyCalculator.fromUsd(amountUsd, targetCurrency, fromUsdResult.rate)
                        }
                    }
                } catch (_: Exception) {
                    // Skip currencies we can't convert
                }
            }

            formState.update { it.copy(currencyEquivalents = equivalents) }
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

    fun selectGoal(goalId: String?) {
        formState.update { it.copy(selectedGoalId = if (it.selectedGoalId == goalId) null else goalId) }
    }

    fun setNote(note: String) {
        formState.update { it.copy(note = note) }
        suggestionJob?.cancel()
        if (note.isNotBlank() && formState.value.selectedCategoryId == null) {
            suggestionJob = viewModelScope.launch {
                delay(500)
                val categoryId = suggestCategoryByTitle.suggest(note)
                if (categoryId != null) {
                    formState.update { it.copy(suggestedCategoryId = categoryId) }
                }
            }
        } else {
            formState.update { it.copy(suggestedCategoryId = null) }
        }
    }

    fun applySuggestedCategory() {
        val suggested = formState.value.suggestedCategoryId ?: return
        formState.update { it.copy(selectedCategoryId = suggested, suggestedCategoryId = null) }
    }

    fun save() {
        val current = formState.value
        val accountId = uiState.value.selectedAccountId ?: return
        val amount = current.amountText.toDoubleOrNull() ?: return
        if (amount <= 0) return

        val account = uiState.value.accounts.find { it.id == accountId } ?: return
        val currencyCode = uiState.value.selectedCurrencyCode ?: account.currencyCode

        viewModelScope.launch {
            formState.update { it.copy(isSaving = true) }
            try {
                if (editTransactionId != null && originalTransaction != null) {
                    updateExistingTransaction(current, account, currencyCode)
                } else {
                    recordTransactionUseCase(
                        amount = amount,
                        currencyCode = currencyCode,
                        accountId = accountId,
                        categoryId = current.selectedCategoryId,
                        type = current.transactionType,
                        note = current.note.ifBlank { null },
                        goalId = current.selectedGoalId,
                    )
                }
                // Learn title→category association for future suggestions
                val categoryId = current.selectedCategoryId
                val note = current.note.trim()
                if (categoryId != null && note.isNotBlank()) {
                    suggestCategoryByTitle.saveAssociation(note, categoryId)
                }
                formState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (_: Exception) {
                formState.update { it.copy(isSaving = false) }
            }
        }
    }

    private suspend fun updateExistingTransaction(current: FormState, account: Account, currencyCode: String) {
        val original = originalTransaction ?: return
        val now = DateUtils.now()
        val prefs = userPreferencesRepository.userPreferences.first()
        val amount = current.amountText.toDoubleOrNull() ?: return

        // Recalculate exchange rate via resolver (handles USDT/EURI/cross-rates)
        val result = resolveExchangeRate.toUsd(currencyCode, prefs.preferredRateSource)
        val rateToUsd = result.rate
        val rateSource = result.source

        val amountInUsd = MoneyCalculator.toUsd(amount, currencyCode, rateToUsd)

        val updated = original.copy(
            type = current.transactionType,
            amount = amount,
            currencyCode = currencyCode,
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
                    TransactionType.INCOME -> reverted + amount
                    TransactionType.EXPENSE -> reverted - amount
                    TransactionType.TRANSFER -> reverted - amount
                }
            } else {
                when (current.transactionType) {
                    TransactionType.INCOME -> targetAccount.balance + amount
                    TransactionType.EXPENSE -> targetAccount.balance - amount
                    TransactionType.TRANSFER -> targetAccount.balance - amount
                }
            }
            accountRepository.updateBalance(account.id, targetBalance)
        }
    }

    private data class FormState(
        val amountText: String = "",
        val transactionType: TransactionType = TransactionType.EXPENSE,
        val selectedAccountId: String? = null,
        val selectedCategoryId: String? = null,
        val selectedGoalId: String? = null,
        val selectedCurrencyCode: String? = null,
        val suggestedCategoryId: String? = null,
        val note: String = "",
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
        val currencyEquivalents: Map<String, Double> = emptyMap(),
    )
}
