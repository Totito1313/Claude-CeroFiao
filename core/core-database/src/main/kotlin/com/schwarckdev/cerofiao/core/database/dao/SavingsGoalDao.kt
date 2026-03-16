package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.schwarckdev.cerofiao.core.database.entity.SavingsContributionEntity
import com.schwarckdev.cerofiao.core.database.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsGoalDao {

    @Query("SELECT * FROM savings_goal WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<SavingsGoalEntity>>

    @Query("SELECT * FROM savings_goal WHERE isDeleted = 0 AND isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveGoals(): Flow<List<SavingsGoalEntity>>

    @Query("SELECT * FROM savings_goal WHERE id = :id AND isDeleted = 0")
    fun getGoalById(id: String): Flow<SavingsGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: SavingsGoalEntity)

    @Update
    suspend fun update(goal: SavingsGoalEntity)

    @Query("UPDATE savings_goal SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Query("UPDATE savings_goal SET currentAmountInUsd = :amount, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateCurrentAmount(id: String, amount: Double, updatedAt: Long)

    // Contributions
    @Query("SELECT * FROM savings_contribution WHERE goalId = :goalId AND isDeleted = 0 ORDER BY contributedAt DESC")
    fun getContributionsForGoal(goalId: String): Flow<List<SavingsContributionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: SavingsContributionEntity)
}
