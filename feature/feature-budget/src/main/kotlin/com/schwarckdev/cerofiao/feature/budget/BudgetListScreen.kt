package com.schwarckdev.cerofiao.feature.budget
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import com.schwarckdev.cerofiao.core.ui.CeroFiaoFAB
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.BudgetPeriod
import com.schwarckdev.cerofiao.core.ui.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    onBack: () -> Unit,
    onAddBudget: () -> Unit,
    onEditBudget: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BudgetListViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).statusBarsPadding(),
    ) {
        // Back button
        Surface(
            shape = CircleShape,
            color = t.iconBg,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .align(Alignment.TopStart),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = CeroFiaoIcons.Back,
                    contentDescription = "Volver",
                    tint = t.text,
                )
            }
        }

        // Title
        Text(
            text = "Presupuestos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = t.text,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
        )

        if (uiState.budgets.isEmpty() && !uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp),
                contentAlignment = Alignment.Center,
            ) {
                EmptyState(
                    icon = CeroFiaoIcons.Budget,
                    title = "Sin presupuestos",
                    description = "Crea un presupuesto para controlar tus gastos",
                    actionLabel = "Crear presupuesto",
                    onAction = onAddBudget,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = uiState.budgets,
                    key = { it.budget.id },
                ) { budgetWithProgress ->
                    val dismissState = rememberSwipeToDismissBoxState()

                    LaunchedEffect(dismissState.currentValue) {
                        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                            viewModel.deleteBudget(budgetWithProgress.budget.id)
                        }
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(t.danger.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Icon(
                                    imageVector = CeroFiaoIcons.Delete,
                                    contentDescription = "Eliminar",
                                    tint = t.danger,
                                    modifier = Modifier.padding(end = 16.dp),
                                )
                            }
                        },
                    ) {
                        BudgetCard(
                            budgetWithProgress = budgetWithProgress,
                            onClick = { onEditBudget(budgetWithProgress.budget.id) },
                        )
                    }
                }
            }
        }

        CeroFiaoFAB(
            icon = CeroFiaoIcons.Add,
            onClick = onAddBudget,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 116.dp),
            contentDescription = "Nuevo presupuesto"
        )
    }
}

@Composable
private fun BudgetCard(
    budgetWithProgress: BudgetWithProgress,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val budget = budgetWithProgress.budget
    val isOverBudget = budgetWithProgress.progress > 1f
    val progressColor = when {
        budgetWithProgress.progress > 1f -> Color(0xFFF44336)
        budgetWithProgress.progress > 0.8f -> Color(0xFFFF9800)
        else -> Color(0xFF4CAF50)
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = t.surface,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val category = budgetWithProgress.category
                    if (category != null) {
                        Icon(
                            painter = painterResource(CeroFiaoIcons.getCategoryIconRes(category.iconName)),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = t.text,
                        )
                    }
                    Text(
                        text = budget.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = t.text,
                    )
                }
                Text(
                    text = when (budget.period) {
                        BudgetPeriod.WEEKLY -> "Semanal"
                        BudgetPeriod.BIWEEKLY -> "Quincenal"
                        BudgetPeriod.MONTHLY -> "Mensual"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = t.textSecondary,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { budgetWithProgress.progress.coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = progressColor,
                trackColor = t.inputBg,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Gastado: ${CurrencyFormatter.format(budgetWithProgress.spentAmount, budgetWithProgress.currencyCode)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOverBudget) Color(0xFFF44336) else t.textSecondary,
                )
                Text(
                    text = "Límite: ${CurrencyFormatter.format(budgetWithProgress.limitAmount, budgetWithProgress.currencyCode)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = t.textSecondary,
                )
            }

            if (isOverBudget) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Excedido por ${CurrencyFormatter.format(budgetWithProgress.spentAmount - budgetWithProgress.limitAmount, budgetWithProgress.currencyCode)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFF44336),
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

