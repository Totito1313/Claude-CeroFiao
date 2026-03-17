package com.schwarckdev.cerofiao.core.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyFormatter {

    private val venezuelanSymbols = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val usSymbols = DecimalFormatSymbols(Locale.US)

    fun format(amount: Double, currencyCode: String, showSymbol: Boolean = true): String {
        val formatted = when (currencyCode) {
            "VES" -> formatVenezuelan(amount)
            else -> formatStandard(amount)
        }
        if (!showSymbol) return formatted
        return when (currencyCode) {
            "USD" -> "$$formatted"
            "VES" -> "$formatted Bs"
            "USDT" -> "$formatted USDT"
            "EUR" -> "€$formatted"
            "EURI" -> "€$formatted"
            else -> "$formatted $currencyCode"
        }
    }

    fun formatCompact(amount: Double, currencyCode: String): String {
        val symbol = when (currencyCode) {
            "USD" -> "$"
            "VES" -> "Bs"
            "USDT" -> "USDT"
            "EUR" -> "€"
            "EURI" -> "€"
            else -> currencyCode
        }
        return when {
            amount >= 1_000_000 -> {
                val value = amount / 1_000_000
                val formatted = if (value == value.toLong().toDouble()) {
                    value.toLong().toString()
                } else {
                    String.format("%.1f", value)
                }
                if (currencyCode == "VES") "${formatted}M Bs" else "$symbol${formatted}M"
            }
            amount >= 1_000 -> {
                val value = amount / 1_000
                val formatted = if (value == value.toLong().toDouble()) {
                    value.toLong().toString()
                } else {
                    String.format("%.1f", value)
                }
                if (currencyCode == "VES") "${formatted}K Bs" else "$symbol${formatted}K"
            }
            else -> format(amount, currencyCode)
        }
    }

    private fun formatVenezuelan(amount: Double): String {
        val pattern = "#,##0.00"
        val df = DecimalFormat(pattern, venezuelanSymbols)
        return df.format(amount)
    }

    private fun formatStandard(amount: Double): String {
        val pattern = "#,##0.00"
        val df = DecimalFormat(pattern, usSymbols)
        return df.format(amount)
    }
}
