package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Form field label with optional required indicator.
 * Replaces HeroUI's Label component.
 */
@Composable
fun CeroFiaoLabel(
    text: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    requiredIndicator: String = "*"
) {
    Row(modifier = modifier) {
        Text(
            text = text,
            color = CeroFiaoDesign.colors.TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        if (isRequired) {
            Text(
                text = " $requiredIndicator",
                color = CeroFiaoDesign.colors.Error,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
