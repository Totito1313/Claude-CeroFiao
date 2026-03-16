package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.database.dao.CategoryDao
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override fun getActiveCategories(): Flow<List<Category>> =
        categoryDao.getActiveCategories().map { list -> list.map { it.toModel() } }

    override fun getCategoriesByType(type: String): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type).map { list -> list.map { it.toModel() } }

    override suspend fun getCategoryById(id: String): Category? =
        categoryDao.getCategoryById(id)?.toModel()

    override suspend fun createCategory(category: Category) {
        categoryDao.insert(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun deleteCategory(id: String) {
        categoryDao.softDelete(id)
    }
}
