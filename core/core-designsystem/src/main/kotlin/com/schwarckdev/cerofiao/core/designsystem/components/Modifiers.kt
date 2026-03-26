package com.schwarckdev.cerofiao.core.designsystem.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalShadowConfig

import android.annotation.SuppressLint

/**
 * Advanced shadow modifier using native Paint for real blur shadows matching LionFitness style.
 * Respects [LocalShadowConfig] — when shadows are disabled, this modifier does nothing.
 */
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 0.2f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
): Modifier = composed {
    val shadowConfig = LocalShadowConfig.current
    if (!shadowConfig.enabled) return@composed Modifier

    Modifier.drawBehind {
        val shadowColor = color.copy(alpha = alpha).toArgb()
        val transparentColor = color.copy(alpha = 0f).toArgb()

        drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparentColor
            frameworkPaint.setShadowLayer(
                shadowBlurRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f, 0f,
                this.size.width, this.size.height,
                cornersRadius.toPx(), cornersRadius.toPx(),
                paint
            )
        }
    }
}

/**
 * Bounce click modifier with scale-down animation and haptic feedback.
 */
@Composable
fun Modifier.bounceClick(
    onClick: () -> Unit,
    scaleDown: Float = 0.90f
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        label = "BounceAnimation",
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
    )
    val haptic = LocalHapticFeedback.current

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
        )
}
