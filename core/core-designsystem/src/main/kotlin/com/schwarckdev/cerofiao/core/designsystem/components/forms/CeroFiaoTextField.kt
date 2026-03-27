package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Compound text field with label, description, and error handling.
 * Replaces HeroUI's TextField component and enhances the original CeroFiaoTextField.
 */
@Composable
fun CeroFiaoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    description: String? = null,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    enabled: Boolean = true,
    displayVariant: InputDisplayVariant = InputDisplayVariant.Flat,
    color: InputColor = InputColor.Default,
    size: InputSize = InputSize.Md,
    labelPlacement: InputLabelPlacement = InputLabelPlacement.Outside,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        if (label != null && labelPlacement == InputLabelPlacement.Outside) {
            CeroFiaoLabel(
                text = label,
                isRequired = isRequired,
                color = when (color) {
                    InputColor.Default -> LabelColor.Default
                    InputColor.Primary -> LabelColor.Primary
                    InputColor.Success -> LabelColor.Success
                    InputColor.Warning -> LabelColor.Warning
                    InputColor.Danger -> LabelColor.Danger
                },
                size = when (size) {
                    InputSize.Sm -> LabelSize.Sm
                    InputSize.Md -> LabelSize.Md
                    InputSize.Lg -> LabelSize.Lg
                },
                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp),
            )
        }

        CeroFiaoInput(
            value = value,
            onValueChange = onValueChange,
            label = if (labelPlacement == InputLabelPlacement.Inside) label else null,
            labelPlacement = labelPlacement,
            displayVariant = displayVariant,
            color = color,
            size = size,
            placeholder = placeholder,
            singleLine = singleLine,
            minLines = minLines,
            enabled = enabled,
            isError = errorMessage != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )

        if (description != null && errorMessage == null) {
            Spacer(modifier = Modifier.height(4.dp))
            CeroFiaoDescription(
                text = description,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        CeroFiaoFieldError(
            message = errorMessage,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )
    }
}
