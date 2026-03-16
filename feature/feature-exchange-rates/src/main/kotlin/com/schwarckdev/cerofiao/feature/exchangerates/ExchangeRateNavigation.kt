package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ExchangeRateRoute

fun NavGraphBuilder.exchangeRateScreen(
    onBack: () -> Unit = {},
) {
    composable<ExchangeRateRoute> {
        ExchangeRateScreen(onBack = onBack)
    }
}

fun NavController.navigateToExchangeRates() {
    navigate(ExchangeRateRoute)
}
