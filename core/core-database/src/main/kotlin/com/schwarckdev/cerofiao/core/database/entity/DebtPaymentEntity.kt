package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "debt_payment",
    foreignKeys = [
        ForeignKey(
            entity = DebtEntity::class,
            parentColumns = ["id"],
            childColumns = ["debtId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("debtId")],
)
data class DebtPaymentEntity(
    @PrimaryKey val id: String,
    val debtId: String,
    val transactionId: String?,
    val amount: Double,
    val currencyCode: String,
    val exchangeRateToUsd: Double,
    val paidAt: Long,
    val note: String?,
    val syncId: String?,
    val isDeleted: Boolean,
)
