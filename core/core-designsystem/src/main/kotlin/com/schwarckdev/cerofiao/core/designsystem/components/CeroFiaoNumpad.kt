package com.schwarckdev.cerofiao.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CeroFiaoNumpad(
    expression: String,
    onExpressionChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keys = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf(".", "0", "DEL", "+")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LocalCeroFiaoColors.current.SurfaceVariant, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { key ->
                    NumpadKey(
                        text = key,
                        modifier = Modifier.weight(1f),
                        isOperator = isOperatorKey(key),
                        isAction = key == "DEL",
                        onClick = {
                            when (key) {
                                "DEL" -> {
                                    if (expression.isNotEmpty()) {
                                        onExpressionChange(expression.dropLast(1))
                                    }
                                }
                                "DONE" -> {
                                    onDone()
                                }
                                else -> {
                                    onExpressionChange(expression + key)
                                }
                            }
                        }
                    )
                }
            }
        }
        
        // Fila extra para el botón DONE / CHECK si queremos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NumpadKey(
                text = "Listo",
                modifier = Modifier.weight(1f),
                isOperator = false,
                isAction = true,
                contentColor = LocalCeroFiaoColors.current.OnPrimary,
                containerColor = LocalCeroFiaoColors.current.Primary,
                onClick = onDone
            )
        }
    }
}

@Composable
private fun NumpadKey(
    text: String,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isAction: Boolean = false,
    containerColor: Color = if (isOperator) LocalCeroFiaoColors.current.SurfaceVariant else LocalCeroFiaoColors.current.Surface,
    contentColor: Color = if (isOperator) LocalCeroFiaoColors.current.TextPrimary else LocalCeroFiaoColors.current.TextPrimary,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(if(text == "Listo") 4f else 1.2f) // Botón Listo más ancho
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

private fun isOperatorKey(key: String) = key == "+" || key == "-" || key == "*" || key == "/"
