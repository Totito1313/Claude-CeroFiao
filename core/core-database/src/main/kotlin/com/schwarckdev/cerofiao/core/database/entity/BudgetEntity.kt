package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val limitAmount: Double,
    val anchorCurrencyCode: String,
    val period: String,
    val categoryId: String?,
    val startDate: String,
    val isRecurring: Boolean,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val syncId: String?,
    val isDeleted: Boolean,
)
