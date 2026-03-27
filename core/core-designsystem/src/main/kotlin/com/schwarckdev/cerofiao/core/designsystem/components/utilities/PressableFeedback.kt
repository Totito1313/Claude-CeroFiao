package com.schwarckdev.cerofiao.core.designsystem.components.utilities

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

enum class FeedbackVariant {
    ScaleHighlight,
    ScaleRipple,
    Scale,
    None
}

/**
 * Container that provides visual press feedback with configurable animation.
 * Replaces HeroUI's PressableFeedback component.
 */
@Composable
fun PressableFeedback(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: FeedbackVariant = FeedbackVariant.ScaleHighlight,
    scaleDown: Float = 0.96f,
    highlightColor: Color = CeroFiaoDesign.colors.Primary.copy(alpha = 0.08f),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed && variant != FeedbackVariant.None) scaleDown else 1f,
        label = "PressableFeedbackScale",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val scaleModifier = if (variant != FeedbackVariant.None) {
        Modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    } else Modifier

    val highlightModifier = if (isPressed && variant == FeedbackVariant.ScaleHighlight) {
        Modifier.background(highlightColor, RoundedCornerShape(8.dp))
    } else Modifier

    val indication = when (variant) {
        FeedbackVariant.ScaleRipple -> ripple()
        FeedbackVariant.None -> ripple()
        else -> null
    }

    Box(
        modifier = modifier
            .then(scaleModifier)
            .then(highlightModifier)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = {
                    if (variant != FeedbackVariant.None) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                    onClick()
                }
            )
    ) {
        content()
    }
}

/**
 * Modifier extension for press feedback — backward-compatible shortcut.
 */
@Composable
fun Modifier.pressableFeedback(
    onClick: () -> Unit,
    variant: FeedbackVariant = FeedbackVariant.Scale,
    scaleDown: Float = 0.90f,
    enabled: Boolean = true
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed && variant != FeedbackVariant.None) scaleDown else 1f,
        label = "PressableFeedbackModifier",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val indication = when (variant) {
        FeedbackVariant.ScaleRipple -> ripple()
        else -> null
    }

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            onClick = {
                if (variant != FeedbackVariant.None) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                onClick()
            }
        )
}
