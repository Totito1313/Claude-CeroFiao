package com.schwarckdev.cerofiao.core.designsystem.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

/**
 * Formats monetary amounts with proper currency symbols and locale formatting.
 * 
 * USD  → $1,234.56
 * BS   → Bs 1.234,56 (Venezuelan locale)
 * USDT → ₮1,234.56
 * EUR  → €1,234.56
 */
object CurrencyFormatter {

    fun symbol(currencyCode: String): String = when (currencyCode.uppercase()) {
        "USD" -> "$"
        "BS" -> "Bs "
        "USDT" -> "₮"
        "EUR" -> "€"
        else -> "$"
    }

    /**
     * Formats a string amount with the appropriate currency symbol and locale.
     */
    fun format(amount: String, currencyCode: String): String {
        val value = try {
            BigDecimal(amount)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
        return format(value, currencyCode)
    }

    /**
     * Formats a BigDecimal amount with the appropriate currency symbol and locale.
     */
    fun format(amount: BigDecimal, currencyCode: String): String {
        val sym = symbol(currencyCode)
        val formatter = when (currencyCode.uppercase()) {
            "BS" -> NumberFormat.getNumberInstance(Locale("es", "VE")).apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
            else -> NumberFormat.getNumberInstance(Locale.US).apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
        }
        return "$sym${formatter.format(amount.setScale(2, RoundingMode.HALF_EVEN))}"
    }

    /**
     * Short format for compact display (no decimals for large amounts).
     */
    fun formatShort(amount: String, currencyCode: String): String {
        val value = try {
            BigDecimal(amount)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
        val sym = symbol(currencyCode)
        return if (value >= BigDecimal("1000")) {
            "$sym${NumberFormat.getNumberInstance(Locale.US).format(value.setScale(0, RoundingMode.HALF_EVEN))}"
        } else {
            format(value, currencyCode)
        }
    }
}
