package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Checkbox with optional label and description.
 * Replaces HeroUI's Checkbox component.
 */
@Composable
fun CeroFiaoCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    description: String? = null
) {
    val t = CeroFiaoDesign.colors

    Row(
        modifier = modifier
            .then(
                if (onCheckedChange != null && label != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = enabled,
                        onClick = { onCheckedChange(!checked) }
                    )
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = t.Primary,
                uncheckedColor = t.TextSecondary,
                checkmarkColor = t.OnPrimary,
                disabledCheckedColor = t.InactiveColor,
                disabledUncheckedColor = t.InactiveColor
            )
        )

        if (label != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    color = if (enabled) t.TextPrimary else t.InactiveColor,
                    fontSize = 15.sp
                )
                if (description != null) {
                    Text(
                        text = description,
                        color = t.TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
