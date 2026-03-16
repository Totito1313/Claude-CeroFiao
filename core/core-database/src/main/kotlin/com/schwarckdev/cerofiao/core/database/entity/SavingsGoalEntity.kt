package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goal")
data class SavingsGoalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val targetAmount: Double,
    val currencyCode: String,
    val currentAmountInUsd: Double,
    val iconName: String?,
    val colorHex: String?,
    val deadline: Long?,
    val createdAt: Long,
    val updatedAt: Long,
    val isCompleted: Boolean,
    val completedAt: Long?,
    val syncId: String?,
    val isDeleted: Boolean,
)
