package com.schwarckdev.cerofiao.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_metadata")
data class SyncMetadataEntity(
    @PrimaryKey val tableName: String,
    val lastSyncedAt: Long,
    val lastLocalChangeAt: Long,
)
