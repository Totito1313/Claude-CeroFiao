package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.DeleteTransactionUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetTransactionsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.AccountType
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

data class TransactionWithCategory(
    val transaction: Transaction,
    val categoryName: String,
    val categoryIconName: String,
    val categoryColorHex: String,
    val accountName: String,
    val accountType: AccountType = AccountType.CASH,
    val accountPlatform: AccountPlatform = AccountPlatform.NONE,
)

data class TransactionDateGroup(
    val dateMillis: Long,
    val transactions: List<TransactionWithCategory>,
    val dayNetUsd: Double,
)

enum class SortOrder {
    DATE_DESC,
    DATE_ASC,
    AMOUNT_DESC,
    AMOUNT_ASC,
}

data class TransactionListUiState(
    val groupedTransactions: List<TransactionDateGroup> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val selectedTypeFilter: TransactionType? = null,
    val selectedAccountId: String? = null,
    val selectedCurrencyFilter: String? = null,
    val selectedCategoryId: String? = null,
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.DATE_DESC,
    val totalIncomeUsd: Double = 0.0,
    val totalExpenseUsd: Double = 0.0,
    val monthOverMonthPercent: Double? = null,
    val categories: List<Category> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    // Display currency — resolved via ResolveExchangeRateUseCase triangulation
    val displayCurrencyCode: String = "USD",
    val displayFormatCode: String = "USD",
    val displaySymbol: String = "$",
    val displayLabel: String = "USD",
    val displayRate: Double = 1.0,
)

