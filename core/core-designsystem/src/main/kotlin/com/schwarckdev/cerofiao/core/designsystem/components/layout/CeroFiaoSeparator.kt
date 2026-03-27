package com.schwarckdev.cerofiao.core.designsystem.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

enum class SeparatorOrientation {
    Horizontal,
    Vertical
}

/**
 * Visual separator line. Replaces HeroUI's Separator and enhances CeroFiaoDivider.
 */
@Composable
fun CeroFiaoSeparator(
    modifier: Modifier = Modifier,
    orientation: SeparatorOrientation = SeparatorOrientation.Horizontal,
    thickness: Dp = 1.dp,
    color: Color = CeroFiaoDesign.colors.CardBorder,
    inset: Dp = 0.dp
) {
    val insetModifier = when (orientation) {
        SeparatorOrientation.Horizontal -> Modifier.padding(horizontal = inset)
        SeparatorOrientation.Vertical -> Modifier.padding(vertical = inset)
    }

    val sizeModifier = when (orientation) {
        SeparatorOrientation.Horizontal -> Modifier.fillMaxWidth().height(thickness)
        SeparatorOrientation.Vertical -> Modifier.fillMaxHeight().width(thickness)
    }

    Box(
        modifier = modifier
            .then(insetModifier)
            .then(sizeModifier)
            .background(color)
    )
}
