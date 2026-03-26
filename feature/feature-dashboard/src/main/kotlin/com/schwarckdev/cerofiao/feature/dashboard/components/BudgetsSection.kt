package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.feature.dashboard.BudgetWithSpending

@Composable
fun BudgetsSection(
    budgets: List<BudgetWithSpending>,
    onViewAll: () -> Unit,
    onAddBudget: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(0.dp),
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = "Presupuestos",
            onViewAll = onViewAll,
            modifier = Modifier.padding(contentPadding)
        )
        Spacer(Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(budgets) { item ->
                BudgetCard(item = item)
            }
            item {
                AddBudgetCard(onClick = onAddBudget)
            }
        }
    }
}

@Composable
private fun BudgetCard(
    item: BudgetWithSpending,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val hasLimit = item.budget.limitAmount > 0
    val progress = if (hasLimit && item.budget.limitAmount > 0) {
        (item.spentAmount / item.budget.limitAmount).toFloat().coerceIn(0f, 1f)
    } else 0f

    Surface(
        modifier = modifier
            .width(288.dp)
            .height(182.dp),
        shape = RoundedCornerShape(32.dp),
        color = colors.Foreground,
    ) {
        Box {
            // Decorative blur circle
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .align(if (hasLimit) Alignment.TopEnd else Alignment.BottomStart)
                    .padding(
                        top = if (hasLimit) 0.dp else 0.dp,
                        end = if (hasLimit) 0.dp else 0.dp,
                    )
                    .blur(30.dp)
                    .background(
                        if (hasLimit) Color(0xFFCA98FF).copy(alpha = 0.2f)
                        else Color(0xFF00FE66).copy(alpha = 0.1f),
                        CircleShape
                    ),
            )
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Top
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = if (hasLimit) "LIMITADO" else "SIN LÍMITE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.InactiveColor,
                            letterSpacing = 1.1.sp,
                        )
                        Text(
                            text = item.categoryName ?: item.budget.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary,
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                // Bottom
                if (hasLimit) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.5.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = CurrencyFormatter.format(item.spentAmount, item.budget.anchorCurrencyCode),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.TextSecondary,
                            )
                            Text(text = "/", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.TextPrimary)
                            Text(
                                text = CurrencyFormatter.format(item.budget.limitAmount, item.budget.anchorCurrencyCode),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.TextPrimary,
                            )
                        }
                        // Progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(9999.dp))
                                .background(colors.SurfaceVariant),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .height(4.dp)
                                    .background(Color(0xFFCA98FF), RoundedCornerShape(9999.dp)),
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.5.dp)) {
                        Text(
                            text = CurrencyFormatter.format(item.spentAmount, item.budget.anchorCurrencyCode),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddBudgetCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    Surface(
        onClick = onClick,
        modifier = modifier
            .width(288.dp)
            .height(182.dp),
        shape = RoundedCornerShape(32.dp),
        color = colors.Foreground,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(colors.SurfaceVariant, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Lucide.Plus,
                    contentDescription = "Add budget",
                    modifier = Modifier.size(14.dp),
                    tint = colors.TextSecondary,
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "CREAR PRESUPUESTO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextSecondary,
                letterSpacing = 1.2.sp,
            )
        }
    }
}
