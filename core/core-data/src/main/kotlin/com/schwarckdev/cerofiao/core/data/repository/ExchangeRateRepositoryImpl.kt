package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.database.dao.ExchangeRateDao
import com.schwarckdev.cerofiao.core.database.entity.ExchangeRateEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.network.ExchangeRateApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateApi: ExchangeRateApi,
) : ExchangeRateRepository {

    companion object {
        private const val CACHE_TTL_MS = 4 * 60 * 60 * 1000L // 4 hours
    }

    override suspend fun getLatestRate(from: String, to: String): ExchangeRate? =
        exchangeRateDao.getLatestRate(from, to)?.toModel()

    override suspend fun getLatestRateBySource(
        from: String,
        to: String,
        source: ExchangeRateSource,
    ): ExchangeRate? =
        exchangeRateDao.getLatestRateBySource(from, to, source.name)?.toModel()

    override suspend fun getRatesForDate(from: String, to: String, date: String): List<ExchangeRate> =
        exchangeRateDao.getRatesForDate(from, to, date).map { it.toModel() }

    override fun getRateHistory(
        from: String,
        to: String,
        startDate: String,
        endDate: String,
    ): Flow<List<ExchangeRate>> =
        exchangeRateDao.getRateHistory(from, to, startDate, endDate).map { list -> list.map { it.toModel() } }

    override fun getUsdToVesRates(): Flow<List<ExchangeRate>> =
        exchangeRateDao.getUsdToVesRates().map { list -> list.map { it.toModel() } }

    override suspend fun refreshRates() {
        // Check cache freshness
        val today = DateUtils.todayIsoDate()
        val cached = exchangeRateDao.getRatesForDate("USD", "VES", today)
        val now = DateUtils.now()

        if (cached.isNotEmpty() && (now - cached.first().fetchedAt) < CACHE_TTL_MS) {
            return // Cache is still fresh
        }

        try {
            val rates = exchangeRateApi.getAllDollarRates()
            val entities = rates.map { apiRate ->
                val source = when (apiRate.fuente) {
                    "oficial" -> ExchangeRateSource.BCV.name
                    "paralelo" -> ExchangeRateSource.PARALLEL.name
                    else -> ExchangeRateSource.PARALLEL.name
                }

                // API returns rate as: 1 USD = X VES (promedio)
                ExchangeRateEntity(
                    fromCurrency = "USD",
                    toCurrency = "VES",
                    rate = apiRate.promedio,
                    date = today,
                    source = source,
                    fetchedAt = now,
                )
            }

            exchangeRateDao.insertAll(entities)

            // Also store inverse rates (VES -> USD) for convenience
            val inverseEntities = entities.map { entity ->
                entity.copy(
                    fromCurrency = "VES",
                    toCurrency = "USD",
                    rate = if (entity.rate > 0) 1.0 / entity.rate else 0.0,
                )
            }
            exchangeRateDao.insertAll(inverseEntities)
        } catch (_: Exception) {
            // Network failure - app continues with cached data (offline-first)
        }
    }

    override suspend fun insertManualRate(rate: ExchangeRate) {
        exchangeRateDao.insert(
            ExchangeRateEntity(
                fromCurrency = rate.fromCurrency,
                toCurrency = rate.toCurrency,
                rate = rate.rate,
                date = rate.date,
                source = ExchangeRateSource.MANUAL.name,
                fetchedAt = DateUtils.now(),
            )
        )
    }
}
