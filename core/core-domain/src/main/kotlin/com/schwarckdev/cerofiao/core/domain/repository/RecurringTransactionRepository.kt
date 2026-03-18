package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.RecurringTransaction
import kotlinx.coroutines.flow.Flow

interface RecurringTransactionRepository {
    fun getAllActive(): Flow<List<RecurringTransaction>>
    suspend fun getDueTransactions(now: Long): List<RecurringTransaction>
    suspend fun getById(id: String): RecurringTransaction?
    suspend fun insert(recurringTransaction: RecurringTransaction)
    suspend fun updateNextDueDate(id: String, nextDueDate: Long)
    suspend fun setActive(id: String, isActive: Boolean)
    suspend fun delete(id: String)
}
