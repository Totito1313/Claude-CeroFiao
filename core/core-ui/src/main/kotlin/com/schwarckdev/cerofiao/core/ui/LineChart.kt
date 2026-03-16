package com.schwarckdev.cerofiao.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ChartLine(
    val label: String,
    val points: List<Double>,
    val color: Color,
)

data class ChartData(
    val lines: List<ChartLine>,
    val dateLabels: List<String> = emptyList(),
)

@Composable
fun LineChart(
    data: ChartData,
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    if (data.lines.all { it.points.isEmpty() }) return

    val allPoints = data.lines.flatMap { it.points }
    val globalMin = remember(allPoints) { allPoints.minOrNull() ?: 0.0 }
    val globalMax = remember(allPoints) { allPoints.maxOrNull() ?: 1.0 }
    val range = (globalMax - globalMin).coerceAtLeast(0.01)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                val w = size.width
                val h = size.height
                val paddingTop = 8f
                val paddingBottom = 8f
                val chartH = h - paddingTop - paddingBottom

                data.lines.forEach { line ->
                    if (line.points.size < 2) return@forEach

                    val path = Path()
                    val step = w / (line.points.size - 1).coerceAtLeast(1)

                    line.points.forEachIndexed { i, value ->
                        val x = i * step
                        val y = paddingTop + chartH - ((value - globalMin) / range * chartH).toFloat()
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }

                    drawPath(
                        path = path,
                        color = line.color,
                        style = Stroke(
                            width = 2.5f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Legend
            Row {
                data.lines.forEach { line ->
                    Row(modifier = Modifier.padding(end = 16.dp)) {
                        Canvas(modifier = Modifier.height(14.dp).width(14.dp)) {
                            drawCircle(color = line.color, radius = 5f)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = line.label,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }

            // Min/Max labels
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "Min: ${String.format("%.2f", globalMin)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Max: ${String.format("%.2f", globalMax)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Date range
            if (data.dateLabels.size >= 2) {
                Row {
                    Text(
                        text = data.dateLabels.first(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = data.dateLabels.last(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
