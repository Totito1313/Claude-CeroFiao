package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetTransactionsUseCase
import com.schwarckdev.cerofiao.core.model.Account
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
)

data class TransactionDateGroup(
    val dateMillis: Long,
    val transactions: List<TransactionWithCategory>,
    val dayNetUsd: Double,
)

data class TransactionListUiState(
    val groupedTransactions: List<TransactionDateGroup> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val selectedTypeFilter: TransactionType? = null,
    val selectedAccountId: String? = null,
    val selectedCurrencyFilter: String? = null,
    val searchQuery: String = "",
    val totalIncomeUsd: Double = 0.0,
    val totalExpenseUsd: Double = 0.0,
    // Keep flat list for compatibility
    val transactions: List<Transaction> = emptyList(),
)

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    getTransactionsUseCase: GetTransactionsUseCase,
    getAccountsUseCase: GetAccountsUseCase,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val filters = MutableStateFlow(Filters())

    val uiState: StateFlow<TransactionListUiState> = combine(
        getTransactionsUseCase(),
        getAccountsUseCase(),
        filters,
    ) { transactions, accounts, filter ->
        val filtered = transactions.filter { tx ->
            val matchType = filter.typeFilter == null || tx.type == filter.typeFilter
            val matchAccount = filter.accountId == null || tx.accountId == filter.accountId
            val matchCurrency = filter.currencyFilter == null || tx.currencyCode == filter.currencyFilter
            val matchSearch = filter.searchQuery.isBlank() ||
                (tx.note ?: "").contains(filter.searchQuery, ignoreCase = true)
            matchType && matchAccount && matchCurrency && matchSearch
        }

        val accountMap = accounts.associateBy { it.id }

        val withCategories = filtered.map { tx ->
            val category = tx.categoryId?.let { categoryRepository.getCategoryById(it) }
            TransactionWithCategory(
                transaction = tx,
                categoryName = category?.name ?: "Sin categoría",
                categoryIconName = category?.iconName ?: "Apps",
                categoryColorHex = category?.colorHex ?: "#9E9E9E",
                accountName = accountMap[tx.accountId]?.name ?: "",
            )
        }

        // Group by date (day)
        val grouped = withCategories
            .groupBy { DateUtils.startOfDay(it.transaction.date) }
            .entries
            .sortedByDescending { it.key }
            .map { (dayMillis, txs) ->
                val dayNet = txs.sumOf { twc ->
                    when (twc.transaction.type) {
                        TransactionType.INCOME -> twc.transaction.amountInUsd
                        TransactionType.EXPENSE -> -twc.transaction.amountInUsd
                        TransactionType.TRANSFER -> 0.0
                    }
                }
                TransactionDateGroup(
                    dateMillis = dayMillis,
                    transactions = txs.sortedByDescending { it.transaction.date },
                    dayNetUsd = dayNet,
                )
            }

        val totalIncome = filtered
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amountInUsd }
        val totalExpense = filtered
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amountInUsd }

        TransactionListUiState(
            groupedTransactions = grouped,
            accounts = accounts,
            selectedTypeFilter = filter.typeFilter,
            selectedAccountId = filter.accountId,
            selectedCurrencyFilter = filter.currencyFilter,
            searchQuery = filter.searchQuery,
            totalIncomeUsd = totalIncome,
            totalExpenseUsd = totalExpense,
            transactions = filtered,
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

    fun setSearchQuery(query: String) {
        filters.update { it.copy(searchQuery = query) }
    }

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transactionId)
        }
    }

    private data class Filters(
        val typeFilter: TransactionType? = null,
        val accountId: String? = null,
        val currencyFilter: String? = null,
        val searchQuery: String = "",
    )
}
