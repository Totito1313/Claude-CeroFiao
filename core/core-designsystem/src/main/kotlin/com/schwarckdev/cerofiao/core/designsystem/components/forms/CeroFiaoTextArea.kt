package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Multiline text input with label, description, error, and character counter.
 * Replaces HeroUI's TextArea component.
 */
@Composable
fun CeroFiaoTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    description: String? = null,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    minLines: Int = 3,
    maxLines: Int = Int.MAX_VALUE,
    maxLength: Int? = null,
    enabled: Boolean = true,
    variant: InputVariant = InputVariant.Primary
) {
    val effectiveOnValueChange: (String) -> Unit = if (maxLength != null) {
        { newValue -> if (newValue.length <= maxLength) onValueChange(newValue) }
    } else {
        onValueChange
    }

    Column(modifier = modifier) {
        if (label != null) {
            CeroFiaoLabel(
                text = label,
                isRequired = isRequired,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )
        }

        CeroFiaoInput(
            value = value,
            onValueChange = effectiveOnValueChange,
            variant = variant,
            placeholder = placeholder,
            singleLine = false,
            minLines = minLines,
            maxLines = maxLines,
            enabled = enabled,
            isError = errorMessage != null
        )

        Row(modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)) {
            if (description != null && errorMessage == null) {
                CeroFiaoDescription(
                    text = description,
                    modifier = Modifier.weight(1f)
                )
            } else {
                CeroFiaoFieldError(
                    message = errorMessage,
                    modifier = Modifier.weight(1f)
                )
            }

            if (maxLength != null) {
                Text(
                    text = "${value.length}/$maxLength",
                    color = CeroFiaoDesign.colors.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
