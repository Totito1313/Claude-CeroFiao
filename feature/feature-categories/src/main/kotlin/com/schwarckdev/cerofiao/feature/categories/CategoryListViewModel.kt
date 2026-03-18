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

data class CategoryListUiState(
    val expenseCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList(),
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
        CategoryListUiState(
            expenseCategories = categories.filter { it.type == CategoryType.EXPENSE },
            incomeCategories = categories.filter { it.type == CategoryType.INCOME },
            categoryTotals = totals,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CategoryListUiState(),
    )

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(id)
        }
    }
}
