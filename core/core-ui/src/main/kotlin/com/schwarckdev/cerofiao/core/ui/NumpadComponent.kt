package com.schwarckdev.cerofiao.core.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

sealed interface NumpadKey {
    data class Digit(val value: String) : NumpadKey
    data class Operator(val symbol: String) : NumpadKey
    data object Decimal : NumpadKey
    data object Backspace : NumpadKey
    data object Equals : NumpadKey
    data object Clear : NumpadKey
}

@Composable
fun NumpadComponent(
    onKeyPress: (NumpadKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    val keys = listOf(
        listOf(NumpadKey.Digit("7"), NumpadKey.Digit("8"), NumpadKey.Digit("9"), NumpadKey.Operator("×")),
        listOf(NumpadKey.Digit("4"), NumpadKey.Digit("5"), NumpadKey.Digit("6"), NumpadKey.Operator("-")),
        listOf(NumpadKey.Digit("1"), NumpadKey.Digit("2"), NumpadKey.Digit("3"), NumpadKey.Operator("+")),
        listOf(NumpadKey.Decimal, NumpadKey.Digit("0"), NumpadKey.Backspace, NumpadKey.Equals),
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { key ->
                    NumpadButton(
                        key = key,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onKeyPress(key)
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun NumpadButton(
    key: NumpadKey,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOperator = key is NumpadKey.Operator || key is NumpadKey.Equals
    val backgroundColor = when {
        key is NumpadKey.Equals -> MaterialTheme.colorScheme.primary
        isOperator -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when {
        key is NumpadKey.Equals -> MaterialTheme.colorScheme.onPrimary
        isOperator -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val label = when (key) {
        is NumpadKey.Digit -> key.value
        is NumpadKey.Operator -> key.symbol
        NumpadKey.Decimal -> ","
        NumpadKey.Backspace -> "⌫"
        NumpadKey.Equals -> "="
        NumpadKey.Clear -> "C"
    }

    Box(
        modifier = modifier
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}
