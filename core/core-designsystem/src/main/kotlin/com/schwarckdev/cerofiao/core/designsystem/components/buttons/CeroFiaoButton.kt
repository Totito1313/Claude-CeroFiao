package com.schwarckdev.cerofiao.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.DangerGradient
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes

enum class ButtonVariant {
    Primary,
    Secondary,
    Tertiary,
    Outline,
    Ghost,
    Danger,
    DangerSoft
}

enum class ButtonSize {
    Small,
    Medium,
    Large
}

/**
 * CeroFiao unified button — 7 variants, 3 sizes, icon-only mode, loading state.
 * Sizes and padding match HeroUI Figma Kit V3 specs.
 */
@Composable
fun CeroFiaoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Medium,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    feedback: FeedbackVariant = FeedbackVariant.ScaleHighlight,
    shape: Shape? = null,
    contentPadding: PaddingValues? = null,
    content: (@Composable () -> Unit)? = null
) {
    val t = CeroFiaoDesign.colors

    // Figma: Primary=40px, Small=36px (ButtonGroup), Large=56px
    val height = when (size) {
        ButtonSize.Small -> 36.dp
        ButtonSize.Medium -> 40.dp
        ButtonSize.Large -> 56.dp
    }

    // Figma: Primary=16sp medium, Small=14sp medium, Large=18sp medium
    val fontSize = when (size) {
        ButtonSize.Small -> 14.sp
        ButtonSize.Medium -> 16.sp
        ButtonSize.Large -> 18.sp
    }

    // Figma: icon 16px for all button sizes
    val iconSize = when (size) {
        ButtonSize.Small -> 16.dp
        ButtonSize.Medium -> 16.dp
        ButtonSize.Large -> 18.dp
    }

    val isIconOnly = text == null && icon != null && content == null

    val effectiveShape = shape ?: if (isIconOnly) {
        RoundedCornerShape(50)
    } else {
        RoundedCornerShape(CeroFiaoShapes.ButtonRadius)
    }

    // Figma: padding 8px vertical / 16px horizontal for Medium
    val effectivePadding = contentPadding ?: if (isIconOnly) {
        PaddingValues(0.dp)
    } else {
        when (size) {
            ButtonSize.Small -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ButtonSize.Medium -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ButtonSize.Large -> PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        }
    }

    val disabledBrush = Brush.linearGradient(listOf(t.SurfaceVariant, t.SurfaceVariant))

    // Figma: Primary uses solid Primary color (not gradient)
    val backgroundModifier = when (variant) {
        ButtonVariant.Danger -> Modifier.background(
            if (enabled) DangerGradient else disabledBrush
        )
        ButtonVariant.DangerSoft -> Modifier.background(
            if (enabled) t.DangerSoft else t.SurfaceVariant
        )
        else -> Modifier
    }

    val contentColor = when (variant) {
        ButtonVariant.Primary -> if (enabled) t.OnPrimary else t.InactiveColor
        ButtonVariant.Danger -> if (enabled) Color.White else t.InactiveColor
        ButtonVariant.Secondary -> if (enabled) t.TextPrimary else t.InactiveColor
        ButtonVariant.Tertiary -> if (enabled) t.TextSecondary else t.InactiveColor
        ButtonVariant.Outline -> if (enabled) t.TextPrimary else t.InactiveColor
        ButtonVariant.Ghost -> if (enabled) t.TextPrimary else t.InactiveColor
        ButtonVariant.DangerSoft -> if (enabled) t.Error else t.InactiveColor
    }

    val surfaceColor = when (variant) {
        ButtonVariant.Primary -> if (enabled) t.Primary else t.SurfaceVariant
        ButtonVariant.Secondary -> if (enabled) t.Surface else t.Surface.copy(alpha = 0.02f)
        ButtonVariant.Tertiary -> if (enabled) t.SurfaceVariant else t.SurfaceVariant.copy(alpha = 0.5f)
        ButtonVariant.Ghost, ButtonVariant.Danger -> Color.Transparent
        ButtonVariant.Outline -> Color.Transparent
        ButtonVariant.DangerSoft -> Color.Transparent
    }

    val surfaceBorder = when (variant) {
        ButtonVariant.Secondary -> BorderStroke(1.dp, if (enabled) t.CardBorder else t.CardBorder.copy(alpha = 0.02f))
        ButtonVariant.Outline -> BorderStroke(1.dp, if (enabled) t.CardBorder else t.CardBorder.copy(alpha = 0.02f))
        else -> null
    }

    val minSize = if (isIconOnly) height else Dp.Unspecified

    Surface(
        modifier = modifier.clip(effectiveShape),
        shape = effectiveShape,
        color = surfaceColor,
        border = surfaceBorder
    ) {
        Box(
            modifier = Modifier
                .then(backgroundModifier)
                .defaultMinSize(minWidth = minSize, minHeight = height)
                .then(
                    if (enabled && !isLoading) {
                        Modifier.pressableFeedback(
                            onClick = onClick,
                            variant = feedback
                        )
                    } else {
                        Modifier.clickable(enabled = false, onClick = {})
                    }
                )
                .padding(effectivePadding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(iconSize),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else if (content != null) {
                content()
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(iconSize)
                        )
                        if (text != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    if (text != null) {
                        Text(
                            text = text,
                            color = contentColor,
                            fontSize = fontSize,
                            fontWeight = FontWeight.Medium,
                            lineHeight = when (size) {
                                ButtonSize.Small -> 20.sp
                                ButtonSize.Medium -> 24.sp
                                ButtonSize.Large -> 28.sp
                            },
                        )
                    }
                }
            }
        }
    }
}
