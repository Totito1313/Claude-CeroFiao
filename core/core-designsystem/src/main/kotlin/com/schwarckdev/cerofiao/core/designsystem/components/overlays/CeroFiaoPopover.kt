package com.schwarckdev.cerofiao.core.designsystem.components.overlays

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Floating content panel anchored to a trigger element.
 * Replaces HeroUI's Popover component.
 */
@Composable
fun CeroFiaoPopover(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    anchor: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.BottomCenter,
    content: @Composable () -> Unit
) {
    val t = CeroFiaoDesign.colors

    Box(modifier = modifier) {
        anchor()

        if (expanded) {
            Popup(
                alignment = alignment,
                onDismissRequest = onDismissRequest,
                properties = PopupProperties(focusable = true)
            ) {
                Surface(
                    shape = RoundedCornerShape(CeroFiaoDesign.radius.lg),
                    color = t.Surface,
                    shadowElevation = 8.dp,
                    border = BorderStroke(1.dp, t.CardBorder)
                ) {
                    Box(modifier = Modifier.padding(12.dp)) {
                        content()
                    }
                }
            }
        }
    }
}
