package com.schwarckdev.cerofiao.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryFood
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryCoffee
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryTransport
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryHome
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryServices
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryEntertainment
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryTech
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryWork
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryHealth
import com.schwarckdev.cerofiao.core.designsystem.theme.CategoryOther
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import com.schwarckdev.cerofiao.core.designsystem.util.CurrencyFormatter

/**
 * TransactionItem — Row component matching Figma design.
 *
 * Shows: category icon → concept/category + date → amount with currency symbol
 * If the transaction currency differs from baseCurrency, shows equivalence.
 */

// Emoji category icons (no MaterialUI per user rule #4)
private val CATEGORY_EMOJIS = mapOf(
    "Alimentación" to "🛒",
    "Comida" to "☕",
    "Transporte" to "🚗",
    "Hogar" to "🏠",
    "Servicios" to "⚡",
    "Entretenimiento" to "🎮",
    "Tecnología" to "📱",
    "Trabajo" to "💼",
    "Salud" to "❤️",
    "Otros" to "📦"
)

private val CATEGORY_COLORS = mapOf(
    "Alimentación" to CategoryFood,
    "Comida" to CategoryCoffee,
    "Transporte" to CategoryTransport,
    "Hogar" to CategoryHome,
    "Servicios" to CategoryServices,
    "Entretenimiento" to CategoryEntertainment,
    "Tecnología" to CategoryTech,
    "Trabajo" to CategoryWork,
    "Salud" to CategoryHealth,
    "Otros" to CategoryOther
)

@Composable
fun CeroFiaoTransactionItem(
    concept: String?,
    category: String,
    type: String, // INCOME, EXPENSE, TRANSFER
    amount: String,
    currency: String,
    date: String,
    modifier: Modifier = Modifier,
    baseCurrency: String = "USD",
    equivalentAmount: String? = null,
    onClick: (() -> Unit)? = null
) {
    val extendedColors = LocalCeroFiaoColors.current
    val isExpense = type == "EXPENSE"
    val categoryColor = CATEGORY_COLORS[category] ?: CategoryOther
    val emoji = CATEGORY_EMOJIS[category] ?: "📦"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ) else Modifier
            )
            .padding(horizontal = 2.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category icon circle
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(categoryColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = concept ?: category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = LocalCeroFiaoColors.current.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$date • $category",
                style = MaterialTheme.typography.labelSmall,
                color = LocalCeroFiaoColors.current.TextSecondary
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Amount
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (isExpense) "-" else "+"}${CurrencyFormatter.format(amount, currency)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isExpense) LocalCeroFiaoColors.current.TextPrimary else extendedColors.income
            )
            // Show equivalence if different currency
            if (currency != baseCurrency && equivalentAmount != null) {
                Text(
                    text = "≈ ${CurrencyFormatter.format(equivalentAmount, baseCurrency)}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalCeroFiaoColors.current.TextSecondary
                )
            }
        }
    }
}