private data class DisplayCurrencySettings(
    val code: String = "USD",
    val formatCode: String = "USD",
    val symbol: String = "$",
    val label: String = "USD",
    val rate: Double = 1.0,
)

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    getTransactionsUseCase: GetTransactionsUseCase,
    getAccountsUseCase: GetAccountsUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val transactionRepository: TransactionRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val categoryRepository: CategoryRepository,
    private val resolveExchangeRate: ResolveExchangeRateUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val filters = MutableStateFlow(Filters())
    private val _displayCurrency = MutableStateFlow(DisplayCurrencySettings())

    val uiState: StateFlow<TransactionListUiState> = combine(
        getTransactionsUseCase(),
        getAccountsUseCase(),
        getCategoriesUseCase(),
        filters,
        _displayCurrency,
    ) { transactions, accounts, allCategories, filter, displayCurrency ->
        val filtered = transactions.filter { tx ->
            val matchType = filter.typeFilter == null || tx.type == filter.typeFilter
            val matchAccount = filter.accountId == null || tx.accountId == filter.accountId
            val matchCurrency = filter.currencyFilter == null || tx.currencyCode == filter.currencyFilter
            val matchCategory = filter.categoryId == null || tx.categoryId == filter.categoryId
            val matchSearch = filter.searchQuery.isBlank() ||
                (tx.note ?: "").contains(filter.searchQuery, ignoreCase = true)
            matchType && matchAccount && matchCurrency && matchCategory && matchSearch
        }

        val accountMap = accounts.associateBy { it.id }

        val withCategories = filtered.map { tx ->
            val category = tx.categoryId?.let { categoryRepository.getCategoryById(it) }
            val account = accountMap[tx.accountId]
            TransactionWithCategory(
                transaction = tx,
                categoryName = category?.name ?: "Sin categoría",
                categoryIconName = category?.iconName ?: "Apps",
                categoryColorHex = category?.colorHex ?: "#9E9E9E",
                accountName = account?.name ?: "",
                accountType = account?.type ?: AccountType.CASH,
                accountPlatform = account?.platform ?: AccountPlatform.NONE,
            )
        }

        // Group by date, respecting sort order for group ordering
        val grouped = withCategories
            .groupBy { DateUtils.startOfDay(it.transaction.date) }
            .entries
            .let { entries ->
                when (filter.sortOrder) {
                    SortOrder.DATE_ASC -> entries.sortedBy { it.key }
                    else -> entries.sortedByDescending { it.key }
                }
            }
            .map { (dayMillis, txs) ->
                val dayNet = txs.sumOf { twc ->
                    when (twc.transaction.type) {
                        TransactionType.INCOME -> twc.transaction.amountInUsd
                        TransactionType.EXPENSE -> -twc.transaction.amountInUsd
                        TransactionType.TRANSFER -> 0.0
                    }
                }
                val sortedTxs = when (filter.sortOrder) {
                    SortOrder.DATE_DESC -> txs.sortedByDescending { it.transaction.date }
                    SortOrder.DATE_ASC -> txs.sortedBy { it.transaction.date }
                    SortOrder.AMOUNT_DESC -> txs.sortedByDescending { it.transaction.amount }
                    SortOrder.AMOUNT_ASC -> txs.sortedBy { it.transaction.amount }
                }
                TransactionDateGroup(
                    dateMillis = dayMillis,
                    transactions = sortedTxs,
                    dayNetUsd = dayNet,
                )
            }

        val totalIncome = filtered
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amountInUsd }
        val totalExpense = filtered
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amountInUsd }

        val (curStart, curEnd) = DateUtils.getCurrentMonthRange()
        val prevMonthSomeDay = curStart - 32L * 24 * 60 * 60 * 1000
        val prevMonthStart = DateUtils.startOfMonth(prevMonthSomeDay)
        val prevMonthEnd = DateUtils.endOfMonth(prevMonthSomeDay)

        val currentMonthTotal = transactions
            .filter { it.date in curStart..curEnd }
            .filter { filter.typeFilter == null || it.type == filter.typeFilter }
            .sumOf { it.amountInUsd }

        val prevMonthTotal = transactions
            .filter { it.date in prevMonthStart..prevMonthEnd }
            .filter { filter.typeFilter == null || it.type == filter.typeFilter }
            .sumOf { it.amountInUsd }

        val monthPercent = if (prevMonthTotal > 0) {
            ((currentMonthTotal - prevMonthTotal) / prevMonthTotal) * 100.0
        } else null

        TransactionListUiState(
            groupedTransactions = grouped,
            accounts = accounts,
            selectedTypeFilter = filter.typeFilter,
            selectedAccountId = filter.accountId,
            selectedCurrencyFilter = filter.currencyFilter,
            selectedCategoryId = filter.categoryId,
            searchQuery = filter.searchQuery,
            sortOrder = filter.sortOrder,
            totalIncomeUsd = totalIncome,
            totalExpenseUsd = totalExpense,
            monthOverMonthPercent = monthPercent,
            categories = allCategories,
            transactions = filtered,
            displayCurrencyCode = displayCurrency.code,
            displayFormatCode = displayCurrency.formatCode,
            displaySymbol = displayCurrency.symbol,
            displayLabel = displayCurrency.label,
            displayRate = displayCurrency.rate,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionListUiState(),
    )

    fun setTypeFilter(type: TransactionType?) {
        filters.update { it.copy(typeFilter = type) }
    }

    fun setAccountFilter(accountId: String?) {
        filters.update { it.copy(accountId = accountId) }
    }

    fun setCurrencyFilter(currency: String?) {
        filters.update { it.copy(currencyFilter = currency) }
    }

    fun setCategoryFilter(categoryId: String?) {
        filters.update { it.copy(categoryId = categoryId) }
    }

    fun setSearchQuery(query: String) {
        filters.update { it.copy(searchQuery = query) }
    }

    fun setSortOrder(order: SortOrder) {
        filters.update { it.copy(sortOrder = order) }
    }

    /**
     * Sets the display currency for the hero amounts using VES-intermediary triangulation.
     *
     * Uses [ResolveExchangeRateUseCase] which handles:
     * - USD→VES rates from BCV official source
     * - USD→VES rates from USDT parallel source
     * - EUR→VES rates from BCV and EURI sources
     * - Cross-currency via VES: e.g., USD→USDT = (USD→VES_bcv) × (VES→USD_usdt) ≈ 0.95
     *
     * This ensures that 100 USD ≠ 100 USDT (the parallel market premium is reflected).
     */
    fun setDisplayCurrency(code: String) {
        viewModelScope.launch {
            if (code == "USD") {
                _displayCurrency.update {
                    DisplayCurrencySettings("USD", "USD", "$", "USD", 1.0)
                }
                return@launch
            }

            val prefs = userPreferencesRepository.userPreferences.first()
            val result = resolveExchangeRate.resolve("USD", code, prefs.preferredRateSource)

            val (formatCode, symbol, label) = when (code) {
                "VES" -> Triple("VES", "Bs.", "Bs")
                "USDT" -> Triple("USDT", "₮", "USDT")
                "EUR" -> Triple("EUR", "€", "EUR")
                "EURI" -> Triple("EURI", "€", "EURI")
                else -> Triple(code, code, code)
            }

            _displayCurrency.update {
                DisplayCurrencySettings(
                    code = code,
                    formatCode = formatCode,
                    symbol = symbol,
                    label = label,
                    rate = result.rate,
                )
            }
        }
    }

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            deleteTransactionUseCase(transactionId)
        }
    }

    private data class Filters(
        val typeFilter: TransactionType? = null,
        val accountId: String? = null,
        val currencyFilter: String? = null,
        val categoryId: String? = null,
        val searchQuery: String = "",
        val sortOrder: SortOrder = SortOrder.DATE_DESC,
    )
}
