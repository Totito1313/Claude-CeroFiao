package com.schwarckdev.cerofiao.feature.dashboard.components

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingUp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCardConfig
import com.schwarckdev.cerofiao.core.designsystem.theme.RateColors
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

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
    val colors = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val prefs = remember { context.getSharedPreferences("dashboard_prefs", Context.MODE_PRIVATE) }

    val initialItems = listOf(
        RateItem("bcv_eur", "€ Euro (BCV)", bcvEurRate?.rate, RateColors.BcvGreen),
        RateItem("euri", "€ EURI", euriRate?.rate, RateColors.ParallelTeal),
        RateItem("bcv_usd", "$ Dólar (BCV)", bcvRate?.rate, RateColors.BcvGreen),
        RateItem("usdt", "₮ USDT", usdtRate?.rate, RateColors.ParallelTeal),
    )

    val items = remember {
        val savedOrder = prefs.getString("exchange_rates_order", null)?.split(",")
        val sortedItems = if (savedOrder != null) {
            val orderMap = savedOrder.withIndex().associate { it.value to it.index }
            initialItems.sortedBy { orderMap[it.key] ?: Int.MAX_VALUE }
        } else {
            initialItems
        }
        mutableStateListOf(*sortedItems.toTypedArray())
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

    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        val temp = items[from.index]
        items[from.index] = items[to.index]
        items[to.index] = temp
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Column(modifier = modifier) {
        SectionHeader(title = "Tasas de cambio", onViewAll = onViewAll)
        Spacer(Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(34.dp),
            color = colors.Foreground.copy(alpha = cardConfig.backgroundOpacity),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = lazyGridState,
                userScrollEnabled = false,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                // HeightIn used to allow wrapContentHeight inside a Scrollable parent
                modifier = Modifier.fillMaxWidth().heightIn(max = 1000.dp)
            ) {
                items(items, key = { it.key }) { item ->
                    ReorderableItem(
                        state = reorderableLazyGridState,
                        key = item.key
                    ) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp)

                        Surface(
                            shape = RoundedCornerShape(30.dp),
                            color = Color.Transparent,
                            shadowElevation = elevation,
                            modifier = Modifier
                                .fillMaxWidth()
                                .longPressDraggableHandle(
                                    onDragStarted = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    },
                                    onDragStopped = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        prefs.edit().putString("exchange_rates_order", items.joinToString(",") { it.key }).apply()
                                    }
                                )
                        ) {
                            ExchangeRateCard(
                                label = item.label,
                                rate = item.rate,
                                color = item.color,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExchangeRateCard(
    label: String,
    rate: Double?,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = colors.Background.copy(alpha = cardConfig.backgroundOpacity),
        shadowElevation = 0.dp,
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
                color = colors.TextPrimary,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (rate != null) "+0%" else "—",
                    fontSize = 12.sp,
                    color = colors.TextSecondary,
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
