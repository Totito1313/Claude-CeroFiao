package com.schwarckdev.cerofiao.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter

@Composable
fun MoneyText(
    amount: Double,
    currencyCode: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = Color.Unspecified,
    showSign: Boolean = false,
    compact: Boolean = false,
    fontWeight: FontWeight? = null,
) {
    val formatted = if (compact) {
        CurrencyFormatter.formatCompact(kotlin.math.abs(amount), currencyCode)
    } else {
        CurrencyFormatter.format(kotlin.math.abs(amount), currencyCode)
    }

    val prefix = when {
        !showSign -> ""
        amount > 0 -> "+"
        amount < 0 -> "-"
        else -> ""
    }

    Text(
        text = "$prefix$formatted",
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = fontWeight,
    )
}
