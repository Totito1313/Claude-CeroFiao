package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index("accountId"),
        Index("categoryId"),
        Index("date"),
        Index("type"),
    ],
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    val type: String,
    val amount: Double,
    val currencyCode: String,
    val accountId: String,
    val categoryId: String?,
    val note: String?,
    val date: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val exchangeRateToUsd: Double,
    val exchangeRateSource: String?,
    val amountInUsd: Double,
    val transferLinkedId: String?,
    val transferToAccountId: String?,
    val transferCommission: Double?,
    val transferCommissionCurrency: String?,
    val receiptImagePath: String?,
    val smsSourceId: String?,
    val syncId: String?,
    val isDeleted: Boolean,
)
