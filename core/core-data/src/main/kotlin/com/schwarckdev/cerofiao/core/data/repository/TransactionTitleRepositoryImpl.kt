package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.database.dao.TransactionTitleDao
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.TransactionTitleRepository
import com.schwarckdev.cerofiao.core.model.TransactionTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionTitleRepositoryImpl @Inject constructor(
    private val dao: TransactionTitleDao,
) : TransactionTitleRepository {

    override fun getAll(): Flow<List<TransactionTitle>> =
        dao.getAll().map { list -> list.map { it.toModel() } }

    override suspend fun findMatch(query: String): TransactionTitle? =
        dao.findMatch(query)?.toModel()

    override suspend fun insert(title: TransactionTitle) {
        dao.insert(title.toEntity())
    }

    override suspend fun delete(id: String) {
        dao.softDelete(id)
    }
}
