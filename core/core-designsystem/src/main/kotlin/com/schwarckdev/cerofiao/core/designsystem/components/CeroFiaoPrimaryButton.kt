package com.schwarckdev.cerofiao.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors

/**
 * Primary action button mapped from LionFitness's LionPrimaryButton.
 * Includes bounce click, advanced shadow, and loading state support.
 */
@Composable
fun CeroFiaoPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val colors = LocalCeroFiaoColors.current
    val shape = RoundedCornerShape(25.dp)
    
    val containerColor = if (enabled) LocalCeroFiaoColors.current.Primary else colors.InactiveColor
    val contentColor = if (enabled) LocalCeroFiaoColors.current.OnPrimary else colors.TextSecondary
    
    // Build modifiers in composable scope (bounceClick is @Composable)
    val shadowModifier = if (enabled && !isLoading) {
        Modifier.advancedShadow(
            color = containerColor,
            alpha = 0.3f,
            shadowBlurRadius = 15.dp,
            offsetY = 6.dp
        )
    } else Modifier

    val clickModifier = if (enabled && !isLoading) {
        Modifier.bounceClick(onClick = onClick)
    } else Modifier

    Box(
        modifier = modifier
            .then(shadowModifier)
            .then(clickModifier)
            .clip(shape)
            .background(containerColor)
            .border(1.dp, containerColor.copy(alpha = 0.5f), shape)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = contentColor,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
