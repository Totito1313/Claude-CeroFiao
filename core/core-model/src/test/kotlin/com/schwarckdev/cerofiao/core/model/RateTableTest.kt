package com.schwarckdev.cerofiao.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class RateTableTest {

    private val testTable = RateTable(
        mapOf(
            "USD" to mapOf("VES" to 45.50, "USDT" to 0.9479, "EUR" to 0.9248, "EURI" to 0.875),
            "VES" to mapOf("USD" to 0.02198, "USDT" to 0.02083, "EUR" to 0.02033, "EURI" to 0.01923),
            "USDT" to mapOf("USD" to 1.0549, "VES" to 48.0, "EUR" to 0.9756, "EURI" to 0.9231),
            "EUR" to mapOf("USD" to 1.0813, "VES" to 49.20, "USDT" to 1.025, "EURI" to 0.9462),
            "EURI" to mapOf("USD" to 1.1429, "VES" to 52.0, "USDT" to 1.0833, "EUR" to 1.0569),
        )
    )

    @Test
    fun `identity rate returns 1 for same currency`() {
        assertEquals(1.0, testTable.rate("USD", "USD"), 0.001)
        assertEquals(1.0, testTable.rate("VES", "VES"), 0.001)
        assertEquals(1.0, testTable.rate("USDT", "USDT"), 0.001)
    }

    @Test
    fun `rate returns stored value`() {
        assertEquals(45.50, testTable.rate("USD", "VES"), 0.001)
        assertEquals(0.9479, testTable.rate("USD", "USDT"), 0.001)
    }

    @Test
    fun `missing pair falls back to 1`() {
        assertEquals(1.0, testTable.rate("BTC", "USD"), 0.001)
    }

    @Test
    fun `convert applies rate with 2dp rounding`() {
        // 100 USD → VES at 45.50 = 4550.00
        assertEquals(4550.0, testTable.convert(100.0, "USD", "VES"), 0.01)
        // 100 USD → USDT at 0.9479 = 94.79
        assertEquals(94.79, testTable.convert(100.0, "USD", "USDT"), 0.01)
    }

    @Test
    fun `convert identity returns same amount`() {
        assertEquals(123.45, testTable.convert(123.45, "USD", "USD"), 0.001)
    }

    @Test
    fun `convertToAll produces all 5 currencies`() {
        val multi = testTable.convertToAll(100.0, "USD")
        assertEquals(100.0, multi.usd, 0.01)      // identity
        assertEquals(4550.0, multi.ves, 0.01)      // 100 × 45.50
        assertEquals(94.79, multi.usdt, 0.01)      // 100 × 0.9479
        assertEquals(92.48, multi.eur, 0.01)       // 100 × 0.9248
        assertEquals(87.50, multi.euri, 0.01)      // 100 × 0.875
    }

    @Test
    fun `USD is not USDT in converted amounts`() {
        val fromUsd = testTable.convertToAll(10.0, "USD")
        val fromUsdt = testTable.convertToAll(10.0, "USDT")
        assertNotEquals(fromUsd.usd, fromUsdt.usd, 0.001)
        assertNotEquals(fromUsd.ves, fromUsdt.ves, 0.001)
    }

    @Test
    fun `IDENTITY table returns amount unchanged`() {
        assertEquals(42.0, RateTable.IDENTITY.convert(42.0, "USD", "VES"), 0.001)
        assertEquals(1.0, RateTable.IDENTITY.rate("USD", "VES"), 0.001)
    }
}
