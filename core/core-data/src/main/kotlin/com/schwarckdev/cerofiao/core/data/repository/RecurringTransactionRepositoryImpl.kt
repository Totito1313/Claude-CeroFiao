package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.database.dao.RecurringTransactionDao
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.RecurringTransactionRepository
import com.schwarckdev.cerofiao.core.model.RecurringTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurringTransactionRepositoryImpl @Inject constructor(
    private val dao: RecurringTransactionDao,
) : RecurringTransactionRepository {

    override fun getAllActive(): Flow<List<RecurringTransaction>> =
        dao.getAllActive().map { list -> list.map { it.toModel() } }

    override suspend fun getDueTransactions(now: Long): List<RecurringTransaction> =
        dao.getDueTransactions(now).map { it.toModel() }

    override suspend fun getById(id: String): RecurringTransaction? =
        dao.getById(id)?.toModel()

    override suspend fun insert(recurringTransaction: RecurringTransaction) {
        dao.insert(recurringTransaction.toEntity())
    }

    override suspend fun updateNextDueDate(id: String, nextDueDate: Long) {
        dao.updateNextDueDate(id, nextDueDate, DateUtils.now())
    }

    override suspend fun setActive(id: String, isActive: Boolean) {
        dao.setActive(id, isActive, DateUtils.now())
    }

    override suspend fun delete(id: String) {
        dao.softDelete(id, DateUtils.now())
    }
}
