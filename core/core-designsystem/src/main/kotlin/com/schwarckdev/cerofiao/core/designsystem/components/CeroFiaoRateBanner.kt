package com.schwarckdev.cerofiao.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.util.CurrencyFormatter

/**
 * RateBanner — Exchange rate display ribbon from Figma.
 * Shows the current market rate as a compact banner.
 */
@Composable
fun CeroFiaoRateBanner(
    rateValue: String,
    modifier: Modifier = Modifier,
    source: String = "Mercado"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LocalCeroFiaoColors.current.Primary.copy(alpha = 0.12f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "📊", modifier = Modifier.padding(end = 8.dp))
            Text(
                text = "Tasa $source",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = LocalCeroFiaoColors.current.Primary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "1 USD = Bs $rateValue",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = LocalCeroFiaoColors.current.Primary
        )
    }
}
