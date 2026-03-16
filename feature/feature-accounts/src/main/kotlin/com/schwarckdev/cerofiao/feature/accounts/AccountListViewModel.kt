package com.schwarckdev.cerofiao.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AccountListUiState(
    val accounts: List<Account> = emptyList(),
    val totalBalance: Map<String, Double> = emptyMap(),
)

@HiltViewModel
class AccountListViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase,
) : ViewModel() {

    val uiState: StateFlow<AccountListUiState> = getAccountsUseCase()
        .map { accounts ->
            val totalBalance = accounts
                .filter { it.includeInTotal }
                .groupBy { it.currencyCode }
                .mapValues { (_, group) -> group.sumOf { it.balance } }

            AccountListUiState(
                accounts = accounts,
                totalBalance = totalBalance,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AccountListUiState(),
        )
}
