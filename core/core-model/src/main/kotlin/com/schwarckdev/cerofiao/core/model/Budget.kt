package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class BudgetPeriod {
    WEEKLY,
    BIWEEKLY,
    MONTHLY,
}

@Serializable
data class Budget(
    val id: String,
    val name: String,
    val limitAmount: Double,
    val anchorCurrencyCode: String,
    val period: BudgetPeriod,
    val categoryId: String? = null,
    val startDate: String,
    val isRecurring: Boolean = true,
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
