package com.SchwarckDev.CeroFiao.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MoreScreen(
    onNavigateToAccounts: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToAlcancia: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToBillSplitter: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Más",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
