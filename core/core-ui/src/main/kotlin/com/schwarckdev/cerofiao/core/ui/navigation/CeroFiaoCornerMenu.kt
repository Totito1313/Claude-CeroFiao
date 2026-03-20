package com.schwarckdev.cerofiao.core.ui.navigation

import com.schwarckdev.cerofiao.core.designsystem.theme.*
import dev.chrisbanes.haze.*



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.schwarckdev.cerofiao.core.designsystem.theme.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild


/**
 * Corner Menu Component
 * Popup menu with options - with real backdrop blur
 * Pixel-perfect port from LionFitness CornerMenu.kt
 *
 * LionFitness values inlined:
 * - Radius.cornerMenu = 28.dp
 * - ComponentSize.cornerMenuWidth = 220.dp
 * - ComponentSize.cornerMenuItemPaddingVertical = 4.dp
 * - ComponentSize.cornerMenuItemPaddingHorizontal = 9.dp
 * - IconSize.md = 24.dp
 * - Spacing.xs = 6.dp
 */
@Composable
fun CeroFiaoCornerMenu(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onMenuItemClick: (String) -> Unit
) {
    val colors = CeroFiaoTheme.tokens
    val shape = RoundedCornerShape(CeroFiaoDesign.radius.cornerMenu) // Radius.cornerMenu
    
    val glassConfig = LocalGlassConfig.current
    val isBlurEnabled = LocalBlurEnabled.current
    val bgAlpha = if (isBlurEnabled) glassConfig.tintAlpha else 1f
    
    Box(
        modifier = modifier
            .graphicsLayer { clip = false }
    ) {
        Box(
            modifier = Modifier
                .width(220.dp) // ComponentSize.cornerMenuWidth
                .shadow(15.dp, androidx.compose.foundation.shape.RoundedCornerShape(25.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .then(
                        if (isBlurEnabled) {
                             Modifier.hazeChild(
                                state = hazeState,
                                style = dev.chrisbanes.haze.HazeStyle(
                                    backgroundColor = colors.GlassBackground,
                                    blurRadius = glassConfig.blurIntensity,
                                    tint = null
                                )
                            )
                        } else Modifier
                    )
                    .background(colors.GlassBackground.copy(alpha = bgAlpha))
                    .border(1.dp, colors.CardBorder.copy(alpha = glassConfig.borderOpacity), shape)
            ) {
                // Menu Content
        Column(
            modifier = Modifier.padding(
                vertical = CeroFiaoDesign.componentSize.cornerMenuPaddingVertical,
                horizontal = CeroFiaoDesign.componentSize.cornerMenuPaddingHorizontal
            ),
            verticalArrangement = Arrangement.spacedBy(CeroFiaoDesign.spacing.sm)
        ) {
            CornerMenuItem(
                label = "Cuenta",
                iconData = Icons.Default.Person,
                onClick = { onMenuItemClick("Cuenta") }
            )

            CornerMenuItem(
                label = "Tasas de Cambio",
                iconData = Icons.Default.Sync,
                onClick = { onMenuItemClick("Tasas") }
            )

            CornerMenuItem(
                label = "Deudas",
                iconData = Icons.Default.List,
                onClick = { onMenuItemClick("Deudas") }
            )

            DashedDivider()

            CornerMenuItem(
                label = "Ajustes",
                iconData = Icons.Default.Settings,
                onClick = { onMenuItemClick("Ajustes") }
            )
        }
            }
        }
    }
}

@Composable
private fun CornerMenuItem(
    label: String,
    iconData: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconList: List<androidx.compose.ui.graphics.vector.ImageVector>? = null,
    onClick: () -> Unit
) {
    val colors = CeroFiaoTheme.tokens

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(
                vertical = 4.dp,  // ComponentSize.cornerMenuItemPaddingVertical
                horizontal = 9.dp // ComponentSize.cornerMenuItemPaddingHorizontal
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(modifier = Modifier.size(CeroFiaoDesign.iconSize.md)) { // IconSize.md
            if (iconData != null) {
                androidx.compose.material3.Icon(
                    imageVector = iconData,
                    contentDescription = null,
                    tint = colors.TextPrimary,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (iconList != null) {
                val textPrimaryColor = colors.TextPrimary
                Box(modifier = Modifier.fillMaxSize()) {
                    iconList.forEach { vector ->
                        androidx.compose.material3.Icon(
                            imageVector = vector,
                            contentDescription = null,
                            tint = textPrimaryColor,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(CeroFiaoDesign.spacing.xs)) // Spacing.xs

        Text(
            text = label,
            style = CeroFiaoTypography.titleMedium,
            color = colors.TextPrimary
        )
    }
}

@Composable
private fun DashedDivider() {
    val lineColor = CeroFiaoTheme.tokens.text
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(CeroFiaoDesign.spacing.xxxs)
    ) {
        drawLine(
            color = lineColor.copy(alpha = 0.3f),
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 1f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
        )
    }
}
