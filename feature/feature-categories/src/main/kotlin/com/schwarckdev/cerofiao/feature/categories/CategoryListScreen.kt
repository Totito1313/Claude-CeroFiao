package com.schwarckdev.cerofiao.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    onBack: () -> Unit = {},
    onAddCategory: () -> Unit = {},
    onEditCategory: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CategoryListViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Top bar row with back button, title, and FAB-style add button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = t.iconBg,
                    modifier = Modifier.size(40.dp),
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            CeroFiaoIcons.Back,
                            contentDescription = "Volver",
                            tint = t.text,
                        )
                    }
                }
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = t.text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                )
                FloatingActionButton(
                    onClick = onAddCategory,
                    modifier = Modifier.size(40.dp),
                    containerColor = Color(0xFF8A2BE2),
                ) {
                    Icon(
                        CeroFiaoIcons.Add,
                        contentDescription = "Agregar categoría",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        if (uiState.expenseCategories.isNotEmpty()) {
            item {
                Text(
                    text = "Gastos",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF8A2BE2),
                )
            }
            items(uiState.expenseCategories, key = { it.id }) { category ->
                SwipeToDeleteCategoryRow(
                    category = category,
                    totalUsd = uiState.categoryTotals[category.id],
                    onClick = { onEditCategory(category.id) },
                    onDelete = { viewModel.deleteCategory(category.id) },
                )
            }
        }

        if (uiState.incomeCategories.isNotEmpty()) {
            item {
                Text(
                    text = "Ingresos",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF8A2BE2),
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            items(uiState.incomeCategories, key = { it.id }) { category ->
                SwipeToDeleteCategoryRow(
                    category = category,
                    totalUsd = uiState.categoryTotals[category.id],
                    onClick = { onEditCategory(category.id) },
                    onDelete = { viewModel.deleteCategory(category.id) },
                )
            }
        }

        item {
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.padding(bottom = 100.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteCategoryRow(
    category: Category,
    totalUsd: Double?,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                color = t.danger.copy(alpha = 0.15f),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        CeroFiaoIcons.Delete,
                        contentDescription = "Eliminar",
                        tint = t.danger,
                    )
                }
            }
        },
        enableDismissFromStartToEnd = false,
    ) {
        CategoryRow(
            category = category,
            totalUsd = totalUsd,
            onClick = onClick,
        )
    }
}

@Composable
private fun CategoryRow(
    category: Category,
    totalUsd: Double?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val iconColor = try {
        Color(android.graphics.Color.parseColor(category.colorHex))
    } catch (_: Exception) {
        Color(0xFF8A2BE2)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = t.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = iconColor.copy(alpha = 0.12f),
            ) {
                Icon(
                    painter = painterResource(CeroFiaoIcons.getCategoryIconRes(category.iconName)),
                    contentDescription = category.name,
                    tint = iconColor,
                    modifier = Modifier.padding(8.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = t.text,
                )
                if (totalUsd != null && totalUsd != 0.0) {
                    val absAmount = kotlin.math.abs(totalUsd)
                    Text(
                        text = "${CurrencyFormatter.format(absAmount, "USD")} este mes",
                        style = MaterialTheme.typography.bodySmall,
                        color = t.textSecondary,
                    )
                }
            }
        }
    }
}
