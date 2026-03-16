package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity

@Entity(
    tableName = "exchange_rate",
    primaryKeys = ["fromCurrency", "toCurrency", "date", "source"],
)
data class ExchangeRateEntity(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val date: String,
    val source: String,
    val fetchedAt: Long,
)
