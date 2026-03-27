package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.Spacing

/**
 * Radio button group where only one option can be selected.
 * Replaces HeroUI's RadioGroup component.
 */
@Composable
fun <T> CeroFiaoRadioGroup(
    selected: T,
    onSelectedChange: (T) -> Unit,
    options: List<T>,
    modifier: Modifier = Modifier,
    label: ((T) -> String)? = null,
    description: ((T) -> String?)? = null,
    enabled: Boolean = true
) {
    val t = CeroFiaoDesign.colors

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        options.forEach { option ->
            Row(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = enabled,
                    onClick = { onSelectedChange(option) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option == selected,
                    onClick = { onSelectedChange(option) },
                    enabled = enabled,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = t.Primary,
                        unselectedColor = t.TextSecondary,
                        disabledSelectedColor = t.InactiveColor,
                        disabledUnselectedColor = t.InactiveColor
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = label?.invoke(option) ?: option.toString(),
                        color = if (enabled) t.TextPrimary else t.InactiveColor,
                        fontSize = 15.sp
                    )
                    val desc = description?.invoke(option)
                    if (desc != null) {
                        Text(
                            text = desc,
                            color = t.TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
