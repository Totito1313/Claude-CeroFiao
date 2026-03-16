package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class DebtType {
    I_OWE,
    THEY_OWE,
}

@Serializable
data class Debt(
    val id: String,
    val personName: String,
    val personPhone: String? = null,
    val type: DebtType,
    val originalAmount: Double,
    val currencyCode: String,
    val remainingAmount: Double,
    val exchangeRateToUsdAtCreation: Double = 1.0,
    val note: String? = null,
    val dueDate: Long? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isSettled: Boolean = false,
    val settledAt: Long? = null,
)

@Serializable
data class DebtPayment(
    val id: String,
    val debtId: String,
    val transactionId: String? = null,
    val amount: Double,
    val currencyCode: String,
    val exchangeRateToUsd: Double = 1.0,
    val paidAt: Long = 0L,
    val note: String? = null,
)
