package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debt")
data class DebtEntity(
    @PrimaryKey val id: String,
    val personName: String,
    val personPhone: String?,
    val type: String,
    val originalAmount: Double,
    val currencyCode: String,
    val remainingAmount: Double,
    val exchangeRateToUsdAtCreation: Double,
    val note: String?,
    val dueDate: Long?,
    val createdAt: Long,
    val updatedAt: Long,
    val isSettled: Boolean,
    val settledAt: Long?,
    val syncId: String?,
    val isDeleted: Boolean,
)
