package com.schwarckdev.cerofiao.feature.transactions

import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Comprehensive test suite for CeroFiao's currency triangulation logic.
 *
 * Venezuelan exchange rate context:
 * - All rates are stored as X→VES (how many Bs per 1 unit of X)
 * - BCV = Banco Central de Venezuela official rate
 * - USDT = parallel/crypto market rate (always higher than BCV for USD)
 * - EURI = parallel euro market rate (always higher than BCV for EUR)
 *
 * Stored rates in this test (typical March 2026 scenario):
 *   USD→VES at BCV:   45.50 Bs per $1  (official)
 *   USD→VES at USDT:  48.00 Bs per $1  (parallel — HIGHER)
 *   EUR→VES at BCV:   49.20 Bs per €1  (official)
 *   EUR→VES at EURI:  52.00 Bs per €1  (parallel — HIGHER)
 *   Inverse rates also stored (VES→USD, VES→EUR)
 */
class TriangulationTest {

    private lateinit var resolver: ResolveExchangeRateUseCase
    private lateinit var repo: FakeExchangeRateRepository

    // ─── Test rates ───────────────────────────────────────────────
    private val usdToVesBcv = 45.50
    private val usdToVesUsdt = 48.00
    private val eurToVesBcv = 49.20
    private val eurToVesEuri = 52.00

    @Before
    fun setup() {
        repo = FakeExchangeRateRepository(
            rates = listOf(
                // USD ↔ VES
                rate("USD", "VES", usdToVesBcv, ExchangeRateSource.BCV),
                rate("USD", "VES", usdToVesUsdt, ExchangeRateSource.USDT),
                rate("VES", "USD", 1.0 / usdToVesBcv, ExchangeRateSource.BCV),
                rate("VES", "USD", 1.0 / usdToVesUsdt, ExchangeRateSource.USDT),
                // EUR ↔ VES
                rate("EUR", "VES", eurToVesBcv, ExchangeRateSource.BCV),
                rate("EUR", "VES", eurToVesEuri, ExchangeRateSource.EURI),
                rate("VES", "EUR", 1.0 / eurToVesBcv, ExchangeRateSource.BCV),
                rate("VES", "EUR", 1.0 / eurToVesEuri, ExchangeRateSource.EURI),
            ),
        )
        resolver = ResolveExchangeRateUseCase(repo)
    }

