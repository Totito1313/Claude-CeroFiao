package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val date: String,
    val source: ExchangeRateSource,
    val fetchedAt: Long = 0L,
)
