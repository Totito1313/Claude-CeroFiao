package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val platform: String,
    val currencyCode: String,
    val balance: Double,
    val initialBalance: Double,
    val iconName: String?,
    val colorHex: String?,
    val isActive: Boolean,
    val includeInTotal: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val syncId: String?,
    val isDeleted: Boolean,
)
