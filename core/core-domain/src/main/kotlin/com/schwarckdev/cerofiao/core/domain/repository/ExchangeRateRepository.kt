package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    suspend fun getLatestRate(from: String, to: String): ExchangeRate?
    suspend fun getLatestRateBySource(from: String, to: String, source: ExchangeRateSource): ExchangeRate?
    suspend fun getRatesForDate(from: String, to: String, date: String): List<ExchangeRate>
    fun getRateHistory(from: String, to: String, startDate: String, endDate: String): Flow<List<ExchangeRate>>
    fun getUsdToVesRates(): Flow<List<ExchangeRate>>
    suspend fun refreshRates()
    suspend fun insertManualRate(rate: ExchangeRate)
}
