package com.schwarckdev.cerofiao.feature.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryType
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditCategoryUiState(
    val name: String = "",
    val type: CategoryType = CategoryType.EXPENSE,
    val iconName: String = "Food",
    val colorHex: String = "#FF5722",
    val parentId: String? = null,
    val parentCategories: List<Category> = emptyList(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
)

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<AddEditCategoryRoute>()
    private val editCategoryId: String? = route.categoryId

    private data class FormState(
        val name: String = "",
        val type: CategoryType = CategoryType.EXPENSE,
        val iconName: String = "Food",
        val colorHex: String = "#FF5722",
        val parentId: String? = null,
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
    )

    private val formState = MutableStateFlow(FormState())

    val uiState: StateFlow<AddEditCategoryUiState> = combine(
        formState,
        getCategoriesUseCase(),
    ) { form, categories ->
        val eligibleParents = categories.filter { 
            it.type == form.type && 
            it.id != editCategoryId && 
            it.parentId == null 
        }

        AddEditCategoryUiState(
            name = form.name,
            type = form.type,
            iconName = form.iconName,
            colorHex = form.colorHex,
            parentId = form.parentId,
            parentCategories = eligibleParents,
            isSaving = form.isSaving,
            isSaved = form.isSaved,
            isEditMode = editCategoryId != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddEditCategoryUiState(isEditMode = editCategoryId != null),
    )

    init {
        if (editCategoryId != null) {
            loadCategory(editCategoryId)
        }
    }

    private fun loadCategory(id: String) {
        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(id) ?: return@launch
            formState.update {
                it.copy(
                    name = category.name,
                    type = category.type,
                    iconName = category.iconName,
                    colorHex = category.colorHex,
                    parentId = category.parentId,
                )
            }
        }
    }

    fun setName(name: String) {
        formState.update { it.copy(name = name) }
    }

    fun setType(type: CategoryType) {
        formState.update { it.copy(type = type, parentId = null) }
    }

    fun setIcon(iconName: String) {
        formState.update { it.copy(iconName = iconName) }
    }

    fun setColor(colorHex: String) {
        formState.update { it.copy(colorHex = colorHex) }
    }

    fun setParent(parentId: String?) {
        formState.update { it.copy(parentId = parentId) }
    }

    fun save() {
        val state = formState.value
        if (state.name.isBlank()) return

        viewModelScope.launch {
            formState.update { it.copy(isSaving = true) }
            if (editCategoryId != null) {
                val existing = categoryRepository.getCategoryById(editCategoryId)
                if (existing != null) {
                    categoryRepository.updateCategory(
                        existing.copy(
                            name = state.name.trim(),
                            type = state.type,
                            iconName = state.iconName,
                            colorHex = state.colorHex,
                            parentId = state.parentId,
                        ),
                    )
                }
            } else {
                categoryRepository.createCategory(
                    Category(
                        id = UuidGenerator.generate(),
                        name = state.name.trim(),
                        type = state.type,
                        iconName = state.iconName,
                        colorHex = state.colorHex,
                        parentId = state.parentId,
                        isDefault = false,
                        sortOrder = 99,
                    ),
                )
            }
            formState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
