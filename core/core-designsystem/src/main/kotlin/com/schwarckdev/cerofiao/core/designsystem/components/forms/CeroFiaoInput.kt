package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes

enum class InputVariant {
    Primary,
    Secondary
}

/**
 * Low-level styled text input. Replaces HeroUI's Input component.
 */
@Composable
fun CeroFiaoInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: InputVariant = InputVariant.Primary,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    val t = CeroFiaoDesign.colors

    val backgroundColor = when (variant) {
        InputVariant.Primary -> t.SurfaceVariant
        InputVariant.Secondary -> t.Surface
    }

    val borderColor = when {
        isError -> t.Error
        else -> t.CardBorder
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
        color = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = if (minLines > 1) Alignment.Top else Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(12.dp))
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        color = t.TextSecondary,
                        fontSize = 15.sp
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (minLines > 1) Modifier.height((minLines * 24).dp) else Modifier),
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    enabled = enabled,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    textStyle = TextStyle(
                        color = if (enabled) t.TextPrimary else t.TextSecondary,
                        fontSize = 15.sp
                    ),
                    cursorBrush = SolidColor(t.Primary)
                )
            }

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(12.dp))
                trailingIcon()
            }
        }
    }
}
