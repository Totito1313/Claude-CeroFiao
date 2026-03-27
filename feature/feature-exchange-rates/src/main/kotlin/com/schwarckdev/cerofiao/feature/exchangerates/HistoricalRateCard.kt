package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingUp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.TransferGradient
import com.schwarckdev.cerofiao.core.model.ExchangeRate

private val GradientBrush = Brush.horizontalGradient(
    listOf(Color(0xFF66A1F3), Color(0xFF22C9A6)),
)

@Composable
fun HistoricalRateCard(
    title: String,
    currentRate: Double?,
    historicalRates: List<ExchangeRate>,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val dataPoints = historicalRates.map { it.rate }

    val percentChange = if (dataPoints.size >= 2) {
        val first = dataPoints.first()
        val last = dataPoints.last()
        if (first > 0) ((last - first) / first * 100) else 0.0
    } else {
        0.0
    }

    Surface(
        modifier = modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(30.dp),
        color = colors.Background,
        shadowElevation = 5.dp,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // Title row with gradient dot and gradient text
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(GradientBrush, CircleShape),
                )
                Text(
                    text = title,
                    style = TextStyle(
                        brush = GradientBrush,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                    ),
                )
            }

            // Current rate value
            AnimatedContent(
                targetState = currentRate,
                label = "current_rate"
            ) { targetRate ->
                Text(
                    text = if (targetRate != null) String.format("%.2f Bs", targetRate) else "—",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            // Chart
            if (dataPoints.size >= 2) {
                HistoricalRateChart(
                    dataPoints = dataPoints,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    lineBrush = TransferGradient,
                )
            }

            // Bottom row: percentage + trending icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedContent(
                    targetState = percentChange,
                    label = "percent_change"
                ) { targetPercent ->
                    Text(
                        text = if (dataPoints.size >= 2) {
                            String.format("%+.1f%%", targetPercent)
                        } else {
                            "—"
                        },
                        fontSize = 12.sp,
                        color = colors.TextSecondary,
                    )
                }
                Icon(
                    imageVector = Lucide.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color(0xFF22C9A6),
                )
            }
        }
    }
}
