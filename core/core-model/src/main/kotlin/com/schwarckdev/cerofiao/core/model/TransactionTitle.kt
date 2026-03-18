package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionTitle(
    val id: String,
    val title: String,
    val categoryId: String,
    val isExactMatch: Boolean = false,
)
