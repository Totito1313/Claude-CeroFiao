package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction_logs",
    indices = [
        Index("transactionId"),
        Index("timestamp"),
    ],
)
data class TransactionLogEntity(
    @PrimaryKey val id: String,
    val transactionId: String,
    val action: String,
    val timestamp: Long,
    val snapshotJson: String,
)
