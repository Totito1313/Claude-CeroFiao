package com.schwarckdev.cerofiao.feature.billsplitter.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

import com.schwarckdev.cerofiao.feature.billsplitter.BillSplitterScreen

@Serializable
data object BillSplitterRoute

fun NavController.navigateToBillSplitter(navOptions: NavOptions? = null) {
    navigate(BillSplitterRoute, navOptions)
}

fun NavGraphBuilder.billSplitterScreen(onBack: () -> Unit) {
    composable<BillSplitterRoute> {
        BillSplitterScreen(onBack = onBack)
    }
}
