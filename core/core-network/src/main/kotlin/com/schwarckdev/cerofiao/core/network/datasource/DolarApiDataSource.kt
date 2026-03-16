package com.schwarckdev.cerofiao.core.network.datasource

import com.schwarckdev.cerofiao.core.network.ExchangeRateApi
import com.schwarckdev.cerofiao.core.network.model.DolarApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DolarApiDataSource @Inject constructor(
    private val httpClient: HttpClient,
) : ExchangeRateApi {

    companion object {
        private const val BASE_URL = "https://ve.dolarapi.com/v1"
    }

    override suspend fun getAllDollarRates(): List<DolarApiResponse> {
        return httpClient.get("$BASE_URL/dolares").body()
    }

    override suspend fun getOfficialRate(): DolarApiResponse {
        return httpClient.get("$BASE_URL/dolar-oficial").body()
    }

    override suspend fun getParallelRate(): DolarApiResponse {
        return httpClient.get("$BASE_URL/dolar-paralelo").body()
    }

    override suspend fun getAllEuroRates(): List<DolarApiResponse> {
        return httpClient.get("$BASE_URL/euros").body()
    }
}
