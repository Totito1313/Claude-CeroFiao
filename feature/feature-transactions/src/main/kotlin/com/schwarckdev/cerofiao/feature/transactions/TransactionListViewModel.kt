package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val selectedTypeFilter: TransactionType? = null,
    val selectedAccountId: String? = null,
)

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    getTransactionsUseCase: GetTransactionsUseCase,
    getAccountsUseCase: GetAccountsUseCase,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val filters = MutableStateFlow(Filters())

    val uiState: StateFlow<TransactionListUiState> = combine(
        getTransactionsUseCase(),
        getAccountsUseCase(),
        filters,
    ) { transactions, accounts, filter ->
        val filtered = transactions
            .filter { tx ->
                (filter.typeFilter == null || tx.type == filter.typeFilter) &&
                    (filter.accountId == null || tx.accountId == filter.accountId)
            }

        TransactionListUiState(
            transactions = filtered,
            accounts = accounts,
            selectedTypeFilter = filter.typeFilter,
            selectedAccountId = filter.accountId,
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

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transactionId)
        }
    }

    private data class Filters(
        val typeFilter: TransactionType? = null,
        val accountId: String? = null,
    )
}
