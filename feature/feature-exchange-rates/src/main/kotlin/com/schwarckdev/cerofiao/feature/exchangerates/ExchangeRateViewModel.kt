package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.usecase.RefreshExchangeRatesUseCase
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExchangeRateUiState(
    val bcvUsdRate: ExchangeRate? = null,
    val usdtRate: ExchangeRate? = null,
    val bcvEurRate: ExchangeRate? = null,
    val euriRate: ExchangeRate? = null,
    val historicalUsd: List<ExchangeRate> = emptyList(),
    val historicalEur: List<ExchangeRate> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeRateUiState())
    val uiState: StateFlow<ExchangeRateUiState> = _uiState

    init {
        refresh()
    }

    private suspend fun loadRates() {
        val bcvUsd = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.BCV)
        val usdt = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.USDT)
        val bcvEur = exchangeRateRepository.getLatestRateBySource("EUR", "VES", ExchangeRateSource.BCV)
        val euri = exchangeRateRepository.getLatestRateBySource("EUR", "VES", ExchangeRateSource.EURI)
        _uiState.update {
            it.copy(
                bcvUsdRate = bcvUsd,
                usdtRate = usdt,
                bcvEurRate = bcvEur,
                euriRate = euri,
            )
        }
    }

    private suspend fun loadHistorical() {
        val usdHistory = exchangeRateRepository.getHistoricalRates("USD")
        val eurHistory = exchangeRateRepository.getHistoricalRates("EUR")
        _uiState.update {
            it.copy(
                historicalUsd = usdHistory,
                historicalEur = eurHistory,
            )
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                refreshExchangeRatesUseCase()
            } catch (_: Exception) {
                // Offline-first
            }
            loadRates()
            loadHistorical()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
