package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

@Composable
fun QuickActionsCard(
    onAddTransaction: () -> Unit,
    onTransfer: () -> Unit,
    onCategories: () -> Unit,
    onExchangeRates: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        color = colors.Foreground,
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                QuickActionIcon(contentDescription = "Agregar", onClick = onAddTransaction)
                QuickActionIcon(contentDescription = "Transfer", onClick = onTransfer)
                QuickActionIcon(contentDescription = "Categorías", onClick = onCategories)
                QuickActionIcon(contentDescription = "Tasas", onClick = onExchangeRates)
            }
            // Bottom indicator bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
                    .width(50.dp)
                    .height(4.dp)
                    .background(colors.TextSecondary, RoundedCornerShape(2.dp)),
            )
        }
    }
}

@Composable
private fun QuickActionIcon(
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    Surface(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        shape = CircleShape,
        color = colors.SurfaceVariant,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Lucide.Plus,
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp),
                tint = colors.TextSecondary,
            )
        }
    }
}
