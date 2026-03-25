package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingUp
import com.schwarckdev.cerofiao.core.model.ExchangeRate

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
                ExchangeRateCard(
                    label = "€ Euro (BCV)",
                    rate = bcvEurRate?.rate,
                    color = Color(0xFF00BB89),
                    modifier = Modifier.width(180.dp),
                )
                ExchangeRateCard(
                    label = "€ EURI",
                    rate = euriRate?.rate,
                    color = Color(0xFF00819A),
                    modifier = Modifier.width(180.dp),
                )
                ExchangeRateCard(
                    label = "$ Dólar (BCV)",
                    rate = bcvRate?.rate,
                    color = Color(0xFF00BB89),
                    modifier = Modifier.width(180.dp),
                )
                ExchangeRateCard(
                    label = "₮ USDT",
                    rate = usdtRate?.rate,
                    color = Color(0xFF00819A),
                    modifier = Modifier.width(180.dp),
                )
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
    Surface(
        modifier = modifier,
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
