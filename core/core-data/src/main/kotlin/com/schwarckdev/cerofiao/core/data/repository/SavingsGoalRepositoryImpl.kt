package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.database.dao.SavingsGoalDao
import com.schwarckdev.cerofiao.core.domain.repository.SavingsGoalRepository
import com.schwarckdev.cerofiao.core.model.SavingsContribution
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import com.schwarckdev.cerofiao.core.data.model.toEntity
import com.schwarckdev.cerofiao.core.data.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavingsGoalRepositoryImpl @Inject constructor(
    private val savingsGoalDao: SavingsGoalDao
) : SavingsGoalRepository {

    override fun getAllGoals(): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getAllGoals().map { list -> list.map { it.toModel() } }
    }

    override fun getActiveGoals(): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getActiveGoals().map { list -> list.map { it.toModel() } }
    }

    override fun getGoalById(id: String): Flow<SavingsGoal?> {
        return savingsGoalDao.getGoalById(id).map { it?.toModel() }
    }

    override suspend fun insertGoal(goal: SavingsGoal) {
        savingsGoalDao.insert(goal.toEntity())
    }

    override suspend fun updateGoal(goal: SavingsGoal) {
        savingsGoalDao.update(goal.toEntity().copy(updatedAt = DateUtils.now()))
    }

    override suspend fun deleteGoal(id: String) {
        savingsGoalDao.softDelete(id, DateUtils.now())
    }

    override suspend fun updateCurrentAmount(id: String, amount: Double) {
        savingsGoalDao.updateCurrentAmount(id, amount, DateUtils.now())
    }

    override fun getContributionsForGoal(goalId: String): Flow<List<SavingsContribution>> {
        return savingsGoalDao.getContributionsForGoal(goalId).map { list -> list.map { it.toModel() } }
    }

    override suspend fun insertContribution(contribution: SavingsContribution) {
        savingsGoalDao.insertContribution(contribution.toEntity())
    }
}
