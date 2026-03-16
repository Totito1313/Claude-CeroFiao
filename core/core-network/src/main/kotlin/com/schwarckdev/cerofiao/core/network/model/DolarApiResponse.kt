package com.schwarckdev.cerofiao.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class DolarApiResponse(
    val moneda: String,
    val fuente: String,
    val nombre: String,
    val compra: Double? = null,
    val venta: Double? = null,
    val promedio: Double,
    val fechaActualizacion: String,
)
