package com.schwarckdev.cerofiao.feature.accounts

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
fun AccountListScreen(
    onAccountClick: (String) -> Unit,
    onAddAccount: () -> Unit,
    onTransfer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountListViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Standard, title = "Cuentas")

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "AccountList",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
