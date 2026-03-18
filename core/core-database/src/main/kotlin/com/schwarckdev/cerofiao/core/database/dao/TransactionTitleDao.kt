package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.schwarckdev.cerofiao.core.database.entity.TransactionTitleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionTitleDao {

    @Query("SELECT * FROM transaction_titles WHERE isDeleted = 0 ORDER BY title ASC")
    fun getAll(): Flow<List<TransactionTitleEntity>>

    @Query(
        "SELECT * FROM transaction_titles WHERE isDeleted = 0 " +
            "AND (isExactMatch = 1 AND title = :query OR isExactMatch = 0 AND title LIKE '%' || :query || '%') " +
            "LIMIT 1",
    )
    suspend fun findMatch(query: String): TransactionTitleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TransactionTitleEntity)

    @Query("UPDATE transaction_titles SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: String)
}
