package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class RefreshExchangeRatesUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
) {
    suspend operator fun invoke() {
        exchangeRateRepository.refreshRates()
    }
}
