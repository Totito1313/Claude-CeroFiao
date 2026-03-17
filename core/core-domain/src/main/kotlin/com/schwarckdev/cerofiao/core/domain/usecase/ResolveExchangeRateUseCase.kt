package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.model.Currencies
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import javax.inject.Inject

/**
 * Resolves exchange rates between any two currencies, handling:
 * - USDT ↔ USD peg (1:1)
 * - EURI ↔ EUR peg (1:1)
 * - Cross-currency conversion via VES as intermediate (since DB only stores X↔VES pairs)
 */
class ResolveExchangeRateUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
) {
    data class RateResult(
        val rate: Double,
        val source: ExchangeRateSource,
    )

    /**
     * Resolves the rate to convert [from] currency to [to] currency.
     * Uses [preferredSource] when no implicit source is defined for the currency.
     */
    suspend fun resolve(
        from: String,
        to: String,
        preferredSource: ExchangeRateSource,
    ): RateResult {
        val baseFrom = Currencies.baseCurrency(from)
        val baseTo = Currencies.baseCurrency(to)

        // Identity (e.g., USDT→USD, EURI→EUR, USD→USD)
        if (baseFrom == baseTo) return RateResult(1.0, preferredSource)

        val sourceFrom = Currencies.implicitSource(from) ?: preferredSource
        val sourceTo = Currencies.implicitSource(to) ?: preferredSource

        // Try direct lookup: baseFrom → baseTo
        val direct = exchangeRateRepository.getLatestRateBySource(baseFrom, baseTo, sourceFrom)
            ?: exchangeRateRepository.getLatestRate(baseFrom, baseTo)
        if (direct != null) return RateResult(direct.rate, direct.source)

        // Cross-rate via VES: baseFrom → VES → baseTo
        val toVes = (exchangeRateRepository.getLatestRateBySource(baseFrom, "VES", sourceFrom)
            ?: exchangeRateRepository.getLatestRate(baseFrom, "VES"))?.rate
        val fromVes = (exchangeRateRepository.getLatestRateBySource("VES", baseTo, sourceTo)
            ?: exchangeRateRepository.getLatestRate("VES", baseTo))?.rate

        if (toVes != null && fromVes != null) {
            return RateResult(MoneyCalculator.convert(toVes, fromVes), sourceFrom)
        }

        return RateResult(1.0, ExchangeRateSource.MANUAL)
    }

    /**
     * Convenience: resolves [currencyCode] → USD rate.
     */
    suspend fun toUsd(
        currencyCode: String,
        preferredSource: ExchangeRateSource,
    ): RateResult = resolve(currencyCode, "USD", preferredSource)

    /**
     * Convenience: resolves USD → [currencyCode] rate.
     */
    suspend fun fromUsd(
        currencyCode: String,
        preferredSource: ExchangeRateSource,
    ): RateResult = resolve("USD", currencyCode, preferredSource)
}
