package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.TransactionTitle
import kotlinx.coroutines.flow.Flow

interface TransactionTitleRepository {
    fun getAll(): Flow<List<TransactionTitle>>
    suspend fun findMatch(query: String): TransactionTitle?
    suspend fun insert(title: TransactionTitle)
    suspend fun delete(id: String)
}
