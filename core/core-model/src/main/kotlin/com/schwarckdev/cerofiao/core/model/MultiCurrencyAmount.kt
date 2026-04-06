package com.schwarckdev.cerofiao.core.model

/**
 * A monetary amount expressed simultaneously in all 5 supported currencies.
 *
 * Eliminates ad-hoc conversion across the app — amounts are pre-computed once
 * via [RateTable.convertToAll] and any screen can pick the currency it needs
 * with [inCurrency].
 */
data class MultiCurrencyAmount(
    val usd: Double = 0.0,
    val ves: Double = 0.0,
    val usdt: Double = 0.0,
    val eur: Double = 0.0,
    val euri: Double = 0.0,
) {
    operator fun plus(other: MultiCurrencyAmount): MultiCurrencyAmount = MultiCurrencyAmount(
        usd = usd + other.usd,
        ves = ves + other.ves,
        usdt = usdt + other.usdt,
        eur = eur + other.eur,
        euri = euri + other.euri,
    )

    operator fun minus(other: MultiCurrencyAmount): MultiCurrencyAmount = MultiCurrencyAmount(
        usd = usd - other.usd,
        ves = ves - other.ves,
        usdt = usdt - other.usdt,
        eur = eur - other.eur,
        euri = euri - other.euri,
    )

    fun negate(): MultiCurrencyAmount = MultiCurrencyAmount(
        usd = -usd,
        ves = -ves,
        usdt = -usdt,
        eur = -eur,
        euri = -euri,
    )

    /** Returns the amount in the given currency code. Defaults to USD for unknown codes. */
    fun inCurrency(code: String): Double = when (code) {
        "USD" -> usd
        "VES" -> ves
        "USDT" -> usdt
        "EUR" -> eur
        "EURI" -> euri
        else -> usd
    }

    companion object {
        val ZERO = MultiCurrencyAmount()
    }
}
