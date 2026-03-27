package com.schwarckdev.cerofiao.core.designsystem.components.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Elevation + background container. Replaces HeroUI's Surface component.
 * Wraps Material 3 Surface with CeroFiao design tokens.
 */
@Composable
fun CeroFiaoSurface(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    color: Color = CeroFiaoDesign.colors.Surface,
    shape: Shape = RoundedCornerShape(CeroFiaoDesign.radius.card),
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        tonalElevation = elevation,
        shadowElevation = elevation,
        border = border
    ) {
        content()
    }
}
