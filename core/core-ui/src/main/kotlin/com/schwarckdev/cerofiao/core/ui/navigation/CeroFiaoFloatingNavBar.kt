package com.schwarckdev.cerofiao.core.ui.navigation

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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

data class FloatingNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
)

/**
 * Floating Navigation Bar Component
 * Premium glassmorphism style
 */
@Composable
fun CeroFiaoFloatingNavBar(
    items: List<FloatingNavItem>,
    activeRouteClass: kotlin.reflect.KClass<out Any>?,
    onTabSelected: (FloatingNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = CeroFiaoTheme.tokens
    val activeIndex = items.indexOfFirst { it.route::class == activeRouteClass }.takeIf { it >= 0 } ?: 0
    
    val navItemWidth = 68.dp
    val navBarRadius = 28.dp

    val indicatorOffset by animateDpAsState(
        targetValue = (activeIndex * navItemWidth.value).dp,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow),
        label = "indicatorOffset"
    )

    val indicatorAlpha by animateFloatAsState(
        targetValue = if (activeIndex != -1) 1f else 0f,
        animationSpec = tween(300),
        label = "indicatorAlpha"
    )

    val shape = RoundedCornerShape(navBarRadius)
    
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
                .shadow(
                    elevation = 15.dp,
                    shape = shape,
                    spotColor = tokens.navBg.copy(alpha = 0.5f),
                    ambientColor = tokens.navBg.copy(alpha = 0.5f)
                )
        ) {
            // Content Box: Visuals and interactions
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(tokens.navBg.copy(alpha = 0.85f)) // Simulated glassmorphism
                    .border(1.dp, tokens.navBorder, shape)
            ) {
                // Sliding Active Indicator
                Box(
                    modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .offset(x = indicatorOffset)
                            .width(navItemWidth)
                            .fillMaxHeight()
                            .alpha(indicatorAlpha)
                            .clip(RoundedCornerShape(navBarRadius))
                            .background(tokens.success.copy(alpha = 0.2f)) // Subtle active background
                    )

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items.forEachIndexed { index, item ->
                            val isActive = index == activeIndex
                            
                            Box(
                                modifier = Modifier
                                    .width(navItemWidth)
                                    .clip(RoundedCornerShape(navBarRadius))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { onTabSelected(item) }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Inactive Content
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = !isActive,
                                    enter = fadeIn(tween(200)) + scaleIn(initialScale = 0.8f),
                                    exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.8f)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier.size(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(imageVector = item.icon, contentDescription = null, tint = tokens.textMuted)
                                        }
                                        Text(
                                            text = item.label,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = tokens.textMuted,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }

                                // Active Content
                                androidx.compose.animation.AnimatedVisibility(
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
                                            modifier = Modifier.size(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(imageVector = item.icon, contentDescription = null, tint = tokens.success)
                                        }
                                        Text(
                                            text = item.label,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = tokens.success,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
