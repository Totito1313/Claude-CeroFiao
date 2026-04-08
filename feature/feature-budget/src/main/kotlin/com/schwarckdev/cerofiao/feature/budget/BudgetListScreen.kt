package com.schwarckdev.cerofiao.feature.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.Wallet
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.BudgetPeriod
import com.schwarckdev.cerofiao.core.ui.CeroFiaoFAB
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
    ConfigureTopBar(variant = TopBarVariant.Detail, title = "Presupuestos", onBackClick = onBack)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background),
    ) {
        if (!uiState.isLoading && uiState.budgets.isEmpty()) {
            EmptyState(
                icon = Lucide.Wallet,
                title = "Sin presupuestos",
                description = "Crea tu primer presupuesto para controlar tus gastos",
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 70.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
            ) {
                itemsIndexed(
                    items = uiState.budgets,
                    key = { _, item -> item.budget.id },
                ) { index, budgetWithProgress ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { visible = true }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                            slideInVertically(
                                initialOffsetY = { it / 3 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow,
                                ),
                            ),
                    ) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.deleteBudget(budgetWithProgress.budget.id)
                                    true
                                } else {
                                    false
                                }
                            },
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(colors.ExpenseColor),
                                    contentAlignment = Alignment.CenterEnd,
                                ) {
                                    Row(
                                        modifier = Modifier.padding(end = 20.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Text(
                                            text = "Eliminar",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                        )
                                        Icon(
                                            imageVector = Lucide.Trash2,
                                            contentDescription = "Eliminar",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp),
                                        )
                                    }
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
        }

        CeroFiaoFAB(
            onClick = onAddBudget,
            icon = Lucide.Plus,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 100.dp),
        )
    }
}

@Composable
private fun BudgetCard(
    budgetWithProgress: BudgetWithProgress,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val budget = budgetWithProgress.budget
    val progress = budgetWithProgress.progress
    val isOverBudget = progress > 1f

    val progressColor = when {
        isOverBudget -> colors.ExpenseColor
        progress > 0.8f -> colors.Warning
        else -> colors.Primary
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.Foreground,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Header row: name + category badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = budget.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.TextPrimary,
                    )
                    val categoryName = budgetWithProgress.category?.name
                    if (categoryName != null) {
                        Text(
                            text = categoryName,
                            fontSize = 13.sp,
                            color = colors.TextSecondary,
                        )
                    }
                }

                // Period badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.SurfaceVariant,
                ) {
                    Text(
                        text = when (budget.period) {
                            BudgetPeriod.WEEKLY -> "Semanal"
                            BudgetPeriod.BIWEEKLY -> "Quincenal"
                            BudgetPeriod.MONTHLY -> "Mensual"
                        },
                        fontSize = 12.sp,
                        color = colors.TextSecondary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }

            // Progress bar
            LinearProgressIndicator(
                progress = { progress.coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = progressColor,
                trackColor = colors.SurfaceVariant,
                strokeCap = StrokeCap.Round,
            )

            // Amount row: spent / limit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = CurrencyFormatter.format(
                        budgetWithProgress.spentAmount,
                        budgetWithProgress.currencyCode,
                    ),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isOverBudget) colors.ExpenseColor else colors.TextPrimary,
                )
                Text(
                    text = "de ${CurrencyFormatter.format(
                        budgetWithProgress.limitAmount,
                        budgetWithProgress.currencyCode,
                    )}",
                    fontSize = 14.sp,
                    color = colors.TextSecondary,
                )
            }

            // Over-budget warning
            if (isOverBudget) {
                val overAmount = budgetWithProgress.spentAmount - budgetWithProgress.limitAmount
                Text(
                    text = "Excedido por ${CurrencyFormatter.format(
                        overAmount,
                        budgetWithProgress.currencyCode,
                    )}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.ExpenseColor,
                )
            }
        }
    }
}
