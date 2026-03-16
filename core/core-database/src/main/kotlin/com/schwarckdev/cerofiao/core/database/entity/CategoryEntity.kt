package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val iconName: String,
    val colorHex: String,
    val parentId: String?,
    val isDefault: Boolean,
    val sortOrder: Int,
    val isActive: Boolean,
    val syncId: String?,
    val isDeleted: Boolean,
)
