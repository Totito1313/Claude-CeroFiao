package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.TransactionType

private data class FilterTab(
    val label: String,
    val type: TransactionType?,
)

private val tabs = listOf(
    FilterTab("Todos", null),
    FilterTab("Ingresos", TransactionType.INCOME),
    FilterTab("Gastos", TransactionType.EXPENSE),
)

@Composable
fun TransactionTypeFilter(
    selectedType: TransactionType?,
    onTypeSelected: (TransactionType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(100.dp),
        color = colors.SurfaceVariant.copy(alpha = 0.5f),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                val isSelected = tab.type == selectedType
                val bgColor by animateColorAsState(
                    targetValue = when {
                        isSelected && tab.type == TransactionType.INCOME -> colors.IncomeColor
                        isSelected && tab.type == TransactionType.EXPENSE -> colors.ExpenseColor
                        isSelected -> colors.Primary
                        else -> Color.Transparent
                    },
                    label = "tabBg",
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else colors.TextSecondary,
                    label = "tabText",
                )

                Surface(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onTypeSelected(tab.type)
                    },
                    shape = RoundedCornerShape(100.dp),
                    color = bgColor,
                ) {
                    Text(
                        text = tab.label,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor,
                    )
                }
            }
        }
    }
}
