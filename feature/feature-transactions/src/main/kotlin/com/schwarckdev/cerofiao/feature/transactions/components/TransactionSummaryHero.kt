package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingDown
import com.composables.icons.lucide.TrendingUp
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.TransactionType
import kotlin.math.abs

private data class HeroData(
    val label: String,
    val totalAmount: Double,
)

@Composable
fun TransactionSummaryHero(
    totalIncomeUsd: Double,
    totalExpenseUsd: Double,
    selectedTypeFilter: TransactionType?,
    monthOverMonthPercent: Double?,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors

    val heroData = when (selectedTypeFilter) {
        TransactionType.INCOME -> HeroData("TOTAL DE INGRESOS", totalIncomeUsd)
        TransactionType.EXPENSE -> HeroData("TOTAL DE GASTOS", totalExpenseUsd)
        else -> HeroData("BALANCE NETO", totalIncomeUsd - totalExpenseUsd)
    }

    Column(
        modifier = modifier.padding(top = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Animated label
        AnimatedContent(
            targetState = heroData.label,
            transitionSpec = {
                (fadeIn(tween(200)) + slideInVertically(tween(200)) { -it / 3 })
                    .togetherWith(fadeOut(tween(150)) + slideOutVertically(tween(150)) { it / 3 })
            },
            label = "heroLabel",
        ) { label ->
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.TextSecondary,
                letterSpacing = 2.4.sp,
            )
        }

        Spacer(Modifier.height(8.dp))

        // Animated amount
        AnimatedContent(
            targetState = heroData,
            transitionSpec = {
                (fadeIn(tween(250)) + slideInVertically(tween(250)) { -it / 4 })
                    .togetherWith(fadeOut(tween(150)) + slideOutVertically(tween(150)) { it / 4 })
            },
            label = "heroAmount",
        ) { data ->
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextSecondary,
                    letterSpacing = (-2).sp,
                )
                Text(
                    text = CurrencyFormatter.format(abs(data.totalAmount), "USD", showSymbol = false),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextPrimary,
                    letterSpacing = (-2).sp,
                )
            }
        }

        if (monthOverMonthPercent != null) {
            Spacer(Modifier.height(8.dp))
            val isPositive = monthOverMonthPercent >= 0
            val percentColor = if (isPositive) colors.IncomeColor else colors.ExpenseColor
            val percentBg = percentColor.copy(alpha = 0.12f)

            Surface(
                shape = RoundedCornerShape(100.dp),
                color = percentBg,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = if (isPositive) Lucide.TrendingUp else Lucide.TrendingDown,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = percentColor,
                    )
                    Text(
                        text = "${if (isPositive) "+" else ""}${String.format("%.0f", monthOverMonthPercent)}% vs mes anterior",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = percentColor,
                    )
                }
            }
        }
    }
}
