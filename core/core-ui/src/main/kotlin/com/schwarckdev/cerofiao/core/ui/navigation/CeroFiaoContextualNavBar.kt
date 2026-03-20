package com.schwarckdev.cerofiao.core.ui.navigation

import com.schwarckdev.cerofiao.core.designsystem.theme.*
import dev.chrisbanes.haze.*


import androidx.compose.ui.layout.layout
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalBlurEnabled
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign



import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.drawscope.scale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild

/**
 * Contextual Navigation Bar (Figma Design)
 * Floating pill with sliding toggle for specific sections.
 * Pixel-perfect port from LionFitness ContextualNavBar.kt
 */
@Composable
fun CeroFiaoContextualNavBar(
    hazeState: HazeState,
    currentOption: Int,
    options: List<ContextualOption>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (options.isEmpty()) return

    val colors = CeroFiaoTheme.tokens

    val itemSize = CeroFiaoDesign.componentSize.topBarButtonSize
    val gap = CeroFiaoDesign.spacing.sm
    val horizontalPadding = CeroFiaoDesign.spacing.xs
    val verticalPadding = CeroFiaoDesign.spacing.xxs
    
    // Calculate total width: (N * itemSize) + ((N - 1) * gap) + (2 * horizontalPadding)
    val totalWidth = (itemSize * options.size) + (gap * (options.size - 1)) + (horizontalPadding * 2)
    val containerHeight = itemSize + (verticalPadding * 2)

    val containerShape = RoundedCornerShape(CeroFiaoDesign.radius.pill)
    val indicatorShape = RoundedCornerShape(CeroFiaoDesign.radius.circle)
    
    val glassConfig = LocalGlassConfig.current
    val isBlurEnabled = LocalBlurEnabled.current
    val backgroundColor = if (isBlurEnabled) Color.Transparent else colors.fondoMenus

    // Outer Box: Handles external modifiers (positioning) and layout inflation (reserving space for shadow)
    Box(
        modifier = modifier
            .layout { measurable, constraints ->
                val shadowPadding = 20.dp.roundToPx()
                val placeable = measurable.measure(constraints)
                layout(placeable.width + shadowPadding * 2, placeable.height + shadowPadding * 2) {
                    placeable.place(shadowPadding, shadowPadding)
                }
            }
    ) {
        // Inner Box: The actual component container with definitive size and shadow
        Box(
            modifier = Modifier
                .height(containerHeight)
                .width(totalWidth)
                .shadow(15.dp, androidx.compose.foundation.shape.RoundedCornerShape(25.dp))
        ) {
            // Content Box: Handles clipping, background, and internal content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(containerShape)
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
                    .background(backgroundColor)
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                contentAlignment = Alignment.CenterStart
            ) {
                // Sliding Indicator
                // Offset = index * (itemSize + gap)
                val targetOffset by animateDpAsState(
                    targetValue = (itemSize + gap) * currentOption,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "indicator"
                )

                Box(
                    modifier = Modifier
                        .offset(x = targetOffset)
                        .size(itemSize)
                        .clip(indicatorShape)
                        .background(colors.ActiveItemBackground)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    options.forEachIndexed { index, option ->
                        ContextualIconItem(
                            iconData = option.iconPath,
                            iconVector = option.iconVector,
                            isActive = currentOption == index,
                            onClick = { onOptionSelected(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContextualIconItem(
    iconData: String?,
    iconVector: ImageVector?,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val colors = CeroFiaoTheme.tokens

    Box(
        modifier = Modifier
            .size(CeroFiaoDesign.componentSize.topBarButtonSize)
            .clip(RoundedCornerShape(CeroFiaoDesign.radius.pill))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        val contentColor = if (isActive) colors.TextPrimary else colors.TextPrimary.copy(alpha = 0.5f)
        
        if (iconData != null) {
            DrawSvgPath(
                pathData = iconData,
                color = contentColor,
                viewportWidth = 24f,
                viewportHeight = 24f,
                modifier = Modifier.size(CeroFiaoDesign.iconSize.md) // Figma icon size
            )
        } else if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(CeroFiaoDesign.iconSize.md)
            )
        }
    }
}

// Helper for drawing SVG paths (copied/adapted from CornerMenu style)
@Composable
private fun DrawSvgPath(
    pathData: String,
    color: Color,
    viewportWidth: Float,
    viewportHeight: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val path = PathParser().parsePathString(pathData).toPath()
        val scaleX = size.width / viewportWidth
        val scaleY = size.height / viewportHeight
        
        scale(scaleX, scaleY, pivot = Offset.Zero) {
            drawPath(path = path, color = color)
        }
    }
}

data class ContextualOption(
    val iconPath: String? = null,
    val iconVector: ImageVector? = null,
    val label: String = "" // Keep for accessibility/debugging
)
