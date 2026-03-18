package com.schwarckdev.cerofiao.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionTitleRepository
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.TransactionTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssociatedTitlesUiState(
    val titles: List<TitleWithCategory> = emptyList(),
)

data class TitleWithCategory(
    val title: TransactionTitle,
    val categoryName: String,
)

@HiltViewModel
class AssociatedTitlesViewModel @Inject constructor(
    private val transactionTitleRepository: TransactionTitleRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    val uiState: StateFlow<AssociatedTitlesUiState> = combine(
        transactionTitleRepository.getAll(),
        categoryRepository.getActiveCategories(),
    ) { titles, categories ->
        val categoryMap = categories.associateBy { it.id }
        AssociatedTitlesUiState(
            titles = titles.map { title ->
                TitleWithCategory(
                    title = title,
                    categoryName = categoryMap[title.categoryId]?.name ?: "Sin categoría",
                )
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AssociatedTitlesUiState())

    fun delete(id: String) {
        viewModelScope.launch { transactionTitleRepository.delete(id) }
    }
}
