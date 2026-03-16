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
    val bcvRate: ExchangeRate? = null,
    val parallelRate: ExchangeRate? = null,
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
        loadRates()
        refresh()
    }

    private fun loadRates() {
        viewModelScope.launch {
            val bcv = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.BCV)
            val parallel = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.PARALLEL)
            _uiState.update { it.copy(bcvRate = bcv, parallelRate = parallel) }
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
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
