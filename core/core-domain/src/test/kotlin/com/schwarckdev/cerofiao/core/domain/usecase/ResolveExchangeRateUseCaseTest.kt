package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeExchangeRateRepository : ExchangeRateRepository {
    val rates = mutableListOf<ExchangeRate>()

    override suspend fun getLatestRate(from: String, to: String): ExchangeRate? {
        // Return latest by comparing fetchedAt conceptually, or just the first matching one
        return rates.find { it.fromCurrency == from && it.toCurrency == to }
    }

    override suspend fun getLatestRateBySource(
        from: String,
        to: String,
        source: ExchangeRateSource
    ): ExchangeRate? {
        return rates.find { it.fromCurrency == from && it.toCurrency == to && it.source == source }
    }

    override suspend fun getRatesForDate(from: String, to: String, date: String): List<ExchangeRate> = emptyList()
    override fun getRateHistory(from: String, to: String, startDate: String, endDate: String): Flow<List<ExchangeRate>> = emptyFlow()
    override fun getUsdToVesRates(): Flow<List<ExchangeRate>> = emptyFlow()
    override suspend fun refreshRates() {}
    override suspend fun insertManualRate(rate: ExchangeRate) {}
    override suspend fun getHistoricalRates(currency: String): List<ExchangeRate> = emptyList()
}

class ResolveExchangeRateUseCaseTest {

    private val fakeRepository = FakeExchangeRateRepository()
    private val useCase = ResolveExchangeRateUseCase(fakeRepository)

    @Test
    fun `resolve 1 to 1 pegs like USDT to USD returns 1`() = runTest {
        val result = useCase.resolve("USDT", "USD", ExchangeRateSource.BCV)
        assertEquals(1.0, result.rate, 0.001)
    }

    @Test
    fun `resolve direct lookup USD to VES returns stored rate`() = runTest {
        // Setup direct rate USD -> VES
        fakeRepository.rates.add(
            ExchangeRate("USD", "VES", 60.0, "2026-01-01", ExchangeRateSource.BCV)
        )
        val result = useCase.resolve("USD", "VES", ExchangeRateSource.BCV)
        assertEquals(60.0, result.rate, 0.001)
    }

    @Test
    fun `resolve cross currency EUR to USD through VES`() = runTest {
        // Assume we don't have EUR -> USD directly, but we have EUR -> VES and USD -> VES (meaning VES -> USD implies division)
        // Wait, the use case relies on `baseFrom -> VES` and `VES -> baseTo`.
        // So EUR -> VES is 65.0
        // VES -> USD is 1/60.0
        fakeRepository.rates.add(
            ExchangeRate("EUR", "VES", 65.0, "2026-01-01", ExchangeRateSource.BCV)
        )
        fakeRepository.rates.add(
            ExchangeRate("VES", "USD", 1.0 / 60.0, "2026-01-01", ExchangeRateSource.BCV)
        )

        val result = useCase.resolve("EUR", "USD", ExchangeRateSource.BCV)
        // Expected cross rate = 65.0 * (1.0 / 60.0) = 1.0833, rounded to 2 decimals by MoneyCalculator = 1.08
        assertEquals(1.08, result.rate, 0.001)
    }

    @Test
    fun `resolve returns 1_0 as fallback if no rates found`() = runTest {
        fakeRepository.rates.clear()
        val result = useCase.resolve("CAD", "JPY", ExchangeRateSource.BCV)
        assertEquals(1.0, result.rate, 0.001)
        assertEquals(ExchangeRateSource.MANUAL, result.source)
    }
}
