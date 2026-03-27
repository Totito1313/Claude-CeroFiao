package com.schwarckdev.cerofiao.core.designsystem.components.feedback

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.ComponentSize

enum class SpinnerSize {
    Small,
    Medium,
    Large
}

/**
 * Animated loading indicator. Replaces HeroUI's Spinner component.
 */
@Composable
fun CeroFiaoSpinner(
    modifier: Modifier = Modifier,
    size: SpinnerSize = SpinnerSize.Medium,
    color: Color = CeroFiaoDesign.colors.Primary,
    strokeWidth: Dp? = null
) {
    val dimension = when (size) {
        SpinnerSize.Small -> ComponentSize.spinnerSmall
        SpinnerSize.Medium -> ComponentSize.spinnerMedium
        SpinnerSize.Large -> ComponentSize.spinnerLarge
    }

    val effectiveStroke = strokeWidth ?: when (size) {
        SpinnerSize.Small -> 1.5.dp
        SpinnerSize.Medium -> 2.dp
        SpinnerSize.Large -> 3.dp
    }

    CircularProgressIndicator(
        modifier = modifier.size(dimension),
        color = color,
        strokeWidth = effectiveStroke
    )
}
