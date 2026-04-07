package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.TransferGradient

@Composable
fun HistoricalRateChart(
    dataPoints: List<Double>,
    modifier: Modifier = Modifier,
    lineBrush: Brush = TransferGradient,
    fillAlpha: Float = 0.15f,
    animate: Boolean = true,
) {
    if (dataPoints.size < 2) return

    val animationProgress = remember { Animatable(if (animate) 0f else 1f) }

    LaunchedEffect(dataPoints, animate) {
        if (animate) {
            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        } else {
            animationProgress.snapTo(1f)
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = height * 0.1f

        val minVal = dataPoints.min()
        val maxVal = dataPoints.max()
        val range = (maxVal - minVal).coerceAtLeast(0.01)

        val points = dataPoints.mapIndexed { index, value ->
            val x = width * index / (dataPoints.size - 1).toFloat()
            val targetY = height - padding - ((value - minVal) / range).toFloat() * (height - padding * 2)
            
            // Rise up animation
            val animatedY = height - (height - targetY) * animationProgress.value
            Offset(x, animatedY)
        }

        // Build bezier curve path
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
                val p0 = points[i]
                val p1 = points[i + 1]
                val cx1 = p0.x + (p1.x - p0.x) / 3f
                val cx2 = p0.x + 2f * (p1.x - p0.x) / 3f
                cubicTo(cx1, p0.y, cx2, p1.y, p1.x, p1.y)
            }
        }

        // Draw line stroke
        drawPath(
            path = linePath,
            brush = lineBrush,
            style = Stroke(width = 2.5f.dp.toPx(), cap = StrokeCap.Round),
        )

        // Draw filled area under curve
        val fillPath = Path().apply {
            addPath(linePath)
            lineTo(points.last().x, height)
            lineTo(points.first().x, height)
            close()
        }

        val fillBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF66A1F3).copy(alpha = 0.4f * animationProgress.value),
                Color.Transparent
            ),
            startY = padding,
            endY = height
        )

        drawPath(
            path = fillPath,
            brush = fillBrush,
        )
    }
}
