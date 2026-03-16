package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.schwarckdev.cerofiao.core.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category WHERE isDeleted = 0 AND isActive = 1 ORDER BY sortOrder ASC")
    fun getActiveCategories(): Flow<List<CategoryEntity>>

    @Query(
        "SELECT * FROM category WHERE type = :type AND isDeleted = 0 AND isActive = 1 " +
            "ORDER BY sortOrder ASC"
    )
    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE id = :id AND isDeleted = 0")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Query("SELECT * FROM category WHERE parentId = :parentId AND isDeleted = 0 AND isActive = 1")
    fun getSubcategories(parentId: String): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("UPDATE category SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: String)
}
