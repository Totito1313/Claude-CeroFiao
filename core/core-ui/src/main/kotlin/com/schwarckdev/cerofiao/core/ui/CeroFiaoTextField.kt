package com.schwarckdev.cerofiao.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

@Composable
fun CeroFiaoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val t = CeroFiaoTheme.tokens

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                color = t.textSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
            color = t.inputBg,
            border = BorderStroke(1.dp, t.inputBorder)
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
                            color = t.placeholder,
                            fontSize = 15.sp
                        )
                    }
                    
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth().then(if (minLines > 1) Modifier.height((minLines * 24).dp) else Modifier),
                        singleLine = singleLine,
                        minLines = minLines,
                        keyboardOptions = keyboardOptions,
                        textStyle = TextStyle(
                            color = t.text,
                            fontSize = 15.sp
                        ),
                        cursorBrush = SolidColor(t.text)
                    )
                }

                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    trailingIcon()
                }
            }
        }
    }
}