    // ═══════════════════════════════════════════════════════════════
    // 1. IDENTITY TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `USD to USD is always 1 to 1`() = runTest {
        val result = resolver.resolve("USD", "USD", ExchangeRateSource.BCV)
        assertEquals(1.0, result.rate, 0.001)
    }

    @Test
    fun `VES to VES is always 1 to 1`() = runTest {
        val result = resolver.resolve("VES", "VES", ExchangeRateSource.BCV)
        assertEquals(1.0, result.rate, 0.001)
    }

    // ═══════════════════════════════════════════════════════════════
    // 2. DIRECT RATE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `USD to VES at BCV returns official rate`() = runTest {
        val result = resolver.resolve("USD", "VES", ExchangeRateSource.BCV)
        assertEquals(usdToVesBcv, result.rate, 0.001)
        // 100 USD = 4,550 Bs at BCV
        val converted = 100.0 * result.rate
        assertEquals(4550.0, converted, 0.01)
    }

    @Test
    fun `USD to VES at USDT returns parallel rate`() = runTest {
        val result = resolver.resolve("USD", "VES", ExchangeRateSource.USDT)
        assertEquals(usdToVesUsdt, result.rate, 0.001)
        // 100 USD = 4,800 Bs at USDT parallel rate
        val converted = 100.0 * result.rate
        assertEquals(4800.0, converted, 0.01)
    }

    @Test
    fun `VES at BCV differs from VES at USDT — parallel premium exists`() = runTest {
        val bcv = resolver.resolve("USD", "VES", ExchangeRateSource.BCV)
        val usdt = resolver.resolve("USD", "VES", ExchangeRateSource.USDT)
        assertTrue(
            "USDT parallel rate (${usdt.rate}) should be higher than BCV (${bcv.rate})",
            usdt.rate > bcv.rate,
        )
    }

    // ═══════════════════════════════════════════════════════════════
    // 3. TRIANGULATION TESTS — THE CORE SKILL
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `USD to USDT is NOT 1-to-1 — triangulates through VES`() = runTest {
        // USD → USDT goes: USD→VES(BCV) × VES→USD(USDT)
        // = 45.50 × (1/48.00) = 0.9479
        // So 100 USD buys only ~94.79 USDT
        val result = resolver.resolve("USD", "USDT", ExchangeRateSource.BCV)

        assertNotEquals(
            "USD→USDT must NOT be 1.0 (parallel premium)",
            1.0, result.rate, 0.001,
        )

        val expectedRate = MoneyCalculator.convert(usdToVesBcv, 1.0 / usdToVesUsdt)
        assertEquals(expectedRate, result.rate, 0.01)

        val usdtEquivalent = 100.0 * result.rate
        assertTrue(
            "100 USD should buy less than 100 USDT (got $usdtEquivalent)",
            usdtEquivalent < 100.0,
        )
        // Expected: ~94.79 USDT
        assertEquals(94.79, usdtEquivalent, 0.5)
    }

    @Test
    fun `USDT to USD is the inverse — costs more than 1-to-1`() = runTest {
        // USDT → USD goes: USD→VES(USDT) × VES→USD(BCV)
        // = 48.00 × (1/45.50) = 1.0549
        // So 100 USDT buys ~105.49 USD equivalent
        val result = resolver.resolve("USDT", "USD", ExchangeRateSource.BCV)

        assertNotEquals(1.0, result.rate, 0.001)

        val usdEquivalent = 100.0 * result.rate
        assertTrue(
            "100 USDT should be worth more than 100 USD in BCV terms (got $usdEquivalent)",
            usdEquivalent > 100.0,
        )
    }

    @Test
    fun `EUR to EURI is NOT 1-to-1 — same parallel premium logic`() = runTest {
        val result = resolver.resolve("EUR", "EURI", ExchangeRateSource.BCV)

        assertNotEquals(
            "EUR→EURI must NOT be 1.0",
            1.0, result.rate, 0.001,
        )

        // EUR→EURI: EUR→VES(BCV) × VES→EUR(EURI)
        // = 49.20 × (1/52.00) = 0.9462
        val expectedRate = MoneyCalculator.convert(eurToVesBcv, 1.0 / eurToVesEuri)
        assertEquals(expectedRate, result.rate, 0.01)

        val euriEquivalent = 100.0 * result.rate
        assertTrue(
            "100 EUR should buy less than 100 EURI (got $euriEquivalent)",
            euriEquivalent < 100.0,
        )
    }

    @Test
    fun `USD to EUR triangulates correctly through VES`() = runTest {
        // USD→EUR: USD→VES(BCV) × VES→EUR(BCV)
        // = 45.50 × (1/49.20) = 0.9248
        val result = resolver.resolve("USD", "EUR", ExchangeRateSource.BCV)

        val expectedRate = MoneyCalculator.convert(usdToVesBcv, 1.0 / eurToVesBcv)
        assertEquals(expectedRate, result.rate, 0.01)

        val eurAmount = 100.0 * result.rate
        assertEquals(92.48, eurAmount, 0.5)
    }

    @Test
    fun `USD to EURI uses parallel sources on both legs`() = runTest {
        // USD→EURI: uses USDT source for USD leg, EURI source for EUR leg
        // implicitSource("EURI") = EURI, sourceFrom falls to preferredSource
        // USD→VES(preferred) × VES→EUR(EURI)
        // With preferred=BCV: 45.50 × (1/52.00) = 0.875
        val result = resolver.resolve("USD", "EURI", ExchangeRateSource.BCV)

        val expectedRate = MoneyCalculator.convert(usdToVesBcv, 1.0 / eurToVesEuri)
        assertEquals(expectedRate, result.rate, 0.01)
    }

    // ═══════════════════════════════════════════════════════════════
    // 4. DISPLAY CURRENCY CONVERSION (what the hero uses)
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `display currency rates produce different amounts for each currency`() = runTest {
        val totalUsd = 1000.0 // $1,000 in income
        val preferred = ExchangeRateSource.BCV

        val rateVes = resolver.resolve("USD", "VES", preferred).rate
        val rateUsdt = resolver.resolve("USD", "USDT", preferred).rate
        val rateEur = resolver.resolve("USD", "EUR", preferred).rate
        val rateEuri = resolver.resolve("USD", "EURI", preferred).rate

        val inVes = totalUsd * rateVes
        val inUsdt = totalUsd * rateUsdt
        val inEur = totalUsd * rateEur
        val inEuri = totalUsd * rateEuri

        // All must be different values
        val allValues = listOf(totalUsd, inVes, inUsdt, inEur, inEuri)
        assertEquals(
            "All 5 display values should be unique",
            allValues.size, allValues.toSet().size,
        )

        // VES should be the largest number (many Bs per dollar)
        assertTrue("VES ($inVes) should be >> USD ($totalUsd)", inVes > totalUsd * 10)

        // USDT should be < USD (parallel premium costs)
        assertTrue("USDT ($inUsdt) should be < USD ($totalUsd)", inUsdt < totalUsd)

        // EUR should be < USD (euro is stronger)
        assertTrue("EUR ($inEur) should be < USD ($totalUsd)", inEur < totalUsd)

        // EURI should be < EUR (parallel premium)
        assertTrue("EURI ($inEuri) should be < EUR ($inEur)", inEuri < inEur)
    }

    @Test
    fun `switching preferred source changes VES result`() = runTest {
        val bcvRate = resolver.resolve("USD", "VES", ExchangeRateSource.BCV).rate
        val usdtRate = resolver.resolve("USD", "VES", ExchangeRateSource.USDT).rate

        assertNotEquals(
            "VES at BCV ($bcvRate) must differ from VES at USDT ($usdtRate)",
            bcvRate, usdtRate, 0.001,
        )

        val totalUsd = 500.0
        val vesBcv = totalUsd * bcvRate    // e.g., 22,750 Bs
        val vesUsdt = totalUsd * usdtRate  // e.g., 24,000 Bs
        assertTrue("VES at USDT ($vesUsdt) > VES at BCV ($vesBcv)", vesUsdt > vesBcv)
    }

    // ═══════════════════════════════════════════════════════════════
    // 5. ROUNDTRIP CONSISTENCY (no phantom gains/losses)
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `roundtrip USD to VES and back yields original amount`() = runTest {
        val original = 100.0
        val toVes = resolver.resolve("USD", "VES", ExchangeRateSource.BCV).rate
        val fromVes = resolver.resolve("VES", "USD", ExchangeRateSource.BCV).rate

        val inVes = MoneyCalculator.convert(original, toVes)   // 4550.00
        val backToUsd = MoneyCalculator.convert(inVes, fromVes) // should be ~100.00
        assertEquals(original, backToUsd, 0.02) // tolerance for rounding
    }

    @Test
    fun `roundtrip EUR to VES and back at same source`() = runTest {
        val original = 100.0
        val toVes = resolver.resolve("EUR", "VES", ExchangeRateSource.BCV).rate
        val fromVes = resolver.resolve("VES", "EUR", ExchangeRateSource.BCV).rate

        val inVes = MoneyCalculator.convert(original, toVes)
        val backToEur = MoneyCalculator.convert(inVes, fromVes)
        assertEquals(original, backToEur, 0.02)
    }

    // ═══════════════════════════════════════════════════════════════
    // 6. SKILL VALIDATION: "100 USD ≠ 100 USDT" example from the spec
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `skill example - 100 USD equals approximately 94_79 USDT`() = runTest {
        // From the skill document:
        // rates = { USD: 45.50, USDT: 48.00 }
        // 100 USD → 100 × 45.50 = 4550 VES → 4550 / 48.00 = 94.79 USDT
        val result = resolver.resolve("USD", "USDT", ExchangeRateSource.BCV)
        val usdtAmount = 100.0 * result.rate
        assertEquals(
            "Skill example: 100 USD should triangulate to ~94.79 USDT",
            94.79, usdtAmount, 0.5,
        )
    }

    // ═══════════════════════════════════════════════════════════════
    // 7. PREFERRED SOURCE MUST NOT COLLAPSE PARITY-LOSS RATES
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `USD to USDT with preferred=USDT is still NOT 1-to-1`() = runTest {
        // REGRESSION: When preferred=USDT, both legs used USDT source:
        // 48.0 × (1/48.0) = 1.0 — making USD=USDT which is WRONG.
        // Fix: parity-loss ignores preferred, forces BCV for the base currency.
        val result = resolver.resolve("USD", "USDT", ExchangeRateSource.USDT)
        assertNotEquals(
            "USD→USDT must NOT be 1.0 even when preferred=USDT",
            1.0, result.rate, 0.001,
        )
        val usdtAmount = 100.0 * result.rate
        assertTrue("100 USD should buy < 100 USDT (got $usdtAmount)", usdtAmount < 100.0)
        assertEquals(94.79, usdtAmount, 0.5)
    }

    @Test
    fun `USDT to USD with preferred=USDT is still NOT 1-to-1`() = runTest {
        val result = resolver.resolve("USDT", "USD", ExchangeRateSource.USDT)
        assertNotEquals(
            "USDT→USD must NOT be 1.0 even when preferred=USDT",
            1.0, result.rate, 0.001,
        )
        val usdEquivalent = 100.0 * result.rate
        assertTrue("100 USDT should be > 100 USD (got $usdEquivalent)", usdEquivalent > 100.0)
    }

    @Test
    fun `EUR to EURI with preferred=EURI is still NOT 1-to-1`() = runTest {
        val result = resolver.resolve("EUR", "EURI", ExchangeRateSource.EURI)
        assertNotEquals(
            "EUR→EURI must NOT be 1.0 even when preferred=EURI",
            1.0, result.rate, 0.001,
        )
        val euriAmount = 100.0 * result.rate
        assertTrue("100 EUR should buy < 100 EURI (got $euriAmount)", euriAmount < 100.0)
    }

    // ═══════════════════════════════════════════════════════════════
    // 8. EDGE CASES
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `zero amount converts to zero`() = runTest {
        val rate = resolver.resolve("USD", "VES", ExchangeRateSource.BCV).rate
        assertEquals(0.0, 0.0 * rate, 0.001)
    }

    @Test
    fun `negative amount preserves sign through conversion`() = runTest {
        val rate = resolver.resolve("USD", "VES", ExchangeRateSource.BCV).rate
        val negResult = -100.0 * rate
        assertTrue("Negative amount should stay negative", negResult < 0)
        assertEquals(-4550.0, negResult, 0.01)
    }

    // ═══════════════════════════════════════════════════════════════
    // Helper
    // ═══════════════════════════════════════════════════════════════

    private fun rate(from: String, to: String, r: Double, source: ExchangeRateSource) =
        ExchangeRate(
            fromCurrency = from,
            toCurrency = to,
            rate = r,
            date = "2026-03-27",
            source = source,
        )
}

