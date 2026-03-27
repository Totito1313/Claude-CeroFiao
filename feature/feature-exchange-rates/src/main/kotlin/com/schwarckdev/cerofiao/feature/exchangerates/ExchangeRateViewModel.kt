package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.usecase.RefreshExchangeRatesUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
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
    // Calculator state
    val calculatorAmount: String = "",
    val calculatorFromCurrency: String = "USD",
    val calculatorToCurrency: String = "VES",
    val calculatorResult: Double? = null,
    val calculatorResultSource: ExchangeRateSource? = null,
    // Custom Rate
    val calculatorCustomRate: String = "",
    val isCustomRateEnabled: Boolean = false,
    val isParityLossWarning: Boolean = false,
    val parityDifferenceAmount: Double? = null,
    val parityDifferenceVes: Double? = null,
)

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    private val resolveExchangeRateUseCase: ResolveExchangeRateUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeRateUiState())
    val uiState: StateFlow<ExchangeRateUiState> = _uiState

    private var calculatorJob: Job? = null

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

        // Filter to last 30 days for smoother charts
        val thirtyDaysAgo = LocalDate.now().minusDays(30).toString() // "YYYY-MM-DD"

        _uiState.update {
            it.copy(
                historicalUsd = usdHistory.filter { rate -> rate.date >= thirtyDaysAgo },
                historicalEur = eurHistory.filter { rate -> rate.date >= thirtyDaysAgo },
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

    fun updateCalculatorAmount(amount: String) {
        _uiState.update { it.copy(calculatorAmount = amount) }
        recalculate()
    }

    fun updateCalculatorFromCurrency(code: String) {
        _uiState.update { it.copy(calculatorFromCurrency = code) }
        recalculate()
    }

    fun updateCalculatorToCurrency(code: String) {
        _uiState.update { it.copy(calculatorToCurrency = code) }
        recalculate()
    }

    fun swapCalculatorCurrencies() {
        _uiState.update {
            it.copy(
                calculatorFromCurrency = it.calculatorToCurrency,
                calculatorToCurrency = it.calculatorFromCurrency,
            )
        }
        recalculate()
    }

    fun updateCalculatorCustomRate(rate: String) {
        _uiState.update { it.copy(calculatorCustomRate = rate) }
        recalculate()
    }

    fun toggleCustomRate(enabled: Boolean) {
        _uiState.update { it.copy(isCustomRateEnabled = enabled) }
        recalculate()
    }

    private fun recalculate() {
        calculatorJob?.cancel()
        calculatorJob = viewModelScope.launch {
            val state = _uiState.value
            val amount = state.calculatorAmount.toDoubleOrNull()
            if (amount == null || amount == 0.0) {
                _uiState.update { it.copy(calculatorResult = null, calculatorResultSource = null) }
                return@launch
            }
            if (state.isCustomRateEnabled && state.calculatorCustomRate.isNotEmpty()) {
                val customRate = state.calculatorCustomRate.toDoubleOrNull()
                if (customRate != null && customRate > 0) {
                    _uiState.update {
                        it.copy(
                            calculatorResult = amount * customRate,
                            calculatorResultSource = null,
                            isParityLossWarning = false,
                            parityDifferenceAmount = null,
                            parityDifferenceVes = null,
                        )
                    }
                    return@launch
                }
            }
            try {
                val result = resolveExchangeRateUseCase.resolve(
                    from = state.calculatorFromCurrency,
                    to = state.calculatorToCurrency,
                    preferredSource = ExchangeRateSource.BCV,
                )
                
                var diffAmount: Double? = null
                var diffVes: Double? = null
                val finalResult = amount * result.rate
                
                val baseToVesRate = result.baseToVesRate
                if (result.isParityLoss && baseToVesRate != null && amount > 0) {
                    diffAmount = kotlin.math.abs(amount - finalResult)
                    diffVes = diffAmount * baseToVesRate
                }

                _uiState.update {
                    it.copy(
                        calculatorResult = finalResult,
                        calculatorResultSource = result.source,
                        isParityLossWarning = result.isParityLoss,
                        parityDifferenceAmount = diffAmount,
                        parityDifferenceVes = diffVes,
                    )
                }
            } catch (_: Exception) {
                _uiState.update { 
                    it.copy(
                        calculatorResult = null, 
                        calculatorResultSource = null, 
                        isParityLossWarning = false,
                        parityDifferenceAmount = null,
                        parityDifferenceVes = null,
                    ) 
                }
            }
        }
    }
}
