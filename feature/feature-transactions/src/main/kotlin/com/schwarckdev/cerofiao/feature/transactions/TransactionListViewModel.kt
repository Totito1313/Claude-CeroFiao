package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.BuildRateTableUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.DeleteTransactionUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetTransactionsUseCase
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
    /** Total income already converted to display currency via RateTable */
    val totalIncomeDisplay: Double = 0.0,
    /** Total expenses already converted to display currency via RateTable */
    val totalExpenseDisplay: Double = 0.0,
    val monthOverMonthPercent: Double? = null,
    val categories: List<Category> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val displayCurrencyCode: String = "USD",
    val displayFormatCode: String = "USD",
    val displaySymbol: String = "$",
    val displayLabel: String = "USD",
)

private data class DisplayCurrencySettings(
    val code: String = "USD",
    val formatCode: String = "USD",
    val symbol: String = "$",
    val label: String = "USD",
)

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    getTransactionsUseCase: GetTransactionsUseCase,
    getAccountsUseCase: GetAccountsUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val transactionRepository: TransactionRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val categoryRepository: CategoryRepository,
    private val buildRateTable: BuildRateTableUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val filters = MutableStateFlow(Filters())
    private val _displayCurrency = MutableStateFlow(DisplayCurrencySettings())

    val uiState: StateFlow<TransactionListUiState> = combine(
        getTransactionsUseCase(),
        getAccountsUseCase(),
        getCategoriesUseCase(),
        filters,
        combine(_displayCurrency, userPreferencesRepository.userPreferences) { dc, prefs ->
            dc to prefs
        },
    ) { transactions, accounts, allCategories, filter, (displayCurrency, prefs) ->
        // Build rate table ONCE per emission — all display conversions derive from it
        val rateTable = buildRateTable.build(prefs.preferredRateSource)
        val displayCode = displayCurrency.code

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
                    val tx = twc.transaction
                    val amt = rateTable.convert(tx.amount, tx.currencyCode, displayCode)
                    when (tx.type) {
                        TransactionType.INCOME -> amt
                        TransactionType.EXPENSE -> -amt
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

        // Convert each transaction directly to display currency via RateTable — single step,
        // no intermediate USD rounding. This matches the dashboard path exactly.
        val totalIncome = filtered
            .filter { it.type == TransactionType.INCOME }
            .sumOf { rateTable.convert(it.amount, it.currencyCode, displayCode) }
        val totalExpense = filtered
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { rateTable.convert(it.amount, it.currencyCode, displayCode) }

        val (curStart, curEnd) = DateUtils.getCurrentMonthRange()
        val prevMonthSomeDay = curStart - 32L * 24 * 60 * 60 * 1000
        val prevMonthStart = DateUtils.startOfMonth(prevMonthSomeDay)
        val prevMonthEnd = DateUtils.endOfMonth(prevMonthSomeDay)

        val currentMonthTotal = transactions
            .filter { it.date in curStart..curEnd }
            .filter { filter.typeFilter == null || it.type == filter.typeFilter }
            .sumOf { rateTable.convert(it.amount, it.currencyCode, displayCode) }

        val prevMonthTotal = transactions
            .filter { it.date in prevMonthStart..prevMonthEnd }
            .filter { filter.typeFilter == null || it.type == filter.typeFilter }
            .sumOf { rateTable.convert(it.amount, it.currencyCode, displayCode) }

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
            totalIncomeDisplay = totalIncome,
            totalExpenseDisplay = totalExpense,
            monthOverMonthPercent = monthPercent,
            categories = allCategories,
            transactions = filtered,
            displayCurrencyCode = displayCurrency.code,
            displayFormatCode = displayCurrency.formatCode,
            displaySymbol = displayCurrency.symbol,
            displayLabel = displayCurrency.label,
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
     * Sets the display currency for the hero amounts.
     * The actual rate is derived reactively from the RateTable in the combine block,
     * so this only needs to update the display metadata.
     */
    fun setDisplayCurrency(code: String) {
        val (formatCode, symbol, label) = when (code) {
            "USD" -> Triple("USD", "$", "USD")
            "VES" -> Triple("VES", "Bs.", "Bs")
            "USDT" -> Triple("USDT", "\u20AE", "USDT")
            "EUR" -> Triple("EUR", "\u20AC", "EUR")
            "EURI" -> Triple("EURI", "\u20AC", "EURI")
            else -> Triple(code, code, code)
        }
        _displayCurrency.update {
            DisplayCurrencySettings(
                code = code,
                formatCode = formatCode,
                symbol = symbol,
                label = label,
            )
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
