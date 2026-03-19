package com.schwarckdev.cerofiao.feature.budget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding

private enum class AnalyticsPeriod(val label: String) {
    WEEK("Semana"),
    MONTH("Mes"),
    YEAR("Año"),
}

@Composable
fun AnalyticsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BudgetListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens
    var selectedPeriod by remember { mutableStateOf(AnalyticsPeriod.MONTH) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Back button
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = t.iconBg,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    imageVector = CeroFiaoIcons.Back,
                    contentDescription = "Volver",
                    modifier = Modifier.size(20.dp),
                    tint = t.text,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title + subtitle
        Text(
            text = "Analytics",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = t.text,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Presupuestos y estad\u00EDsticas",
            fontSize = 13.sp,
            color = t.textMuted,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Period toggle
        PeriodSegmentedTabs(
            selectedPeriod = selectedPeriod,
            onPeriodChange = { selectedPeriod = it },
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Category Breakdown Card
        CategoryBreakdownCard(budgets = uiState.budgets)

        Spacer(modifier = Modifier.height(16.dp))

        // Budget Pacing Card
        BudgetPacingCard(budgets = uiState.budgets)

        // Bottom padding for nav bar
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun PeriodSegmentedTabs(
    selectedPeriod: AnalyticsPeriod,
    onPeriodChange: (AnalyticsPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CeroFiaoShapes.ChipRadius),
        color = t.pillBg,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            AnalyticsPeriod.entries.forEach { period ->
                val isSelected = selectedPeriod == period
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) t.surface else Color.Transparent,
                    animationSpec = tween(200),
                    label = "periodBg_${period.name}",
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) t.text else t.textMuted,
                    animationSpec = tween(200),
                    label = "periodText_${period.name}",
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(CeroFiaoShapes.ChipRadius - 4.dp))
                        .background(bgColor)
                        .clickable { onPeriodChange(period) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = period.label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = textColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownCard(
    budgets: List<BudgetWithProgress>,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val totalSpent = budgets.sumOf { it.spentAmount }

    // Group by category and sum spent amounts
    val categoryBreakdown = budgets
        .filter { it.category != null }
        .groupBy { it.category!! }
        .map { (category, items) ->
            category to items.sumOf { it.spentAmount }
        }
        .sortedByDescending { it.second }

    // Predefined color palette for category dots
    val dotColors = listOf(
        Color(0xFF8A2BE2),
        Color(0xFFFF6B00),
        Color(0xFF00FF66),
        Color(0xFFFF4433),
        Color(0xFF3B82F6),
        Color(0xFFFFD700),
        Color(0xFFFF69B4),
        Color(0xFF00CED1),
        Color(0xFF9B59B6),
        Color(0xFFE67E22),
    )

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        padding = GlassCardPadding.Large,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Gastos por Categor\u00EDa",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = t.text,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Display currency from first budget, fallback to USD
            val displayCurrency = budgets.firstOrNull()?.currencyCode ?: "USD"
            Text(
                text = "Total: ${CurrencyFormatter.format(totalSpent, displayCurrency)}",
                fontSize = 13.sp,
                color = t.textSecondary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (categoryBreakdown.isEmpty()) {
                Text(
                    text = "Sin datos de categor\u00EDas",
                    fontSize = 13.sp,
                    color = t.textMuted,
                )
            } else {
                categoryBreakdown.forEachIndexed { index, (category, amount) ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 1.dp,
                            color = t.divider,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Colored dot
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(dotColors[index % dotColors.size]),
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Category name
                        Text(
                            text = category.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = t.text,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Amount
                        Text(
                            text = CurrencyFormatter.format(
                                amount,
                                budgets.firstOrNull()?.currencyCode ?: "USD",
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = t.text,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BudgetPacingCard(
    budgets: List<BudgetWithProgress>,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        padding = GlassCardPadding.Large,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Presupuestos del Mes",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = t.text,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (budgets.isEmpty()) {
                Text(
                    text = "Sin presupuestos activos",
                    fontSize = 13.sp,
                    color = t.textMuted,
                )
            } else {
                budgets.forEachIndexed { index, budgetWithProgress ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    BudgetPacingItem(budgetWithProgress = budgetWithProgress)
                }
            }
        }
    }
}

@Composable
private fun BudgetPacingItem(
    budgetWithProgress: BudgetWithProgress,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    var isExpanded by remember { mutableStateOf(false) }

    val progressColor = when {
        budgetWithProgress.progress > 0.8f -> t.danger
        budgetWithProgress.progress > 0.5f -> Color(0xFFFF6B00) // orange warning
        else -> t.success
    }

    val animatedProgress by animateFloatAsState(
        targetValue = budgetWithProgress.progress.coerceAtMost(1f),
        animationSpec = tween(600),
        label = "budgetProgress",
    )

    val remaining = budgetWithProgress.limitAmount - budgetWithProgress.spentAmount
    val category = budgetWithProgress.category

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CeroFiaoShapes.SmallCardRadius))
            .background(t.pillBg)
            .clickable { isExpanded = !isExpanded }
            .padding(14.dp),
    ) {
        // Top row: icon + name + remaining | spent/limit
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Category icon
            if (category != null) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = progressColor.copy(alpha = 0.12f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(
                                CeroFiaoIcons.getCategoryIconRes(category.iconName),
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = progressColor,
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = progressColor.copy(alpha = 0.12f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = CeroFiaoIcons.Budget,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = progressColor,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Name + remaining
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category?.name ?: budgetWithProgress.budget.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = t.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (remaining >= 0) {
                        "Quedan ${CurrencyFormatter.format(remaining, budgetWithProgress.currencyCode)}"
                    } else {
                        "Excedido por ${CurrencyFormatter.format(-remaining, budgetWithProgress.currencyCode)}"
                    },
                    fontSize = 11.sp,
                    color = if (remaining >= 0) t.textMuted else t.danger,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Spent / Limit
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.format(
                        budgetWithProgress.spentAmount,
                        budgetWithProgress.currencyCode,
                    ),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = t.text,
                )
                Text(
                    text = "de ${CurrencyFormatter.format(budgetWithProgress.limitAmount, budgetWithProgress.currencyCode)}",
                    fontSize = 11.sp,
                    color = t.textMuted,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress bar (6dp height)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(t.progressBg),
        ) {
            if (animatedProgress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    progressColor,
                                    progressColor.copy(alpha = 0.7f),
                                ),
                            ),
                        )
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(3.dp),
                            ambientColor = progressColor.copy(alpha = 0.3f),
                            spotColor = progressColor.copy(alpha = 0.3f),
                        ),
                )
            }
        }

        // Expandable detail section
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300)),
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = t.divider,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Detail rows
                DetailRow(
                    label = "Gastado",
                    value = CurrencyFormatter.format(
                        budgetWithProgress.spentAmount,
                        budgetWithProgress.currencyCode,
                    ),
                    valueColor = t.text,
                )
                Spacer(modifier = Modifier.height(6.dp))

                DetailRow(
                    label = "Restante",
                    value = if (remaining >= 0) {
                        CurrencyFormatter.format(remaining, budgetWithProgress.currencyCode)
                    } else {
                        "-${CurrencyFormatter.format(-remaining, budgetWithProgress.currencyCode)}"
                    },
                    valueColor = if (remaining >= 0) t.success else t.danger,
                )
                Spacer(modifier = Modifier.height(6.dp))

                DetailRow(
                    label = "Progreso",
                    value = "${(budgetWithProgress.progress * 100).toInt()}%",
                    valueColor = progressColor,
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Daily pace: spent / days elapsed in month
                val daysInMonth = 30
                val dayOfMonth = java.time.LocalDate.now().dayOfMonth
                val dailyPace = if (dayOfMonth > 0) {
                    budgetWithProgress.spentAmount / dayOfMonth
                } else {
                    0.0
                }
                val idealDailyPace = if (daysInMonth > 0) {
                    budgetWithProgress.limitAmount / daysInMonth
                } else {
                    0.0
                }

                DetailRow(
                    label = "Ritmo diario",
                    value = "${CurrencyFormatter.format(dailyPace, budgetWithProgress.currencyCode)}/d\u00EDa",
                    valueColor = if (dailyPace <= idealDailyPace) t.success else Color(0xFFFF6B00), // orange warning
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = t.textMuted,
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
        )
    }
}
