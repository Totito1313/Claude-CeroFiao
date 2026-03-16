package com.schwarckdev.cerofiao.feature.categories

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CategoryListRoute

fun NavGraphBuilder.categoryListScreen() {
    composable<CategoryListRoute> {
        CategoryListScreen()
    }
}

fun NavController.navigateToCategories() {
    navigate(CategoryListRoute)
}
