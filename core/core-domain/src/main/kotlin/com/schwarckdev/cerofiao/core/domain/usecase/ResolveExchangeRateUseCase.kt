package com.schwarckdev.cerofiao.core.domain.usecase

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
open class ResolveExchangeRateUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
) {
    data class RateResult(
        val rate: Double,
        val source: ExchangeRateSource,
        val isParityLoss: Boolean = false,
        val baseToVesRate: Double? = null,
    )

    /**
     * Resolves the rate to convert [from] currency to [to] currency.
     * Uses [preferredSource] when no implicit source is defined for the currency.
     */
    open suspend fun resolve(
        from: String,
        to: String,
        preferredSource: ExchangeRateSource,
    ): RateResult {
        val baseFrom = Currencies.baseCurrency(from)
        val baseTo = Currencies.baseCurrency(to)

        // Identity strictly equal
        if (from == to) return RateResult(1.0, preferredSource)

        val implicitFrom = Currencies.implicitSource(from)
        val implicitTo = Currencies.implicitSource(to)
        val sourceFrom = implicitFrom ?: preferredSource
        val sourceTo = implicitTo ?: preferredSource

        // For direct lookups, prefer the implicit source (most specific) from either currency
        val directSource = implicitFrom ?: implicitTo ?: preferredSource

        val isParityLoss = baseFrom == baseTo && from != to

        // Try direct lookup using the most specific source first
        val direct = exchangeRateRepository.getLatestRateBySource(baseFrom, baseTo, directSource)
            ?: exchangeRateRepository.getLatestRate(baseFrom, baseTo)
        if (direct != null && !isParityLoss) return RateResult(direct.rate, direct.source)

        // Cross-rate via VES: baseFrom → VES → baseTo
        // Each leg uses the source of its corresponding currency
        val toVes = (exchangeRateRepository.getLatestRateBySource(baseFrom, "VES", sourceFrom)
            ?: exchangeRateRepository.getLatestRate(baseFrom, "VES"))?.rate
        val fromVes = (exchangeRateRepository.getLatestRateBySource("VES", baseTo, sourceTo)
            ?: exchangeRateRepository.getLatestRate("VES", baseTo))?.rate

        if (toVes != null && fromVes != null) {
            // IMPORTANT: Use raw multiplication for cross-rate computation to preserve
            // full Double precision (~15 significant digits). MoneyCalculator.convert()
            // rounds to 2 decimal places which destroys rate precision.
            // Example: 45.50 × 0.02083 = 0.9479 (correct), NOT 0.95 (2dp rounded).
            // The 2dp rounding should only happen on FINAL displayed amounts.
            val crossRate = toVes * fromVes
            return RateResult(crossRate, sourceFrom, isParityLoss, toVes)
        }

        return RateResult(1.0, ExchangeRateSource.MANUAL)
    }

    /**
     * Convenience: resolves [currencyCode] → USD rate.
     */
    open suspend fun toUsd(
        currencyCode: String,
        preferredSource: ExchangeRateSource,
    ): RateResult = resolve(currencyCode, "USD", preferredSource)

    /**
     * Convenience: resolves USD → [currencyCode] rate.
     */
    open suspend fun fromUsd(
        currencyCode: String,
        preferredSource: ExchangeRateSource,
    ): RateResult = resolve("USD", currencyCode, preferredSource)
}
