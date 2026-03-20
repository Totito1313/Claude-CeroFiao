package com.SchwarckDev.cerofiao.core.designsystem.components.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.SchwarckDev.cerofiao.core.designsystem.components.advancedShadow
import androidx.compose.material3.Icon
import com.SchwarckDev.cerofiao.core.designsystem.theme.CeroFiaoTextStyles
import com.SchwarckDev.cerofiao.core.designsystem.theme.ComponentSize
import com.SchwarckDev.cerofiao.core.designsystem.theme.LocalBlurEnabled
import com.SchwarckDev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import com.SchwarckDev.cerofiao.core.designsystem.theme.LocalGlassConfig
import com.SchwarckDev.cerofiao.core.designsystem.theme.Radius
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild

/**
 * Floating Navigation Bar Component
 * Premium glassmorphism style with real backdrop blur using Haze library
 */
@Composable
fun CeroFiaoFloatingNavBar(
    activeTab: Int,
    isMenuOpen: Boolean,
    onTabSelected: (Int) -> Unit,
    onMenuClick: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val targetIndex = if (isMenuOpen) 4 else activeTab
    val colors = LocalCeroFiaoColors.current
    val isBlurEnabled = LocalBlurEnabled.current
    val glassConfig = LocalGlassConfig.current

    val indicatorOffset by animateDpAsState(
        targetValue = (if (targetIndex != -1) targetIndex * ComponentSize.navItemWidth.value else 0f).dp,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow),
        label = "indicatorOffset"
    )

    val indicatorAlpha by animateFloatAsState(
        targetValue = if (targetIndex != -1) 1f else 0f,
        animationSpec = tween(300),
        label = "indicatorAlpha"
    )

    val shape = RoundedCornerShape(Radius.navBar)
    
    // Outer Box: Handles external modifiers and layout inflation
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
        // Inner Box: Actual component with shadow and size
        Box(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .advancedShadow(
                    color = colors.ShadowColor,
                    alpha = 0.25f,
                    cornersRadius = Radius.navBar,
                    shadowBlurRadius = 15.dp,
                    offsetY = 4.dp
                )
        ) {
            // Content Box: Visuals and interactions
            Box(
                modifier = Modifier
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
                    .background(colors.GlassBackground.copy(alpha = if (isBlurEnabled) glassConfig.tintAlpha else 1f))
                    .border(1.dp, colors.CardBorder.copy(alpha = glassConfig.borderOpacity), shape)
            ) {
                // Sliding Active Indicator
                Box(
                    modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .offset(x = indicatorOffset)
                            .width(ComponentSize.navItemWidth)
                            .fillMaxHeight()
                            .alpha(indicatorAlpha)
                            .clip(RoundedCornerShape(Radius.navBar))
                            .background(colors.ActiveItemBackground)
                    )

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavItem(
                            label = "Inicio",
                            isActive = activeTab == 0 && !isMenuOpen,
                            onClick = { onTabSelected(0) }
                        ) { color ->
                            Box(Modifier.fillMaxSize()) {
                                Icon(imageVector = CeroFiaoIcons.Home, contentDescription = null, tint = color)
                            }
                        }

                        NavItem(
                            label = "Gestión",
                            isActive = activeTab == 1 && !isMenuOpen,
                            onClick = { onTabSelected(1) }
                        ) { color ->
                            Icon(imageVector = CeroFiaoIcons.ManageStorage, contentDescription = null, tint = color)
                        }
                        NavItem(
                            label = "Historial",
                            isActive = activeTab == 2 && !isMenuOpen,
                            onClick = { onTabSelected(2) }
                        ) { color ->
                            Box(Modifier.fillMaxSize()) {
                                Icon(imageVector = CeroFiaoIcons.Receipt, contentDescription = null, tint = color)
                            }
                        }

                        NavItem(
                            label = "Tasas",
                            isActive = activeTab == 3 && !isMenuOpen,
                            onClick = { onTabSelected(3) }
                        ) { color ->
                            Icon(imageVector = CeroFiaoIcons.Chages, contentDescription = null, tint = color)
                        }

                        NavItem(
                            label = "Menú",
                            isActive = isMenuOpen,
                            onClick = { onMenuClick() }
                        ) { color ->
                            Icon(imageVector = CeroFiaoIcons.Apps, contentDescription = null, tint = color)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    iconContent: @Composable (Color) -> Unit
) {
    val colors = LocalCeroFiaoColors.current

    Box(
        modifier = Modifier
            .width(ComponentSize.navItemWidth)
            .clip(RoundedCornerShape(Radius.navBar))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(
                vertical = ComponentSize.navItemPaddingVertical,
                horizontal = ComponentSize.navItemPaddingHorizontal
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inactive Content
        AnimatedVisibility(
            visible = !isActive,
            enter = fadeIn(tween(200)) + scaleIn(initialScale = 0.8f),
            exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.8f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(ComponentSize.navItemWidth - 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    iconContent(colors.InactiveColor)
                }
                Text(
                    text = label,
                    style = CeroFiaoTextStyles.NavLabel,
                    color = colors.InactiveColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }

        // Active Content
        AnimatedVisibility(
            visible = isActive,
            enter = fadeIn(tween(400)) + scaleIn(
                initialScale = 0.0f,
                animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMediumLow)
            ),
            exit = fadeOut(tween(200))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(ComponentSize.navItemWidth - 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    iconContent(colors.TextPrimary)
                }
                Text(
                    text = label,
                    style = CeroFiaoTextStyles.NavLabel,
                    color = colors.TextPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}



@Preview
@Composable
private fun PreviewFloatingNav() {
    val hazeState = remember { HazeState() }
    val colors = LocalCeroFiaoColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.Background),
        contentAlignment = Alignment.Center
    ) {
        CeroFiaoFloatingNavBar(
            activeTab = 0,
            isMenuOpen = false,
            onTabSelected = {},
            onMenuClick = {},
            hazeState = hazeState
        )
    }
}
