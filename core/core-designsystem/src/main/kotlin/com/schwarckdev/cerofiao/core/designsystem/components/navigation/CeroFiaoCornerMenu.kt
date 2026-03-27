package com.schwarckdev.cerofiao.core.designsystem.components.navigation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.components.advancedShadow
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
    visible: Boolean = true,
    onMenuItemLongClick: ((String) -> Unit)? = null,
    onMenuItemClick: (String) -> Unit
) {
    val colors = LocalCeroFiaoColors.current
    val shape = RoundedCornerShape(CeroFiaoDesign.radius.cornerMenu) // Radius.cornerMenu
    
    val glassConfig = LocalGlassConfig.current
    val isBlurEnabled = LocalBlurEnabled.current
    val bgAlpha = if (isBlurEnabled) glassConfig.tintAlpha else 1f
    
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            initialScale = 0.8f,
            transformOrigin = TransformOrigin(1f, 1f),
            animationSpec = spring(
                dampingRatio = 0.7f,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + fadeIn(tween(200)),
        exit = scaleOut(
            targetScale = 0.8f,
            transformOrigin = TransformOrigin(1f, 1f),
            animationSpec = tween(150)
        ) + fadeOut(tween(150)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer { clip = false }
        ) {
        Box(
            modifier = Modifier
                .width(CeroFiaoDesign.componentSize.cornerMenuWidth) // ComponentSize.cornerMenuWidth
                .advancedShadow(
                    color = colors.ShadowColor,
                    alpha = 0.06f,
                    cornersRadius = CeroFiaoDesign.radius.cornerMenu,
                    shadowBlurRadius = CeroFiaoDesign.elevation.cornerMenu,
                    offsetY = CeroFiaoDesign.elevation.sm
                )
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
                iconData = CeroFiaoIcons.SamsungAccount2,
                onClick = { onMenuItemClick("Cuenta") }
            )

            CornerMenuItem(
                label = "Tasas de Cambio",
                iconData = CeroFiaoIcons.Changes,
                onClick = { onMenuItemClick("Tasas") }
            )

            CornerMenuItem(
                label = "Deudas",
                iconData = CeroFiaoIcons.Receipt,
                onClick = { onMenuItemClick("Deudas") }
            )

            DashedDivider()

            CornerMenuItem(
                label = "Ajustes",
                iconData = CeroFiaoIcons.Settings,
                onLongClick = onMenuItemLongClick?.let { { it("Ajustes") } },
                onClick = { onMenuItemClick("Ajustes") }
            )
        }
            }
        }
    }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CornerMenuItem(
    label: String,
    iconData: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconList: List<androidx.compose.ui.graphics.vector.ImageVector>? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val colors = LocalCeroFiaoColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onLongClick != null) {
                    Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onLongClick = onLongClick,
                        onClick = onClick
                    )
                } else {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() }
                }
            )
            .padding(
                vertical = CeroFiaoDesign.componentSize.cornerMenuItemPaddingVertical,  // ComponentSize.cornerMenuItemPaddingVertical
                horizontal = CeroFiaoDesign.componentSize.cornerMenuItemPaddingHorizontal // ComponentSize.cornerMenuItemPaddingHorizontal
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
    val lineColor = LocalCeroFiaoColors.current.TextPrimary
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
