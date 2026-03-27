package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes

/**
 * Groups an input with optional prefix and suffix decorators.
 * Replaces HeroUI's InputGroup component.
 */
@Composable
fun CeroFiaoInputGroup(
    modifier: Modifier = Modifier,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val t = CeroFiaoDesign.colors

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
        color = t.SurfaceVariant,
        border = BorderStroke(1.dp, t.CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (prefix != null) {
                Surface(
                    color = t.Surface,
                    modifier = Modifier.padding(end = 1.dp)
                ) {
                    prefix()
                }
            }

            content()

            if (suffix != null) {
                Surface(
                    color = t.Surface,
                    modifier = Modifier.padding(start = 1.dp)
                ) {
                    suffix()
                }
            }
        }
    }
}
