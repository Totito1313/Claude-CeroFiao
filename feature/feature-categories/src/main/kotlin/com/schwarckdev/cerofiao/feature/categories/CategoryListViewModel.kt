package com.schwarckdev.cerofiao.feature.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.schwarckdev.cerofiao.core.model.CategoryNode

data class CategoryListUiState(
    val expenseCategories: List<CategoryNode> = emptyList(),
    val incomeCategories: List<CategoryNode> = emptyList(),
    val categoryTotals: Map<String, Double> = emptyMap(),
)

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesUseCase,
    transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val monthRange = DateUtils.getCurrentMonthRange()

    val uiState: StateFlow<CategoryListUiState> = combine(
        getCategoriesUseCase(),
        transactionRepository.getCategoryTotalsForPeriod(monthRange.first, monthRange.second),
    ) { categories, totals ->
        val expenseNodes = buildCategoryNodes(categories.filter { it.type == CategoryType.EXPENSE })
        val incomeNodes = buildCategoryNodes(categories.filter { it.type == CategoryType.INCOME })
        
        CategoryListUiState(
            expenseCategories = expenseNodes,
            incomeCategories = incomeNodes,
            categoryTotals = totals,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CategoryListUiState(),
    )

    private fun buildCategoryNodes(categories: List<Category>): List<CategoryNode> {
        val rootCategories = categories.filter { it.parentId == null }.sortedBy { it.sortOrder }
        return rootCategories.map { root ->
            val subs = categories.filter { it.parentId == root.id }.sortedBy { it.sortOrder }
            CategoryNode(category = root, subcategories = subs)
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(id)
        }
    }
}
