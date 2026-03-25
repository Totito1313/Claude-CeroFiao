package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingUp
import com.schwarckdev.cerofiao.core.model.ExchangeRate

private data class RateItem(
    val key: String,
    val label: String,
    val rate: Double?,
    val color: Color,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExchangeRatesSection(
    bcvRate: ExchangeRate?,
    euriRate: ExchangeRate?,
    bcvEurRate: ExchangeRate?,
    usdtRate: ExchangeRate?,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = remember {
        mutableStateListOf(
            RateItem(
                key = "bcv_eur",
                label = "€ Euro (BCV)",
                rate = bcvEurRate?.rate,
                color = Color(0xFF00BB89),
            ),
            RateItem(
                key = "euri",
                label = "€ EURI",
                rate = euriRate?.rate,
                color = Color(0xFF00819A),
            ),
            RateItem(
                key = "bcv_usd",
                label = "$ Dólar (BCV)",
                rate = bcvRate?.rate,
                color = Color(0xFF00BB89),
            ),
            RateItem(
                key = "usdt",
                label = "₮ USDT",
                rate = usdtRate?.rate,
                color = Color(0xFF00819A),
            ),
        )
    }

    // Update rates when they change without resetting order
    remember(bcvEurRate?.rate, euriRate?.rate, bcvRate?.rate, usdtRate?.rate) {
        val rateMap = mapOf(
            "bcv_eur" to bcvEurRate?.rate,
            "euri" to euriRate?.rate,
            "bcv_usd" to bcvRate?.rate,
            "usdt" to usdtRate?.rate,
        )
        for (i in items.indices) {
            val updated = rateMap[items[i].key]
            if (items[i].rate != updated) {
                items[i] = items[i].copy(rate = updated)
            }
        }
        true
    }

    // Drag state
    var draggedIndex by remember { mutableIntStateOf(-1) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    // Track each card's position and size for hit testing
    val cardPositions = remember { mutableMapOf<Int, Offset>() }
    val cardSizes = remember { mutableMapOf<Int, IntSize>() }

    Column(modifier = modifier) {
        SectionHeader(title = "Tasas de cambio", onViewAll = onViewAll)
        Spacer(Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(48.dp),
            color = Color(0xFFFCFCFF),
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(11.dp),
                maxItemsInEachRow = 2,
            ) {
                items.forEachIndexed { index, item ->
                    key(item.key) {
                        val isDragging = draggedIndex == index
                        val scale by animateFloatAsState(
                            targetValue = if (isDragging) 1.05f else 1f,
                            label = "dragScale",
                        )

                        DraggableExchangeRateCard(
                            label = item.label,
                            rate = item.rate,
                            color = item.color,
                            isDragging = isDragging,
                            scale = scale,
                            dragOffsetX = if (isDragging) dragOffset.x else 0f,
                            dragOffsetY = if (isDragging) dragOffset.y else 0f,
                            onPositioned = { position, size ->
                                cardPositions[index] = position
                                cardSizes[index] = size
                            },
                            onDragStart = {
                                draggedIndex = index
                                dragOffset = Offset.Zero
                            },
                            onDrag = { change ->
                                dragOffset = Offset(
                                    dragOffset.x + change.x,
                                    dragOffset.y + change.y,
                                )
                            },
                            onDragEnd = {
                                // Determine target by finding which card center is closest
                                // to the dragged card's current center
                                val draggedPos = cardPositions[draggedIndex]
                                val draggedSize = cardSizes[draggedIndex]
                                if (draggedPos != null && draggedSize != null) {
                                    val draggedCenter = Offset(
                                        draggedPos.x + draggedSize.width / 2f + dragOffset.x,
                                        draggedPos.y + draggedSize.height / 2f + dragOffset.y,
                                    )
                                    var targetIndex = draggedIndex
                                    var minDist = Float.MAX_VALUE
                                    for (i in items.indices) {
                                        if (i == draggedIndex) continue
                                        val pos = cardPositions[i] ?: continue
                                        val size = cardSizes[i] ?: continue
                                        val center = Offset(
                                            pos.x + size.width / 2f,
                                            pos.y + size.height / 2f,
                                        )
                                        val dist = (draggedCenter - center).getDistance()
                                        if (dist < minDist) {
                                            minDist = dist
                                            targetIndex = i
                                        }
                                    }
                                    // Only swap if we actually overlap the target card
                                    if (targetIndex != draggedIndex) {
                                        val targetPos = cardPositions[targetIndex]
                                        val targetSize = cardSizes[targetIndex]
                                        if (targetPos != null && targetSize != null) {
                                            val threshold = (targetSize.width / 3f)
                                            if (minDist < threshold + targetSize.width / 2f) {
                                                val temp = items[draggedIndex]
                                                items[draggedIndex] = items[targetIndex]
                                                items[targetIndex] = temp
                                            }
                                        }
                                    }
                                }
                                draggedIndex = -1
                                dragOffset = Offset.Zero
                            },
                            onDragCancel = {
                                draggedIndex = -1
                                dragOffset = Offset.Zero
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowScope.DraggableExchangeRateCard(
    label: String,
    rate: Double?,
    color: Color,
    isDragging: Boolean,
    scale: Float,
    dragOffsetX: Float,
    dragOffsetY: Float,
    onPositioned: (Offset, IntSize) -> Unit,
    onDragStart: () -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .zIndex(if (isDragging) 10f else 0f)
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.positionInParent(), coordinates.size)
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { onDragStart() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(Offset(dragAmount.x, dragAmount.y))
                    },
                    onDragEnd = onDragEnd,
                    onDragCancel = onDragCancel,
                )
            }
            .graphicsLayer {
                translationX = dragOffsetX
                translationY = dragOffsetY
                scaleX = scale
                scaleY = scale
                shadowElevation = if (isDragging) 16f else 0f
            },
        shape = RoundedCornerShape(30.dp),
        color = Color(0xFFF1F1F3),
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape),
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = color,
                )
            }
            Text(
                text = if (rate != null) String.format("%.2f Bs", rate) else "—",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (rate != null) "+0%" else "—",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                )
                Icon(
                    imageVector = Lucide.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = color,
                )
            }
        }
    }
}
