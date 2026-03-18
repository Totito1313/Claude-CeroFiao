package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction_titles",
    indices = [
        Index("title"),
    ],
)
data class TransactionTitleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val categoryId: String,
    val isExactMatch: Boolean,
    val syncId: String?,
    val isDeleted: Boolean,
)
