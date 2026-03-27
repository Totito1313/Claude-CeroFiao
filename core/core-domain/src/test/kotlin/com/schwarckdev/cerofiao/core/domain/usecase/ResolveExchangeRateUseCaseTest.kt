package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeExchangeRateRepository : ExchangeRateRepository {
    val rates = mutableListOf<ExchangeRate>()

    override suspend fun getLatestRate(from: String, to: String): ExchangeRate? {
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

    // Realistic rates
    private val bcvUsdRate = 455.25
    private val usdtRate = 466.02
    private val bcvEurRate = 524.51
    private val euriRate = 758.92

    @Before
    fun setup() {
        fakeRepository.rates.clear()
        // USD ↔ VES rates
        fakeRepository.rates.add(ExchangeRate("USD", "VES", bcvUsdRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("USD", "VES", usdtRate, "2026-03-26", ExchangeRateSource.USDT))
        fakeRepository.rates.add(ExchangeRate("VES", "USD", 1.0 / bcvUsdRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("VES", "USD", 1.0 / usdtRate, "2026-03-26", ExchangeRateSource.USDT))
        // EUR ↔ VES rates
        fakeRepository.rates.add(ExchangeRate("EUR", "VES", bcvEurRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("EUR", "VES", euriRate, "2026-03-26", ExchangeRateSource.EURI))
        fakeRepository.rates.add(ExchangeRate("VES", "EUR", 1.0 / bcvEurRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("VES", "EUR", 1.0 / euriRate, "2026-03-26", ExchangeRateSource.EURI))
    }

    // ── Identity tests ──

    @Test
    fun `resolve same currency returns 1`() = runTest {
        assertEquals(1.0, useCase.resolve("USD", "USD", ExchangeRateSource.BCV).rate, 0.001)
        assertEquals(1.0, useCase.resolve("VES", "VES", ExchangeRateSource.BCV).rate, 0.001)
    }

    @Test
    fun `USDT to USD is parity loss not 1 to 1`() = runTest {
        // USDT→USD goes through VES cross-rate showing the market difference
        // USDT→VES(USDT) * VES→USD(BCV) = 466.02 * (1/455.25) ≈ 1.024
        val result = useCase.resolve("USDT", "USD", ExchangeRateSource.BCV)
        assertTrue("USDT→USD should flag parity loss", result.isParityLoss)
        assertTrue("USDT→USD rate should be > 1.0", result.rate > 1.0)
    }

    @Test
    fun `EURI to EUR is parity loss not 1 to 1`() = runTest {
        // EURI→EUR goes through VES cross-rate
        // EURI→VES(EURI) * VES→EUR(BCV) = 758.92 * (1/524.51) ≈ 1.447
        val result = useCase.resolve("EURI", "EUR", ExchangeRateSource.BCV)
        assertTrue("EURI→EUR should flag parity loss", result.isParityLoss)
        assertTrue("EURI→EUR rate should be > 1.0", result.rate > 1.0)
    }

    // ── Direct USD conversions ──

    @Test
    fun `USD to VES uses BCV source`() = runTest {
        val result = useCase.resolve("USD", "VES", ExchangeRateSource.BCV)
        assertEquals(bcvUsdRate, result.rate, 0.001)
        assertEquals(ExchangeRateSource.BCV, result.source)
    }

    @Test
    fun `VES to USD uses BCV source`() = runTest {
        val result = useCase.resolve("VES", "USD", ExchangeRateSource.BCV)
        assertEquals(1.0 / bcvUsdRate, result.rate, 0.001)
        assertEquals(ExchangeRateSource.BCV, result.source)
    }

    // ── USDT conversions (must use USDT/parallel source) ──

    @Test
    fun `USDT to VES uses USDT parallel source`() = runTest {
        val result = useCase.resolve("USDT", "VES", ExchangeRateSource.BCV)
        assertEquals(usdtRate, result.rate, 0.001)
        assertEquals(ExchangeRateSource.USDT, result.source)
    }

    @Test
    fun `VES to USDT uses USDT source not BCV`() = runTest {
        val result = useCase.resolve("VES", "USDT", ExchangeRateSource.BCV)
        // Should use VES→USD with USDT source = 1/466.02
        val expectedRate = 1.0 / usdtRate
        assertEquals(expectedRate, result.rate, 0.0001)
        assertEquals(ExchangeRateSource.USDT, result.source)
    }

    @Test
    fun `VES to USDT gives different result than VES to USD`() = runTest {
        val vesUsdt = useCase.resolve("VES", "USDT", ExchangeRateSource.BCV)
        val vesUsd = useCase.resolve("VES", "USD", ExchangeRateSource.BCV)
        // VES→USDT should use parallel rate (1/466), VES→USD should use BCV (1/455)
        // These must use DIFFERENT sources
        assertEquals(ExchangeRateSource.USDT, vesUsdt.source)
        assertEquals(ExchangeRateSource.BCV, vesUsd.source)
        // VES→USDT rate should be lower (fewer USDT per Bs because parallel rate is higher)
        assertTrue(
            "VES→USDT rate (${vesUsdt.rate}) should be lower than VES→USD rate (${vesUsd.rate})",
            vesUsdt.rate < vesUsd.rate,
        )
        // For 26000 Bs, verify different amounts
        val usdtAmount = 26000 * vesUsdt.rate
        val usdAmount = 26000 * vesUsd.rate
        assertTrue(
            "26000 Bs→USDT ($usdtAmount) must differ from 26000 Bs→USD ($usdAmount)",
            kotlin.math.abs(usdtAmount - usdAmount) > 0.5,
        )
    }

    // ── EUR / EURI conversions ──

    @Test
    fun `EUR to VES uses BCV source`() = runTest {
        val result = useCase.resolve("EUR", "VES", ExchangeRateSource.BCV)
        assertEquals(bcvEurRate, result.rate, 0.001)
    }

    @Test
    fun `EURI to VES uses EURI source`() = runTest {
        val result = useCase.resolve("EURI", "VES", ExchangeRateSource.BCV)
        assertEquals(euriRate, result.rate, 0.001)
        assertEquals(ExchangeRateSource.EURI, result.source)
    }

    @Test
    fun `VES to EURI uses EURI source not BCV`() = runTest {
        val result = useCase.resolve("VES", "EURI", ExchangeRateSource.BCV)
        val expectedRate = 1.0 / euriRate
        assertEquals(expectedRate, result.rate, 0.0001)
        assertEquals(ExchangeRateSource.EURI, result.source)
    }

    @Test
    fun `VES to EURI gives different result than VES to EUR`() = runTest {
        val vesEuri = useCase.resolve("VES", "EURI", ExchangeRateSource.BCV)
        val vesEur = useCase.resolve("VES", "EUR", ExchangeRateSource.BCV)
        assertTrue(
            "VES→EURI (${vesEuri.rate}) and VES→EUR (${vesEur.rate}) must differ",
            kotlin.math.abs(vesEuri.rate - vesEur.rate) > 0.0001,
        )
    }

    // ── Parity loss (USD ↔ USDT cross via VES) ──

    @Test
    fun `USD to USDT shows parity loss via VES cross rate`() = runTest {
        val result = useCase.resolve("USD", "USDT", ExchangeRateSource.BCV)
        assertTrue("USD→USDT should flag parity loss", result.isParityLoss)
        // Rate = bcvUsd→VES * VES→USD(USDT) = 455.25 * (1/466.02) ≈ 0.977
        val expectedRate = bcvUsdRate * (1.0 / usdtRate)
        assertEquals(expectedRate, result.rate, 0.01)
    }

    @Test
    fun `USDT to USD shows parity loss`() = runTest {
        val result = useCase.resolve("USDT", "USD", ExchangeRateSource.BCV)
        assertTrue("USDT→USD should flag parity loss", result.isParityLoss)
    }

    // ── Cross-currency (EUR ↔ USD via VES) ──

    @Test
    fun `EUR to USD cross rate through VES`() = runTest {
        val result = useCase.resolve("EUR", "USD", ExchangeRateSource.BCV)
        // EUR→VES(BCV) * VES→USD(BCV) = 524.51 * (1/455.25) ≈ 1.15
        val expectedRate = bcvEurRate * (1.0 / bcvUsdRate)
        assertEquals(expectedRate, result.rate, 0.01)
    }

    // ── Fallback ──

    @Test
    fun `resolve returns 1_0 as fallback if no rates found`() = runTest {
        fakeRepository.rates.clear()
        val result = useCase.resolve("CAD", "JPY", ExchangeRateSource.BCV)
        assertEquals(1.0, result.rate, 0.001)
        assertEquals(ExchangeRateSource.MANUAL, result.source)
    }
}
