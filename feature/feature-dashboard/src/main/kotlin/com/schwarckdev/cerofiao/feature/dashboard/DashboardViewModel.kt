package com.schwarckdev.cerofiao.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetGlobalBalanceUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetTransactionsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RefreshExchangeRatesUseCase
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
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
    val bcvRate: ExchangeRate? = null,
    val usdtRate: ExchangeRate? = null,
    val isRefreshing: Boolean = false,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getGlobalBalanceUseCase: GetGlobalBalanceUseCase,
    getTransactionsUseCase: GetTransactionsUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    private val exchangeRateRepository: ExchangeRateRepository,
) : ViewModel() {

    private val ratesState = MutableStateFlow<Pair<ExchangeRate?, ExchangeRate?>>(null to null)
    private val isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<DashboardUiState> = combine(
        getGlobalBalanceUseCase(),
        getTransactionsUseCase.recent(5),
        ratesState,
        isRefreshing,
    ) { balance, transactions, rates, refreshing ->
        DashboardUiState(
            globalBalance = balance,
            recentTransactions = transactions,
            bcvRate = rates.first,
            usdtRate = rates.second,
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
            loadRates()
            isRefreshing.value = false
        }
    }

    private suspend fun loadRates() {
        val bcv = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.BCV)
        val usdt = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.USDT)
        ratesState.value = bcv to usdt
    }
}
