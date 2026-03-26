package com.schwarckdev.cerofiao.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCardConfig
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors

/**
 * GlassCard — Frosted glass card from Figma design.
 *
 * Variants:
 * - default: glass bg with subtle border
 * - elevated: surface-elevated with border + shadow
 * - accent: accent-soft bg with accent border
 * - primary: solid primary bg
 */
enum class GlassCardVariant { Default, Elevated, Accent, Primary }

@Composable
fun CeroFiaoGlassCard(
    modifier: Modifier = Modifier,
    variant: GlassCardVariant = GlassCardVariant.Default,
    cornerRadius: Dp? = null, // null = use LocalCardConfig
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val extendedColors = LocalCeroFiaoColors.current
    val cardConfig = LocalCardConfig.current

    // Use explicit param if provided, otherwise read from global card config
    val effectiveCornerRadius = cornerRadius ?: cardConfig.cornerRadius
    val shape = RoundedCornerShape(effectiveCornerRadius)

    val colors: Pair<androidx.compose.ui.graphics.Color, androidx.compose.ui.graphics.Color> = when (variant) {
        GlassCardVariant.Default -> {
            extendedColors.CardBackground.copy(alpha = cardConfig.backgroundOpacity) to
                    extendedColors.CardBorder.copy(alpha = cardConfig.borderOpacity)
        }
        GlassCardVariant.Elevated -> {
            extendedColors.CardBackground.copy(alpha = cardConfig.backgroundOpacity) to
                    extendedColors.CardBorder.copy(alpha = cardConfig.borderOpacity)
        }
        GlassCardVariant.Accent -> {
            val accentSoft = extendedColors.Primary.copy(alpha = 0.12f * cardConfig.backgroundOpacity)
            val accentBorder = extendedColors.Primary.copy(alpha = 0.2f * cardConfig.borderOpacity)
            accentSoft to accentBorder
        }
        GlassCardVariant.Primary -> {
            extendedColors.Primary to extendedColors.GlassBorder.copy(alpha = cardConfig.borderOpacity)
        }
    }
    val bgColor = colors.first
    val borderColor = colors.second

    val shadowElevation = when (variant) {
        GlassCardVariant.Elevated -> 8.dp
        GlassCardVariant.Primary -> 12.dp
        else -> 0.dp
    }

    // Build shadow modifier (non-composable, safe in .then())
    val shadowModifier = if (shadowElevation > 0.dp) {
        Modifier.advancedShadow(
            color = androidx.compose.ui.graphics.Color.Black,
            alpha = 0.15f,
            cornersRadius = effectiveCornerRadius,
            shadowBlurRadius = 15.dp,
            offsetY = 4.dp
        )
    } else Modifier

    // bounceClick is @Composable — must be called HERE in the composable scope
    val clickModifier = if (onClick != null) {
        Modifier.bounceClick(onClick = onClick)
    } else Modifier

    Box(
        modifier = modifier
            .then(shadowModifier)
            .then(clickModifier)
            .clip(shape)
            .background(bgColor)
            .border(cardConfig.borderWidth, borderColor, shape)
    ) {
        content()
    }
}
