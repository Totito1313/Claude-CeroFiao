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

    val defaults = listOf(USD, VES, USDT, EUR)
}
