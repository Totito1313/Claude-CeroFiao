package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
data class SavingsGoal(
    val id: String,
    val name: String,
    val targetAmount: Double,
    val currencyCode: String,
    val currentAmountInUsd: Double = 0.0,
    val iconName: String? = null,
    val colorHex: String? = null,
    val deadline: Long? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
)

@Serializable
data class SavingsContribution(
    val id: String,
    val goalId: String,
    val transactionId: String? = null,
    val amount: Double,
    val currencyCode: String,
    val exchangeRateToUsd: Double = 1.0,
    val contributedAt: Long = 0L,
)
