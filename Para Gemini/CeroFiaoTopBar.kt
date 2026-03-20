package com.SchwarckDev.cerofiao.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import com.SchwarckDev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CeroFiaoTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = LocalCeroFiaoColors.current.TextPrimary
            )
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = LocalCeroFiaoColors.current.Background,
            scrolledContainerColor = LocalCeroFiaoColors.current.Surface,
            navigationIconContentColor = LocalCeroFiaoColors.current.TextPrimary,
            titleContentColor = LocalCeroFiaoColors.current.TextPrimary,
            actionIconContentColor = LocalCeroFiaoColors.current.TextPrimary,
        )
    )
}
