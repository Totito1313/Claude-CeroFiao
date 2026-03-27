package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Helper/description text for form fields.
 * Replaces HeroUI's Description component.
 */
@Composable
fun CeroFiaoDescription(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = CeroFiaoDesign.colors.TextSecondary
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
}
