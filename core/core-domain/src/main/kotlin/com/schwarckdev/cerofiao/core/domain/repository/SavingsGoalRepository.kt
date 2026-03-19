package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.SavingsContribution
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

interface SavingsGoalRepository {
    fun getAllGoals(): Flow<List<SavingsGoal>>
    fun getActiveGoals(): Flow<List<SavingsGoal>>
    fun getGoalById(id: String): Flow<SavingsGoal?>
    suspend fun insertGoal(goal: SavingsGoal)
    suspend fun updateGoal(goal: SavingsGoal)
    suspend fun deleteGoal(id: String)
    suspend fun updateCurrentAmount(id: String, amount: Double)
    
    fun getContributionsForGoal(goalId: String): Flow<List<SavingsContribution>>
    suspend fun insertContribution(contribution: SavingsContribution)
}
