package com.schwarckdev.cerofiao.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.usecase.GetGlobalBalanceUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetTransactionsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RefreshExchangeRatesUseCase
import com.schwarckdev.cerofiao.core.model.GlobalBalance
import com.schwarckdev.cerofiao.core.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val globalBalance: GlobalBalance? = null,
    val recentTransactions: List<Transaction> = emptyList(),
    val isRefreshing: Boolean = false,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getGlobalBalanceUseCase: GetGlobalBalanceUseCase,
    getTransactionsUseCase: GetTransactionsUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
) : ViewModel() {

    private val isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<DashboardUiState> = combine(
        getGlobalBalanceUseCase(),
        getTransactionsUseCase.recent(5),
        isRefreshing,
    ) { balance, transactions, refreshing ->
        DashboardUiState(
            globalBalance = balance,
            recentTransactions = transactions,
            isRefreshing = refreshing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState(),
    )

    init {
        refreshRates()
    }

    fun refreshRates() {
        viewModelScope.launch {
            isRefreshing.value = true
            try {
                refreshExchangeRatesUseCase()
            } catch (_: Exception) {
                // Offline-first: silently fail
            }
            isRefreshing.value = false
        }
    }
}
