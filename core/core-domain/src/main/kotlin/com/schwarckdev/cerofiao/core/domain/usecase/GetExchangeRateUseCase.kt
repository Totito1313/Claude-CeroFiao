package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(from: String = "USD", to: String = "VES"): ExchangeRate? {
        val prefs = userPreferencesRepository.userPreferences.first()
        return exchangeRateRepository.getLatestRateBySource(from, to, prefs.preferredRateSource)
            ?: exchangeRateRepository.getLatestRate(from, to)
    }
}
