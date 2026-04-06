package com.schwarckdev.cerofiao.core.model

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Pre-computed exchange rate matrix for all 5×5 currency pairs.
 *
 * Built once per rate-source change via `BuildRateTableUseCase`, then used
 * for all conversions across the app. Eliminates redundant per-account or
 * per-transaction `resolve()` calls.
 *
 * @param rates Map of `fromCurrency → toCurrency → rate`. Identity pairs
 *              (e.g., USD→USD) are implicit and always return 1.0.
 */
data class RateTable(
    private val rates: Map<String, Map<String, Double>>,
) {
    /** Returns the exchange rate from [from] to [to]. Falls back to 1.0 for identity or missing pairs. */
    fun rate(from: String, to: String): Double =
        if (from == to) 1.0 else rates[from]?.get(to) ?: 1.0

    /**
     * Converts [amount] from [from] currency to [to] currency.
     * Uses BigDecimal with 2 decimal place rounding (HALF_UP) for final display amounts.
     */
    fun convert(amount: Double, from: String, to: String): Double {
        if (from == to) return amount
        val r = rate(from, to)
        return BigDecimal.valueOf(amount)
            .multiply(BigDecimal.valueOf(r))
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }

    /** Converts [amount] from [from] currency into all 5 currencies at once. */
    fun convertToAll(amount: Double, from: String): MultiCurrencyAmount = MultiCurrencyAmount(
        usd = convert(amount, from, "USD"),
        ves = convert(amount, from, "VES"),
        usdt = convert(amount, from, "USDT"),
        eur = convert(amount, from, "EUR"),
        euri = convert(amount, from, "EURI"),
    )

    companion object {
        /** Identity table where all rates default to 1.0. Useful as StateFlow initial value. */
        val IDENTITY = RateTable(emptyMap())
    }
}
