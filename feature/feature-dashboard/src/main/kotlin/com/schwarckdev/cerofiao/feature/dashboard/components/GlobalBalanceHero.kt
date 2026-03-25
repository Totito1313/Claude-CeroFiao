package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
    Column(
        modifier = modifier.padding(top = 32.dp, bottom = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "BALANCE TOTAL",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFADAAAA),
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
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.5f),
                    letterSpacing = (-3).sp,
                )
                Text(
                    text = if (balanceVisible) {
                        CurrencyFormatter.format(totalBalance, displayCurrencyCode, showSymbol = false)
                    } else {
                        "••••••"
                    },
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    letterSpacing = (-3).sp,
                )
            }
            Icon(
                imageVector = if (balanceVisible) Lucide.Eye else Lucide.EyeOff,
                contentDescription = "Toggle balance visibility",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onToggleVisibility),
                tint = Color.Black.copy(alpha = 0.4f),
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GradientPillButton(
                label = "Gasto",
                gradient = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFFFF6B00))),
                onClick = onExpense,
            )
            GradientPillButton(
                label = "Transfer",
                gradient = Brush.horizontalGradient(listOf(Color(0xFF66A1F3), Color(0xFF22C9A6))),
                onClick = onTransfer,
            )
            GradientPillButton(
                label = "Ingreso",
                gradient = Brush.horizontalGradient(
                    listOf(Color(0x8085F366), Color(0x8022C9A6))
                ),
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
    Row(
        modifier = modifier
            .shadow(2.5.dp, RoundedCornerShape(100.dp))
            .background(gradient, RoundedCornerShape(100.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Lucide.Plus,
            contentDescription = null,
            modifier = Modifier.size(8.dp),
            tint = Color.Black,
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }
}
