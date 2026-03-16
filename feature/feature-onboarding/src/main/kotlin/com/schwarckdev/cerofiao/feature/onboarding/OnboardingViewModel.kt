package com.schwarckdev.cerofiao.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val currentStep: Int = 0,
    val selectedCurrency: String = "USD",
    val selectedRateSource: ExchangeRateSource = ExchangeRateSource.PARALLEL,
    val selectedPlatforms: Set<AccountPlatform> = emptySet(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun selectCurrency(currencyCode: String) {
        _uiState.update { it.copy(selectedCurrency = currencyCode) }
    }

    fun selectRateSource(source: ExchangeRateSource) {
        _uiState.update { it.copy(selectedRateSource = source) }
    }

    fun togglePlatform(platform: AccountPlatform) {
        _uiState.update { state ->
            val updated = if (platform in state.selectedPlatforms) {
                state.selectedPlatforms - platform
            } else {
                state.selectedPlatforms + platform
            }
            state.copy(selectedPlatforms = updated)
        }
    }

    fun nextStep() {
        _uiState.update { it.copy(currentStep = (it.currentStep + 1).coerceAtMost(3)) }
    }

    fun previousStep() {
        _uiState.update { it.copy(currentStep = (it.currentStep - 1).coerceAtLeast(0)) }
    }

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            userPreferencesRepository.setDisplayCurrency(_uiState.value.selectedCurrency)
            userPreferencesRepository.setPreferredRateSource(_uiState.value.selectedRateSource)

            for (platform in _uiState.value.selectedPlatforms) {
                val account = Account(
                    id = UuidGenerator.generate(),
                    name = platform.displayName,
                    type = platform.defaultType,
                    platform = platform,
                    currencyCode = platform.defaultCurrencyCode,
                    initialBalance = 0.0,
                    balance = 0.0,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                )
                accountRepository.createAccount(account)
            }

            userPreferencesRepository.setOnboardingCompleted()

            _uiState.update { it.copy(isLoading = false) }
            onComplete()
        }
    }
}
