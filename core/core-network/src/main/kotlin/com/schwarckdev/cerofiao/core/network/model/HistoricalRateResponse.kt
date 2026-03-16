package com.schwarckdev.cerofiao.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoricalRateResponse(
    val fuente: String,
    val compra: Double? = null,
    val venta: Double? = null,
    val promedio: Double,
    val fecha: String,
    val moneda: String? = null,
)
