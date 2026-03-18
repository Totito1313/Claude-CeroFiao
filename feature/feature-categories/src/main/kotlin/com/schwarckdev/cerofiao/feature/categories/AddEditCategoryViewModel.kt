package com.schwarckdev.cerofiao.feature.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditCategoryUiState(
    val name: String = "",
    val type: CategoryType = CategoryType.EXPENSE,
    val iconName: String = "Food",
    val colorHex: String = "#FF5722",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
)

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<AddEditCategoryRoute>()
    private val editCategoryId: String? = route.categoryId

    private val _uiState = MutableStateFlow(AddEditCategoryUiState(isEditMode = editCategoryId != null))
    val uiState: StateFlow<AddEditCategoryUiState> = _uiState

    init {
        if (editCategoryId != null) {
            loadCategory(editCategoryId)
        }
    }

    private fun loadCategory(id: String) {
        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(id) ?: return@launch
            _uiState.update {
                it.copy(
                    name = category.name,
                    type = category.type,
                    iconName = category.iconName,
                    colorHex = category.colorHex,
                )
            }
        }
    }

    fun setName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun setType(type: CategoryType) {
        _uiState.update { it.copy(type = type) }
    }

    fun setIcon(iconName: String) {
        _uiState.update { it.copy(iconName = iconName) }
    }

    fun setColor(colorHex: String) {
        _uiState.update { it.copy(colorHex = colorHex) }
    }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            if (editCategoryId != null) {
                val existing = categoryRepository.getCategoryById(editCategoryId)
                if (existing != null) {
                    categoryRepository.updateCategory(
                        existing.copy(
                            name = state.name.trim(),
                            type = state.type,
                            iconName = state.iconName,
                            colorHex = state.colorHex,
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
                        isDefault = false,
                        sortOrder = 99,
                    ),
                )
            }
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
