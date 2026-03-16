package com.schwarckdev.cerofiao.feature.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CategoryListUiState(
    val expenseCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList(),
)

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    val uiState: StateFlow<CategoryListUiState> = getCategoriesUseCase()
        .map { categories ->
            CategoryListUiState(
                expenseCategories = categories.filter { it.type == CategoryType.EXPENSE },
                incomeCategories = categories.filter { it.type == CategoryType.INCOME },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CategoryListUiState(),
        )
}
