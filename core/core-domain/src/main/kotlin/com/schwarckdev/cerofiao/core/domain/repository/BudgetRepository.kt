package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getActiveBudgets(): Flow<List<Budget>>
    fun getAllBudgets(): Flow<List<Budget>>
    fun getBudgetById(id: String): Flow<Budget?>
    suspend fun getBudgetForCategory(categoryId: String): Budget?
    suspend fun insertBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(id: String)
}
