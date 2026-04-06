package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tests for [BuildRateTableUseCase] — verifies the full 5×5 rate matrix
 * is built correctly using [ResolveExchangeRateUseCase] for triangulation.
 *
 * Uses the same [FakeExchangeRateRepository] from ResolveExchangeRateUseCaseTest.
 */
class BuildRateTableUseCaseTest {

    private val fakeRepository = FakeExchangeRateRepository()
    private lateinit var buildRateTable: BuildRateTableUseCase

    // Realistic rates (same as ResolveExchangeRateUseCaseTest)
    private val bcvUsdRate = 455.25
    private val usdtRate = 466.02
    private val bcvEurRate = 524.51
    private val euriRate = 758.92

    @Before
    fun setup() {
        fakeRepository.rates.clear()
        // USD <-> VES
        fakeRepository.rates.add(ExchangeRate("USD", "VES", bcvUsdRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("USD", "VES", usdtRate, "2026-03-26", ExchangeRateSource.USDT))
        fakeRepository.rates.add(ExchangeRate("VES", "USD", 1.0 / bcvUsdRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("VES", "USD", 1.0 / usdtRate, "2026-03-26", ExchangeRateSource.USDT))
        // EUR <-> VES
        fakeRepository.rates.add(ExchangeRate("EUR", "VES", bcvEurRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("EUR", "VES", euriRate, "2026-03-26", ExchangeRateSource.EURI))
        fakeRepository.rates.add(ExchangeRate("VES", "EUR", 1.0 / bcvEurRate, "2026-03-26", ExchangeRateSource.BCV))
        fakeRepository.rates.add(ExchangeRate("VES", "EUR", 1.0 / euriRate, "2026-03-26", ExchangeRateSource.EURI))

        val resolver = ResolveExchangeRateUseCase(fakeRepository)
        buildRateTable = BuildRateTableUseCase(resolver)
    }

    // ── Structure tests ──

    @Test
    fun `built table has identity rates for all currencies`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        assertEquals(1.0, table.rate("USD", "USD"), 0.001)
        assertEquals(1.0, table.rate("VES", "VES"), 0.001)
        assertEquals(1.0, table.rate("USDT", "USDT"), 0.001)
        assertEquals(1.0, table.rate("EUR", "EUR"), 0.001)
        assertEquals(1.0, table.rate("EURI", "EURI"), 0.001)
    }

    @Test
    fun `built table has all 20 non-identity pairs`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        val currencies = listOf("USD", "VES", "USDT", "EUR", "EURI")
        for (from in currencies) {
            for (to in currencies) {
                val rate = table.rate(from, to)
                if (from == to) {
                    assertEquals("$from→$to should be 1.0", 1.0, rate, 0.001)
                } else {
                    assertTrue("$from→$to should be > 0", rate > 0)
                    // Non-identity pairs should generally not be exactly 1.0
                    // (except if rates happen to align, which they don't with our test data)
                }
            }
        }
    }

    // ── Direct rate tests ──

    @Test
    fun `USD to VES uses BCV rate`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        assertEquals(bcvUsdRate, table.rate("USD", "VES"), 0.001)
    }

    @Test
    fun `USDT to VES uses USDT source regardless of preferred`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        assertEquals(usdtRate, table.rate("USDT", "VES"), 0.001)
    }

    @Test
    fun `EUR to VES uses BCV rate`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        assertEquals(bcvEurRate, table.rate("EUR", "VES"), 0.001)
    }

    @Test
    fun `EURI to VES uses EURI source`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        assertEquals(euriRate, table.rate("EURI", "VES"), 0.001)
    }

    // ── Parity loss: USD != USDT ──

    @Test
    fun `USD to USDT is not 1 to 1`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        val rate = table.rate("USD", "USDT")
        assertNotEquals("USD→USDT must not be 1.0", 1.0, rate, 0.001)
        // USD→VES(BCV=455.25) * VES→USD(USDT=1/466.02) ≈ 0.977
        val expected = bcvUsdRate * (1.0 / usdtRate)
        assertEquals(expected, rate, 0.01)
    }

    @Test
    fun `USDT to USD is not 1 to 1`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        val rate = table.rate("USDT", "USD")
        assertNotEquals("USDT→USD must not be 1.0", 1.0, rate, 0.001)
        // USDT→VES(USDT=466.02) * VES→USD(BCV=1/455.25) ≈ 1.024
        val expected = usdtRate * (1.0 / bcvUsdRate)
        assertEquals(expected, rate, 0.01)
    }

    @Test
    fun `EUR to EURI is not 1 to 1`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        val rate = table.rate("EUR", "EURI")
        assertNotEquals("EUR→EURI must not be 1.0", 1.0, rate, 0.001)
    }

    @Test
    fun `EURI to EUR is not 1 to 1`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        val rate = table.rate("EURI", "EUR")
        assertNotEquals("EURI→EUR must not be 1.0", 1.0, rate, 0.001)
    }

    // ── Preferred source=USDT still works (regression) ──

    @Test
    fun `preferred USDT still produces correct parity loss for USD to USDT`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.USDT)
        val rate = table.rate("USD", "USDT")
        assertNotEquals("USD→USDT must not be 1.0 even with preferred=USDT", 1.0, rate, 0.001)
    }

    @Test
    fun `preferred EURI still produces correct parity loss for EUR to EURI`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.EURI)
        val rate = table.rate("EUR", "EURI")
        assertNotEquals("EUR→EURI must not be 1.0 even with preferred=EURI", 1.0, rate, 0.001)
    }

    // ── convertToAll consistency ──

    @Test
    fun `convertToAll from USD produces correct amounts`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        val multi = table.convertToAll(100.0, "USD")

        assertEquals(100.0, multi.usd, 0.01)
        assertEquals(100.0 * bcvUsdRate, multi.ves, 0.01)  // 45525.0

        // USDT: 100 * (bcvUsd * 1/usdt) ≈ 100 * 0.977
        val expectedUsdt = 100.0 * bcvUsdRate * (1.0 / usdtRate)
        assertEquals(expectedUsdt, multi.usdt, 0.5)

        // EUR: 100 * (bcvUsd→VES * VES→EUR(BCV))
        val expectedEur = 100.0 * bcvUsdRate * (1.0 / bcvEurRate)
        assertEquals(expectedEur, multi.eur, 0.5)
    }

    @Test
    fun `10 USD and 10 USDT produce different VES amounts`() = runTest {
        val table = buildRateTable.build(ExchangeRateSource.BCV)
        val fromUsd = table.convertToAll(10.0, "USD")
        val fromUsdt = table.convertToAll(10.0, "USDT")

        assertNotEquals("VES from 10 USD vs 10 USDT must differ", fromUsd.ves, fromUsdt.ves, 0.01)
        assertNotEquals("USD from 10 USD vs 10 USDT must differ", fromUsd.usd, fromUsdt.usd, 0.01)
    }
}
