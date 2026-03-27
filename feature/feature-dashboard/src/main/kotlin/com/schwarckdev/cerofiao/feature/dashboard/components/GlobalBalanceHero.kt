package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.components.bounceClick
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.ExpenseGradient
import com.schwarckdev.cerofiao.core.designsystem.theme.IncomeGradient
import com.schwarckdev.cerofiao.core.designsystem.theme.TransferGradient

@Composable
fun GlobalBalanceHero(
    totalBalance: Double,
    displayCurrencyCode: String,
    balanceVisible: Boolean,
    onToggleVisibility: () -> Unit,
    onExpense: () -> Unit,
    onTransfer: () -> Unit,
    onIncome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    Column(
        modifier = modifier.padding(top = 32.dp, bottom = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "BALANCE TOTAL",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colors.TextSecondary,
            letterSpacing = 2.4.sp,
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row {
                val symbol = when (displayCurrencyCode) {
                    "USD" -> "$"
                    "EUR" -> "€"
                    "VES" -> "Bs"
                    "USDT" -> "₮"
                    else -> "$"
                }
                Text(
                    text = symbol,
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextSecondary,
                    letterSpacing = (-3).sp,
                )
                Text(
                    text = if (balanceVisible) {
                        CurrencyFormatter.format(totalBalance, displayCurrencyCode, showSymbol = false)
                    } else {
                        "••••••"
                    },
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextPrimary,
                    letterSpacing = (-3).sp,
                )
            }
            Surface(
                onClick = onToggleVisibility,
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp),
                shape = CircleShape,
                color = Color.Transparent
            ) {
                Icon(
                    imageVector = if (balanceVisible) Lucide.Eye else Lucide.EyeOff,
                    contentDescription = "Toggle balance visibility",
                    modifier = Modifier.fillMaxSize(),
                    tint = colors.InactiveColor,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GradientPillButton(
                label = "Gasto",
                gradient = ExpenseGradient,
                onClick = onExpense,
            )
            GradientPillButton(
                label = "Transfer",
                gradient = TransferGradient,
                onClick = onTransfer,
            )
            GradientPillButton(
                label = "Ingreso",
                gradient = IncomeGradient,
                onClick = onIncome,
            )
        }
    }
}

@Composable
private fun GradientPillButton(
    label: String,
    gradient: Brush,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    Row(
        modifier = modifier
            .shadow(2.5.dp, RoundedCornerShape(100.dp))
            .background(gradient, RoundedCornerShape(100.dp))
            .bounceClick(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Lucide.Plus,
            contentDescription = null,
            modifier = Modifier.size(8.dp),
            tint = colors.TextPrimary,
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colors.TextPrimary,
        )
    }
}
