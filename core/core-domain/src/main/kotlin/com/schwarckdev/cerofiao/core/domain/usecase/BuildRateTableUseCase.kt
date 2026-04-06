package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.RateTable
import javax.inject.Inject

/**
 * Builds a complete [RateTable] with exchange rates for all 5×5 currency pairs.
 *
 * Calls [ResolveExchangeRateUseCase.resolve] for each non-identity pair (20 calls),
 * which handles triangulation, parity-loss, and source selection internally.
 * The resulting table is meant to be built once per rate-source/preference change
 * and reused for all conversions in a given emission cycle.
 */
class BuildRateTableUseCase @Inject constructor(
    private val resolveExchangeRate: ResolveExchangeRateUseCase,
) {
    private val allCodes = listOf("USD", "VES", "USDT", "EUR", "EURI")

    /**
     * Builds the full rate matrix using [preferredSource] for non-implicit currencies.
     *
     * @return A [RateTable] with rates for all 25 pairs (5 identity + 20 resolved).
     */
    suspend fun build(preferredSource: ExchangeRateSource): RateTable {
        val rates = mutableMapOf<String, MutableMap<String, Double>>()
        for (from in allCodes) {
            val inner = mutableMapOf<String, Double>()
            for (to in allCodes) {
                if (from == to) continue
                inner[to] = resolveExchangeRate.resolve(from, to, preferredSource).rate
            }
            rates[from] = inner
        }
        return RateTable(rates)
    }
}
