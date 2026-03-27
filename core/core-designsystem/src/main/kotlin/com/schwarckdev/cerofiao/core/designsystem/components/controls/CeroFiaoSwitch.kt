package com.schwarckdev.cerofiao.core.designsystem.components.controls

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Toggle on/off switch. Replaces HeroUI's Switch component.
 */
@Composable
fun CeroFiaoSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = null
) {
    val t = CeroFiaoDesign.colors

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        thumbContent = thumbContent,
        colors = SwitchDefaults.colors(
            checkedThumbColor = t.OnPrimary,
            checkedTrackColor = t.Primary,
            uncheckedThumbColor = t.TextSecondary,
            uncheckedTrackColor = t.SurfaceVariant,
            uncheckedBorderColor = t.CardBorder,
            disabledCheckedThumbColor = t.InactiveColor,
            disabledCheckedTrackColor = t.SurfaceVariant,
            disabledUncheckedThumbColor = t.InactiveColor,
            disabledUncheckedTrackColor = t.SurfaceVariant
        )
    )
}
