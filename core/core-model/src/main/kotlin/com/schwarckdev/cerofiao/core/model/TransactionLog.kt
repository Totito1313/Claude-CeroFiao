package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionLogAction {
    CREATED,
    EDITED,
    DELETED,
}

@Serializable
data class TransactionLog(
    val id: String,
    val transactionId: String,
    val action: TransactionLogAction,
    val timestamp: Long,
    val snapshotJson: String,
)
