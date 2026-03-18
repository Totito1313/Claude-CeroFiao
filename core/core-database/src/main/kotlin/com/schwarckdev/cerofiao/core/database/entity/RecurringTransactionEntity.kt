package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recurring_transactions",
    indices = [
        Index("nextDueDate"),
        Index("isActive"),
    ],
)
data class RecurringTransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Double,
    val currencyCode: String,
    val categoryId: String?,
    val accountId: String,
    val type: String,
    val recurrence: String,
    val periodLength: Int,
    val startDate: Long,
    val endDate: Long?,
    val nextDueDate: Long,
    val isActive: Boolean,
    val note: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncId: String?,
    val isDeleted: Boolean,
)
