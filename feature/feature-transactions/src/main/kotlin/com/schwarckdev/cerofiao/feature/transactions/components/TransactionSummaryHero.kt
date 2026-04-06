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
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonGroupItem
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonSize
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButtonGroup
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.TransactionType
import kotlin.math.abs

private data class HeroData(
    val label: String,
    val totalAmountUsd: Double,
)

private data class DisplayOption(
    val code: String,
    val symbol: String,
    val label: String,
    val sourceLabel: String,
)

private val displayOptions = listOf(
    DisplayOption("USD", "$", "Dolar", "BCV"),
    DisplayOption("VES", "Bs", "Bolívares", ""),
    DisplayOption("USDT", "\u20AE", "USDT", "Paralelo"),
    DisplayOption("EUR", "\u20AC", "Euro", "BCV"),
    DisplayOption("EURI", "\u20AC", "EURI", "Paralelo"),
)

@Composable
fun TransactionSummaryHero(
    totalIncomeDisplay: Double,
    totalExpenseDisplay: Double,
    selectedTypeFilter: TransactionType?,
    monthOverMonthPercent: Double?,
    displayCurrencyCode: String,
    displayFormatCode: String,
    displaySymbol: String,
    displayLabel: String,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    var showCurrencyMenu by remember { mutableStateOf(false) }

    // Amounts are already converted to display currency by the ViewModel via RateTable
    val heroData = when (selectedTypeFilter) {
        TransactionType.INCOME -> HeroData("TOTAL DE INGRESOS", totalIncomeDisplay)
        TransactionType.EXPENSE -> HeroData("TOTAL DE GASTOS", totalExpenseDisplay)
        else -> HeroData("BALANCE NETO", totalIncomeDisplay - totalExpenseDisplay)
    }

    val convertedAmount = heroData.totalAmountUsd

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
            val isNegative = amount < 0
            val amountColor = if (isNegative) colors.ExpenseColor else colors.TextPrimary
            Row(verticalAlignment = Alignment.Bottom) {
                if (isNegative) {
                    Text(
                        text = "-",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = amountColor,
                        letterSpacing = (-2).sp,
                    )
                }
                Text(
                    text = symbol,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isNegative) amountColor.copy(alpha = 0.6f) else colors.TextSecondary,
                    letterSpacing = (-2).sp,
                )
                Text(
                    text = CurrencyFormatter.format(abs(amount), displayFormatCode, showSymbol = false),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = amountColor,
                    letterSpacing = (-2).sp,
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Currency switcher — CeroFiaoButtonGroup (matches accounts donut chart style)
        val selected = displayOptions.first { it.code == displayCurrencyCode }
        Box {
            CeroFiaoButtonGroup(
                items = listOf(
                    ButtonGroupItem(
                        key = "currency",
                        text = "${selected.symbol}  ${selected.label}",
                        badge = selected.sourceLabel.ifEmpty { null },
                        isActive = true,
                        onClick = { showCurrencyMenu = true },
                    ),
                    ButtonGroupItem(
                        key = "dropdown",
                        icon = Lucide.ChevronDown,
                        onClick = { showCurrencyMenu = true },
                    ),
                ),
                size = ButtonSize.Small,
            )

            DropdownMenu(
                expanded = showCurrencyMenu,
                onDismissRequest = { showCurrencyMenu = false },
            ) {
                displayOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "${option.symbol}  ${option.label}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.TextPrimary,
                                )
                                if (option.sourceLabel.isNotEmpty()) {
                                    Text(
                                        text = option.sourceLabel,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = colors.Primary,
                                    )
                                }
                            }
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
