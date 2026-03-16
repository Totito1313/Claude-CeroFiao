package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.schwarckdev.cerofiao.core.database.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Query(
        "SELECT * FROM exchange_rate WHERE fromCurrency = :from AND toCurrency = :to " +
            "AND date = :date ORDER BY source ASC"
    )
    suspend fun getRatesForDate(from: String, to: String, date: String): List<ExchangeRateEntity>

    @Query(
        "SELECT * FROM exchange_rate WHERE fromCurrency = :from AND toCurrency = :to " +
            "ORDER BY date DESC, fetchedAt DESC LIMIT 1"
    )
    suspend fun getLatestRate(from: String, to: String): ExchangeRateEntity?

    @Query(
        "SELECT * FROM exchange_rate WHERE fromCurrency = :from AND toCurrency = :to " +
            "AND source = :source ORDER BY date DESC LIMIT 1"
    )
    suspend fun getLatestRateBySource(
        from: String,
        to: String,
        source: String,
    ): ExchangeRateEntity?

    @Query(
        "SELECT * FROM exchange_rate WHERE fromCurrency = :from AND toCurrency = :to " +
            "AND date BETWEEN :startDate AND :endDate ORDER BY date ASC"
    )
    fun getRateHistory(
        from: String,
        to: String,
        startDate: String,
        endDate: String,
    ): Flow<List<ExchangeRateEntity>>

    @Query(
        "SELECT * FROM exchange_rate WHERE fromCurrency = 'USD' AND toCurrency = 'VES' " +
            "ORDER BY date DESC, fetchedAt DESC"
    )
    fun getUsdToVesRates(): Flow<List<ExchangeRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<ExchangeRateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rate: ExchangeRateEntity)
}
