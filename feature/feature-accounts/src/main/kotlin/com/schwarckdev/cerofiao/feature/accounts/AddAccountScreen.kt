package com.schwarckdev.cerofiao.feature.accounts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant

@Composable
fun AddAccountScreen(
    onBack: () -> Unit,
    onAccountCreated: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConfigureTopBar(variant = TopBarVariant.Detail, title = "Nueva Cuenta", onBackClick = onBack)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "AddAccount",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
