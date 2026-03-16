package com.schwarckdev.cerofiao.core.network

import com.schwarckdev.cerofiao.core.network.model.DolarApiResponse

interface ExchangeRateApi {
    suspend fun getAllDollarRates(): List<DolarApiResponse>
    suspend fun getOfficialRate(): DolarApiResponse
    suspend fun getParallelRate(): DolarApiResponse
    suspend fun getAllEuroRates(): List<DolarApiResponse>
}
