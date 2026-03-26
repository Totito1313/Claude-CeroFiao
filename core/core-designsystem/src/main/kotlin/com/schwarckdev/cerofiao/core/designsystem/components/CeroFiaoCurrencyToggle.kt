package com.schwarckdev.cerofiao.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * CurrencyToggle — Animated 3-way toggle matching Figma design.
 *
 * Options: BS ($), USD ($), USDT (₮)
 * Has a sliding accent-colored indicator that animates between options.
 */

private val CURRENCY_LABELS = mapOf(
    "BS" to "Bs",
    "USD" to "$",
    "USDT" to "₮"
)

@Composable
fun CeroFiaoCurrencyToggle(
    current: String,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    options: List<String> = listOf("BS", "USD", "USDT"),
    compact: Boolean = false
) {
    val density = LocalDensity.current

    // Track container width in px for indicator offset calculation
    var containerWidthPx = remember { 0 }
    val currentIndex = options.indexOf(current).coerceAtLeast(0)
    val optionCount = options.size

    val indicatorOffset by animateFloatAsState(
        targetValue = currentIndex.toFloat(),
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.7f),
        label = "indicator"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (compact) 32.dp else 40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LocalCeroFiaoColors.current.SurfaceVariant)
            .onSizeChanged { containerWidthPx = it.width }
            .padding(4.dp)
    ) {
        // Sliding indicator
        val indicatorWidthPx = if (optionCount > 0) (containerWidthPx - with(density) { 8.dp.toPx() }.toInt()) / optionCount else 0
        val offsetX = (indicatorOffset * indicatorWidthPx).roundToInt()

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX, 0) }
                .width(with(density) { indicatorWidthPx.toDp() })
                .height(if (compact) 24.dp else 32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(LocalCeroFiaoColors.current.Primary)
        )

        // Option buttons
        Box(modifier = Modifier.matchParentSize()) {
            options.forEachIndexed { index, option ->
                val isSelected = current == option
                val offsetPx = if (optionCount > 0) (containerWidthPx - with(density) { 8.dp.toPx() }.toInt()) / optionCount * index else 0

                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetPx, 0) }
                        .width(with(density) { indicatorWidthPx.toDp() })
                        .height(if (compact) 24.dp else 32.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onCurrencyChange(option) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = CURRENCY_LABELS[option] ?: option,
                        style = if (compact) MaterialTheme.typography.labelSmall
                               else MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) LocalCeroFiaoColors.current.OnPrimary
                               else LocalCeroFiaoColors.current.TextSecondary
                    )
                }
            }
        }
    }
}
