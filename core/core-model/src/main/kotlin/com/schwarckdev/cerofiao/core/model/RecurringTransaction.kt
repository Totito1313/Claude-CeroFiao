package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class RecurrenceType {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
}

@Serializable
data class RecurringTransaction(
    val id: String,
    val title: String,
    val amount: Double,
    val currencyCode: String,
    val categoryId: String?,
    val accountId: String,
    val type: TransactionType,
    val recurrence: RecurrenceType,
    val periodLength: Int = 1,
    val startDate: Long,
    val endDate: Long? = null,
    val nextDueDate: Long,
    val isActive: Boolean = true,
    val note: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
