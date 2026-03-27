package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * OTP input with individual character slots.
 * Replaces HeroUI's InputOTP component.
 */
@Composable
fun CeroFiaoInputOTP(
    length: Int = 6,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
) {
    val t = CeroFiaoDesign.colors
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(modifier = modifier) {
        // Hidden text field for actual input handling
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= length && newValue.all { it.isDigit() }) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .size(0.dp)
                .focusRequester(focusRequester),
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle.Default.copy(color = t.TextPrimary),
            cursorBrush = SolidColor(t.Primary)
        )

        // Visual slots
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(length) { index ->
                val char = value.getOrNull(index)
                val isFocused = value.length == index

                val borderColor = when {
                    isError -> t.Error
                    isFocused -> t.Primary
                    else -> t.CardBorder
                }

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(CeroFiaoDesign.radius.md),
                    color = t.SurfaceVariant,
                    border = BorderStroke(if (isFocused) 2.dp else 1.dp, borderColor)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (char != null) {
                            Text(
                                text = char.toString(),
                                color = t.TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        } else if (isFocused) {
                            Box(
                                modifier = Modifier.size(width = 2.dp, height = 24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
