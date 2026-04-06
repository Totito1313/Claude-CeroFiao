package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.TransactionType
import kotlin.math.roundToInt

private data class FilterTab(
    val label: String,
    val type: TransactionType?,
)

private val tabs = listOf(
    FilterTab("Todos", null),
    FilterTab("Ingresos", TransactionType.INCOME),
    FilterTab("Gastos", TransactionType.EXPENSE),
)

private val tabShape = RoundedCornerShape(24.dp)

@Composable
fun TransactionTypeFilter(
    selectedType: TransactionType?,
    onTypeSelected: (TransactionType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    val colors = CeroFiaoDesign.colors
    val density = LocalDensity.current

    val currentIndex = tabs.indexOfFirst { it.type == selectedType }.coerceAtLeast(0)

    val indicatorOffset by animateFloatAsState(
        targetValue = currentIndex.toFloat(),
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.7f),
        label = "tabIndicator",
    )

    // Animate gradient colors for smooth cross-type transition
    val (gradStartTarget, gradEndTarget) = when (selectedType) {
        TransactionType.INCOME -> Color(0xFF85F366) to Color(0xFF22C9A6)
        TransactionType.EXPENSE -> Color(0xFF8A2BE2) to Color(0xFFFF6B00)
        else -> Color(0xFF66A1F3) to Color(0xFF22C9A6)
    }
    val gradStart by animateColorAsState(gradStartTarget, label = "gradStart")
    val gradEnd by animateColorAsState(gradEndTarget, label = "gradEnd")
    val animatedGradient = Brush.horizontalGradient(listOf(gradStart, gradEnd))

    var containerWidthPx by remember { mutableIntStateOf(0) }
    val innerPaddingPx = with(density) { 8.dp.toPx() }
    val tabWidthPx = if (containerWidthPx > 0) {
        (containerWidthPx - 2f * innerPaddingPx) / tabs.size
    } else 0f
    val indicatorOffsetX = (innerPaddingPx + indicatorOffset * tabWidthPx).roundToInt()
    val indicatorOffsetY = with(density) { 4.dp.roundToPx() }

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(colors.SurfaceVariant)
            .onSizeChanged { containerWidthPx = it.width },
    ) {
        // Sliding gradient indicator
        if (tabWidthPx > 0f) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(indicatorOffsetX, indicatorOffsetY) }
                    .width(with(density) { tabWidthPx.toDp() })
                    .height(40.dp)
                    .shadow(4.dp, tabShape)
                    .clip(tabShape)
                    .background(animatedGradient),
            )
        }

        // Clickable tabs overlay
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            tabs.forEach { tab ->
                val isSelected = tab.type == selectedType
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(tabShape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onTypeSelected(tab.type)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = tab.label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) colors.TextPrimary else colors.TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
