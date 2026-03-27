package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ArrowUpDown
import com.composables.icons.lucide.Grid2x2
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Wallet
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

@Composable
fun TransactionFilterChips(
    hasAccountFilter: Boolean,
    hasCategoryFilter: Boolean,
    onAccountsClick: () -> Unit,
    onSortClick: () -> Unit,
    onCategoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChipItem(
            label = "Cuentas",
            icon = Lucide.Wallet,
            isActive = hasAccountFilter,
            onClick = onAccountsClick,
        )
        FilterChipItem(
            label = "Ordenar",
            icon = Lucide.ArrowUpDown,
            isActive = false,
            onClick = onSortClick,
        )
        FilterChipItem(
            label = "Categoría",
            icon = Lucide.Grid2x2,
            isActive = hasCategoryFilter,
            onClick = onCategoryClick,
        )
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val haptic = LocalHapticFeedback.current
    val bgColor = if (isActive) colors.Primary.copy(alpha = 0.12f) else colors.Surface
    val contentColor = if (isActive) colors.Primary else colors.TextSecondary
    val border = if (isActive) null else BorderStroke(1.dp, colors.CardBorder)

    Surface(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(100.dp),
        color = bgColor,
        border = border,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = contentColor,
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor,
            )
        }
    }
}
