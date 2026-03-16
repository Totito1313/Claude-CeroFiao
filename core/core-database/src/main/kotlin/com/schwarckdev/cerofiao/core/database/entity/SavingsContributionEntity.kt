package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "savings_contribution",
    foreignKeys = [
        ForeignKey(
            entity = SavingsGoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("goalId")],
)
data class SavingsContributionEntity(
    @PrimaryKey val id: String,
    val goalId: String,
    val transactionId: String?,
    val amount: Double,
    val currencyCode: String,
    val exchangeRateToUsd: Double,
    val contributedAt: Long,
    val syncId: String?,
    val isDeleted: Boolean,
)
