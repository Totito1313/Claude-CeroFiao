package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.database.dao.BudgetDao
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.BudgetRepository
import com.schwarckdev.cerofiao.core.model.Budget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
) : BudgetRepository {

    override fun getActiveBudgets(): Flow<List<Budget>> =
        budgetDao.getActiveBudgets().map { list -> list.map { it.toModel() } }

    override fun getAllBudgets(): Flow<List<Budget>> =
        budgetDao.getAllBudgets().map { list -> list.map { it.toModel() } }

    override fun getBudgetById(id: String): Flow<Budget?> =
        budgetDao.getBudgetById(id).map { it?.toModel() }

    override suspend fun getBudgetForCategory(categoryId: String): Budget? =
        budgetDao.getBudgetForCategory(categoryId)?.toModel()

    override suspend fun insertBudget(budget: Budget) {
        budgetDao.insert(budget.toEntity())
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.insert(budget.toEntity())
    }

    override suspend fun deleteBudget(id: String) {
        budgetDao.softDelete(id, DateUtils.now())
    }
}