/**
 * Fake repository that stores rates in memory and serves them
 * with the same lookup semantics as the real Room DAO.
 */
class FakeExchangeRateRepository(
    private val rates: List<ExchangeRate>,
) : ExchangeRateRepository {

    override suspend fun getLatestRate(from: String, to: String): ExchangeRate? =
        rates.firstOrNull { it.fromCurrency == from && it.toCurrency == to }

    override suspend fun getLatestRateBySource(
        from: String,
        to: String,
        source: ExchangeRateSource,
    ): ExchangeRate? =
        rates.firstOrNull {
            it.fromCurrency == from && it.toCurrency == to && it.source == source
        }

    override suspend fun getRatesForDate(from: String, to: String, date: String) =
        rates.filter { it.fromCurrency == from && it.toCurrency == to && it.date == date }

    override fun getRateHistory(
        from: String, to: String, startDate: String, endDate: String,
    ): Flow<List<ExchangeRate>> = flowOf(emptyList())

    override fun getUsdToVesRates(): Flow<List<ExchangeRate>> = flowOf(
        rates.filter { it.fromCurrency == "USD" && it.toCurrency == "VES" },
    )

    override suspend fun refreshRates() {}
    override suspend fun insertManualRate(rate: ExchangeRate) {}
    override suspend fun getHistoricalRates(currency: String) = emptyList<ExchangeRate>()
}
