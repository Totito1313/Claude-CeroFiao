package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.schwarckdev.cerofiao.core.database.entity.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {

    @Query("SELECT * FROM recurring_transactions WHERE isDeleted = 0 ORDER BY nextDueDate ASC")
    fun getAllActive(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE isDeleted = 0 AND isActive = 1 AND nextDueDate <= :now")
    suspend fun getDueTransactions(now: Long): List<RecurringTransactionEntity>

    @Query("SELECT * FROM recurring_transactions WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: String): RecurringTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RecurringTransactionEntity)

    @Query("UPDATE recurring_transactions SET nextDueDate = :nextDueDate, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateNextDueDate(id: String, nextDueDate: Long, updatedAt: Long)

    @Query("UPDATE recurring_transactions SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setActive(id: String, isActive: Boolean, updatedAt: Long)

    @Query("UPDATE recurring_transactions SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)
}
