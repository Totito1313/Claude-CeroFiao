package com.schwarckdev.cerofiao.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class MultiCurrencyAmountTest {

    @Test
    fun `ZERO has all fields at zero`() {
        val zero = MultiCurrencyAmount.ZERO
        assertEquals(0.0, zero.usd, 0.0)
        assertEquals(0.0, zero.ves, 0.0)
        assertEquals(0.0, zero.usdt, 0.0)
        assertEquals(0.0, zero.eur, 0.0)
        assertEquals(0.0, zero.euri, 0.0)
    }

    @Test
    fun `inCurrency returns correct field`() {
        val amount = MultiCurrencyAmount(usd = 10.0, ves = 455.0, usdt = 9.48, eur = 9.25, euri = 8.75)
        assertEquals(10.0, amount.inCurrency("USD"), 0.001)
        assertEquals(455.0, amount.inCurrency("VES"), 0.001)
        assertEquals(9.48, amount.inCurrency("USDT"), 0.001)
        assertEquals(9.25, amount.inCurrency("EUR"), 0.001)
        assertEquals(8.75, amount.inCurrency("EURI"), 0.001)
    }

    @Test
    fun `inCurrency defaults to USD for unknown code`() {
        val amount = MultiCurrencyAmount(usd = 42.0)
        assertEquals(42.0, amount.inCurrency("BTC"), 0.001)
    }

    @Test
    fun `plus adds all fields`() {
        val a = MultiCurrencyAmount(usd = 10.0, ves = 100.0, usdt = 9.0, eur = 8.0, euri = 7.0)
        val b = MultiCurrencyAmount(usd = 5.0, ves = 50.0, usdt = 4.5, eur = 4.0, euri = 3.5)
        val result = a + b
        assertEquals(15.0, result.usd, 0.001)
        assertEquals(150.0, result.ves, 0.001)
        assertEquals(13.5, result.usdt, 0.001)
        assertEquals(12.0, result.eur, 0.001)
        assertEquals(10.5, result.euri, 0.001)
    }

    @Test
    fun `minus subtracts all fields`() {
        val a = MultiCurrencyAmount(usd = 10.0, ves = 100.0, usdt = 9.0, eur = 8.0, euri = 7.0)
        val b = MultiCurrencyAmount(usd = 3.0, ves = 30.0, usdt = 2.0, eur = 1.0, euri = 0.5)
        val result = a - b
        assertEquals(7.0, result.usd, 0.001)
        assertEquals(70.0, result.ves, 0.001)
        assertEquals(7.0, result.usdt, 0.001)
        assertEquals(7.0, result.eur, 0.001)
        assertEquals(6.5, result.euri, 0.001)
    }

    @Test
    fun `negate inverts all fields`() {
        val amount = MultiCurrencyAmount(usd = 10.0, ves = -5.0, usdt = 0.0, eur = 3.0, euri = -1.0)
        val result = amount.negate()
        assertEquals(-10.0, result.usd, 0.001)
        assertEquals(5.0, result.ves, 0.001)
        assertEquals(0.0, result.usdt, 0.001)
        assertEquals(-3.0, result.eur, 0.001)
        assertEquals(1.0, result.euri, 0.001)
    }

    @Test
    fun `fold with ZERO accumulates correctly`() {
        val amounts = listOf(
            MultiCurrencyAmount(usd = 10.0, ves = 100.0),
            MultiCurrencyAmount(usd = 20.0, ves = 200.0),
            MultiCurrencyAmount(usd = 30.0, ves = 300.0),
        )
        val total = amounts.fold(MultiCurrencyAmount.ZERO) { acc, a -> acc + a }
        assertEquals(60.0, total.usd, 0.001)
        assertEquals(600.0, total.ves, 0.001)
    }
}
