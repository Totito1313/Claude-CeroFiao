package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant

@Composable
fun ExchangeRateScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ExchangeRateViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Standard, title = "Tasas de Cambio")

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "ExchangeRate",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
