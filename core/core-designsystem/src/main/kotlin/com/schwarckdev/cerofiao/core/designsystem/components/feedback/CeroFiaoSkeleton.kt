package com.schwarckdev.cerofiao.core.designsystem.components.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.Spacing

enum class SkeletonAnimation {
    Shimmer,
    Pulse
}

/**
 * Loading placeholder with shimmer or pulse animation.
 * Replaces HeroUI's Skeleton component.
 */
@Composable
fun CeroFiaoSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(CeroFiaoDesign.radius.sm),
    animation: SkeletonAnimation = SkeletonAnimation.Shimmer,
    baseColor: Color = CeroFiaoDesign.colors.SurfaceVariant,
    highlightColor: Color = CeroFiaoDesign.colors.Surface
) {
    val transition = rememberInfiniteTransition(label = "SkeletonTransition")

    when (animation) {
        SkeletonAnimation.Shimmer -> {
            val shimmerOffset by transition.animateFloat(
                initialValue = -1f,
                targetValue = 2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ShimmerOffset"
            )

            Box(
                modifier = modifier
                    .clip(shape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(baseColor, highlightColor, baseColor),
                            start = Offset(shimmerOffset * 300f, 0f),
                            end = Offset((shimmerOffset + 1f) * 300f, 0f)
                        )
                    )
            )
        }

        SkeletonAnimation.Pulse -> {
            val alpha by transition.animateFloat(
                initialValue = 1f,
                targetValue = 0.4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 800),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "PulseAlpha"
            )

            Box(
                modifier = modifier
                    .clip(shape)
                    .graphicsLayer { this.alpha = alpha }
                    .background(baseColor)
            )
        }
    }
}

/**
 * Coordinates multiple skeleton loading placeholders with staggered animations.
 * Replaces HeroUI's SkeletonGroup component.
 */
@Composable
fun CeroFiaoSkeletonGroup(
    count: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = Spacing.sm,
    content: @Composable (index: Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(count) { index ->
            content(index)
        }
    }
}
