package com.schwarckdev.cerofiao.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyCalculatorTest {

    private val delta = 0.001

    @Test
    fun `convert should multiply amount by rate and round correctly`() {
        // 10.555 * 1.5 = 15.8325 -> rounded half up = 15.83
        val result = MoneyCalculator.convert(10.555, 1.5)
        assertEquals(15.83, result, delta)
    }

    @Test
    fun `toUsd should return same amount if currency is USD`() {
        val result = MoneyCalculator.toUsd(100.0, "USD", 0.0) // Rate shouldn't matter
        assertEquals(100.0, result, delta)
    }

    @Test
    fun `toUsd should convert using rate if currency is not USD`() {
        // Example: converting VES to USD. Suppose rateToUsd is 1/60.
        val result = MoneyCalculator.toUsd(6000.0, "VES", 1.0 / 60.0)
        assertEquals(100.0, result, delta)
    }

    @Test
    fun `fromUsd should return same amount if currency is USD`() {
        val result = MoneyCalculator.fromUsd(100.0, "USD", 0.0)
        assertEquals(100.0, result, delta)
    }

    @Test
    fun `fromUsd should convert using rate if currency is not USD`() {
        // Example: converting USD to VES. Rate is 60.
        val result = MoneyCalculator.fromUsd(100.0, "VES", 60.0)
        assertEquals(6000.0, result, delta)
    }

    @Test
    fun `applyCommission should subtract correct percentage`() {
        // 100 - (100 * 0.05) = 95
        val result = MoneyCalculator.applyCommission(100.0, 5.0)
        assertEquals(95.0, result, delta)
    }

    @Test
    fun `calculateCommission should return only the commission amount`() {
        // 200 * 0.02 = 4
        val result = MoneyCalculator.calculateCommission(200.0, 2.0)
        assertEquals(4.0, result, delta)
    }
}
