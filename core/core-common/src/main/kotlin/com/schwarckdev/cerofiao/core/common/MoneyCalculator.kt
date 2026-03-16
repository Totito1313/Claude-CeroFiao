package com.schwarckdev.cerofiao.core.common

import java.math.BigDecimal
import java.math.RoundingMode

object MoneyCalculator {

    fun convert(amount: Double, rate: Double): Double {
        return BigDecimal.valueOf(amount)
            .multiply(BigDecimal.valueOf(rate))
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }

    fun toUsd(amount: Double, currencyCode: String, rateToUsd: Double): Double {
        return if (currencyCode == "USD") amount
        else convert(amount, rateToUsd)
    }

    fun fromUsd(amountUsd: Double, currencyCode: String, rateFromUsd: Double): Double {
        return if (currencyCode == "USD") amountUsd
        else convert(amountUsd, rateFromUsd)
    }

    fun applyCommission(amount: Double, commissionPercent: Double): Double {
        val commission = BigDecimal.valueOf(amount)
            .multiply(BigDecimal.valueOf(commissionPercent / 100.0))
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
        return amount - commission
    }

    fun calculateCommission(amount: Double, commissionPercent: Double): Double {
        return BigDecimal.valueOf(amount)
            .multiply(BigDecimal.valueOf(commissionPercent / 100.0))
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }
}
