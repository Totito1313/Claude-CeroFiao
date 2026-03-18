package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.schwarckdev.cerofiao.core.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY date DESC, createdAt DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY date DESC, createdAt DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND isDeleted = 0 ORDER BY date DESC")
    fun getTransactionsByAccount(accountId: String): Flow<List<TransactionEntity>>

    @Query(
        "SELECT * FROM transactions WHERE categoryId = :categoryId AND isDeleted = 0 " +
            "AND date BETWEEN :startDate AND :endDate ORDER BY date DESC"
    )
    fun getTransactionsByCategoryAndDateRange(
        categoryId: String,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>

    @Query(
        "SELECT * FROM transactions WHERE isDeleted = 0 " +
            "AND date BETWEEN :startDate AND :endDate ORDER BY date DESC"
    )
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query(
        "SELECT * FROM transactions WHERE type = :type AND isDeleted = 0 " +
            "AND date BETWEEN :startDate AND :endDate ORDER BY date DESC"
    )
    fun getTransactionsByTypeAndDateRange(
        type: String,
        startDate: Long,
        endDate: Long,
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id AND isDeleted = 0")
    fun getTransactionById(id: String): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions WHERE id = :id AND isDeleted = 0")
    suspend fun getTransactionByIdOnce(id: String): TransactionEntity?

    @Query(
        "SELECT SUM(amountInUsd) FROM transactions WHERE type = 'EXPENSE' AND isDeleted = 0 " +
            "AND date BETWEEN :startDate AND :endDate"
    )
    fun getTotalExpensesInUsdForPeriod(startDate: Long, endDate: Long): Flow<Double?>

    @Query(
        "SELECT SUM(amountInUsd) FROM transactions WHERE type = 'INCOME' AND isDeleted = 0 " +
            "AND date BETWEEN :startDate AND :endDate"
    )
    fun getTotalIncomeInUsdForPeriod(startDate: Long, endDate: Long): Flow<Double?>

    @Query(
        "SELECT categoryId, SUM(amountInUsd) as total FROM transactions " +
            "WHERE type = 'EXPENSE' AND isDeleted = 0 AND categoryId IS NOT NULL " +
            "AND date BETWEEN :startDate AND :endDate " +
            "GROUP BY categoryId ORDER BY total DESC"
    )
    fun getExpensesByCategoryForPeriod(
        startDate: Long,
        endDate: Long,
    ): Flow<List<CategoryExpenseSum>>

    @Query(
        "SELECT categoryId, type, SUM(amountInUsd) as total FROM transactions " +
            "WHERE isDeleted = 0 AND categoryId IS NOT NULL " +
            "AND date BETWEEN :startDate AND :endDate " +
            "GROUP BY categoryId, type ORDER BY total DESC"
    )
    fun getCategoryTotalsForPeriod(
        startDate: Long,
        endDate: Long,
    ): Flow<List<CategoryTypeSum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Query("UPDATE transactions SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDelete(id: String, updatedAt: Long)

    @Transaction
    suspend fun insertTransferPair(outgoing: TransactionEntity, incoming: TransactionEntity) {
        insert(outgoing)
        insert(incoming)
    }
}

data class CategoryExpenseSum(
    val categoryId: String,
    val total: Double,
)

data class CategoryTypeSum(
    val categoryId: String,
    val type: String,
    val total: Double,
)
