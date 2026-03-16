package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val code: String,
    val name: String,
    val symbol: String,
    val decimalPlaces: Int,
    val isActive: Boolean,
    val sortOrder: Int,
)
