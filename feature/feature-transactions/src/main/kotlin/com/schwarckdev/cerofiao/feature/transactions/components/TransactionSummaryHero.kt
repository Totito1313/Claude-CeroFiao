package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingDown
import com.composables.icons.lucide.TrendingUp
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.TransactionType
import kotlin.math.abs

private data class HeroData(
    val label: String,
    val totalAmountUsd: Double,
)

private data class DisplayOption(
    val code: String,
    val label: String,
)

private val displayOptions = listOf(
    DisplayOption("USD", "USD"),
    DisplayOption("VES", "Bs"),
    DisplayOption("USDT", "USDT"),
    DisplayOption("EUR", "EUR"),
    DisplayOption("EURI", "EURI"),
)

@Composable
fun TransactionSummaryHero(
    totalIncomeUsd: Double,
    totalExpenseUsd: Double,
    selectedTypeFilter: TransactionType?,
    monthOverMonthPercent: Double?,
    displayCurrencyCode: String,
    displayFormatCode: String,
    displaySymbol: String,
    displayLabel: String,
    displayRate: Double,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    var showCurrencyMenu by remember { mutableStateOf(false) }

    val heroData = when (selectedTypeFilter) {
        TransactionType.INCOME -> HeroData("TOTAL DE INGRESOS", totalIncomeUsd)
        TransactionType.EXPENSE -> HeroData("TOTAL DE GASTOS", totalExpenseUsd)
        else -> HeroData("BALANCE NETO", totalIncomeUsd - totalExpenseUsd)
    }

    val convertedAmount = heroData.totalAmountUsd * displayRate

    Column(
        modifier = modifier.padding(top = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Animated label
        AnimatedContent(
            targetState = heroData.label,
            transitionSpec = {
                (fadeIn(spring()) + slideInVertically(spring()) { -it / 3 })
                    .togetherWith(fadeOut(spring()) + slideOutVertically(spring()) { it / 3 })
            },
            label = "heroLabel",
        ) { label ->
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.TextSecondary,
                letterSpacing = 2.2.sp,
            )
        }

        Spacer(Modifier.height(8.dp))

        // Animated amount
        AnimatedContent(
            targetState = Triple(heroData, convertedAmount, displaySymbol),
            transitionSpec = {
                (fadeIn(spring(stiffness = 400f, dampingRatio = 0.8f)) +
                    slideInVertically(spring(stiffness = 400f, dampingRatio = 0.8f)) { -it / 4 })
                    .togetherWith(
                        fadeOut(spring()) + slideOutVertically(spring()) { it / 4 },
                    )
            },
            label = "heroAmount",
        ) { (_, amount, symbol) ->
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = symbol,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextSecondary,
                    letterSpacing = (-2).sp,
                )
                Text(
                    text = CurrencyFormatter.format(abs(amount), displayFormatCode, showSymbol = false),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextPrimary,
                    letterSpacing = (-2).sp,
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Currency switcher pill
        Box {
            Surface(
                onClick = { showCurrencyMenu = true },
                shape = RoundedCornerShape(100.dp),
                color = colors.SurfaceVariant,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = displayLabel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.TextPrimary,
                    )
                    Icon(
                        imageVector = Lucide.ChevronDown,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = colors.TextSecondary,
                    )
                }
            }
            DropdownMenu(
                expanded = showCurrencyMenu,
                onDismissRequest = { showCurrencyMenu = false },
            ) {
                displayOptions.forEach { option ->
                    val isSelected = option.code == displayCurrencyCode
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label,
                                color = if (isSelected) colors.Primary else colors.TextPrimary,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            )
                        },
                        onClick = {
                            onCurrencyChange(option.code)
                            showCurrencyMenu = false
                        },
                    )
                }
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
