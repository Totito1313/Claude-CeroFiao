package com.schwarckdev.cerofiao.core.network

import com.schwarckdev.cerofiao.core.network.model.DolarApiResponse
import com.schwarckdev.cerofiao.core.network.model.HistoricalRateResponse

interface ExchangeRateApi {
    suspend fun getAllDollarRates(): List<DolarApiResponse>
    suspend fun getOfficialRate(): DolarApiResponse
    suspend fun getParallelRate(): DolarApiResponse
    suspend fun getAllEuroRates(): List<DolarApiResponse>
    suspend fun getHistoricalDollarRates(): List<HistoricalRateResponse>
    suspend fun getHistoricalEuroRates(): List<HistoricalRateResponse>
}
