package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    accountsDropdownContent: @Composable BoxScope.() -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box {
            FilterChipItem(
                label = "Cuentas",
                icon = Lucide.Wallet,
                isActive = hasAccountFilter,
                onClick = onAccountsClick,
            )
            accountsDropdownContent()
        }
        FilterChipItem(
            label = "Ordenar",
            icon = Lucide.ArrowUpDown,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
) {
    val haptic = LocalHapticFeedback.current
    val colors = CeroFiaoDesign.colors
    val contentColor = if (isActive) colors.Primary else colors.TextPrimary

    Surface(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(24.dp),
        color = if (isActive) colors.Primary.copy(alpha = 0.12f) else colors.SurfaceVariant,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                color = contentColor,
            )
        }
    }
}
