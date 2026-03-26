package com.schwarckdev.cerofiao.feature.goals

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
fun AddEditGoalScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AddEditGoalViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Detail, title = "Meta", onBackClick = onBack)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "AddEditGoal",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
