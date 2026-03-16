package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.schwarckdev.cerofiao.core.database.entity.DebtEntity
import com.schwarckdev.cerofiao.core.database.entity.DebtPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {

    @Query("SELECT * FROM debt WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAllDebts(): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debt WHERE type = :type AND isDeleted = 0 AND isSettled = 0 ORDER BY createdAt DESC")
    fun getActiveDebtsByType(type: String): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debt WHERE id = :id AND isDeleted = 0")
    fun getDebtById(id: String): Flow<DebtEntity?>

    @Query("SELECT * FROM debt WHERE isDeleted = 0 AND isSettled = 0 AND dueDate IS NOT NULL AND dueDate <= :timestamp")
    suspend fun getOverdueDebts(timestamp: Long): List<DebtEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(debt: DebtEntity)

    @Update
    suspend fun update(debt: DebtEntity)

    @Query("UPDATE debt SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    // Payments
    @Query("SELECT * FROM debt_payment WHERE debtId = :debtId AND isDeleted = 0 ORDER BY paidAt DESC")
    fun getPaymentsForDebt(debtId: String): Flow<List<DebtPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: DebtPaymentEntity)
}
