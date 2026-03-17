package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    val decimalPlaces: Int = 2,
    val isActive: Boolean = true,
    val sortOrder: Int = 0,
)

object Currencies {
    val USD = Currency(code = "USD", name = "Dólar Estadounidense", symbol = "$", decimalPlaces = 2, sortOrder = 0)
    val VES = Currency(code = "VES", name = "Bolívar", symbol = "Bs", decimalPlaces = 2, sortOrder = 1)
    val USDT = Currency(code = "USDT", name = "Tether", symbol = "₮", decimalPlaces = 2, sortOrder = 2)
    val EUR = Currency(code = "EUR", name = "Euro", symbol = "€", decimalPlaces = 2, sortOrder = 3)
    val EURI = Currency(code = "EURI", name = "Euro Informal", symbol = "€", decimalPlaces = 2, sortOrder = 4)

    val defaults = listOf(USD, VES, USDT, EUR, EURI)

    /**
     * Maps a currency to its base fiat currency for exchange rate lookups.
     * USDT tracks USD 1:1, EURI tracks EUR 1:1.
     */
    fun baseCurrency(code: String): String = when (code) {
        "USDT" -> "USD"
        "EURI" -> "EUR"
        else -> code
    }

    /**
     * Returns the implicit exchange rate source for market-tracked currencies.
     * USDT uses the parallel USD rate, EURI uses the parallel EUR rate.
     */
    fun implicitSource(code: String): ExchangeRateSource? = when (code) {
        "USDT" -> ExchangeRateSource.USDT
        "EURI" -> ExchangeRateSource.EURI
        else -> null
    }
}
