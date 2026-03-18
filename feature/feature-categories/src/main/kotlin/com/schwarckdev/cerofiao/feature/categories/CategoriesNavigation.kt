package com.schwarckdev.cerofiao.feature.categories

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CategoryListRoute

@Serializable
data class AddEditCategoryRoute(val categoryId: String? = null)

fun NavGraphBuilder.categoryListScreen(
    onBack: () -> Unit = {},
    onAddCategory: () -> Unit = {},
    onEditCategory: (String) -> Unit = {},
) {
    composable<CategoryListRoute> {
        CategoryListScreen(
            onBack = onBack,
            onAddCategory = onAddCategory,
            onEditCategory = onEditCategory,
        )
    }
}

fun NavGraphBuilder.addEditCategoryScreen(
    onBack: () -> Unit = {},
    onSaved: () -> Unit = {},
) {
    composable<AddEditCategoryRoute> {
        AddEditCategoryScreen(
            onBack = onBack,
            onSaved = onSaved,
        )
    }
}

fun NavController.navigateToCategories() {
    navigate(CategoryListRoute)
}

fun NavController.navigateToAddEditCategory(categoryId: String? = null) {
    navigate(AddEditCategoryRoute(categoryId = categoryId))
}
