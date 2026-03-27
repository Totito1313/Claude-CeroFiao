package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/** Size scale for CeroFiaoLabel — mirrors HeroUI Label sizes */
enum class LabelSize { Sm, Md, Lg }

/** Color variants for CeroFiaoLabel — used to match the parent input's color state */
enum class LabelColor { Default, Primary, Success, Warning, Danger }

/**
 * Form field label with color variants, sizes, required indicator, and optional icon.
 * Matches HeroUI V3 Label component: 14sp medium, TextSecondary for default,
 * accent color when the parent field has a non-default color.
 */
@Composable
fun CeroFiaoLabel(
    text: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    requiredIndicator: String = "*",
    size: LabelSize = LabelSize.Md,
    color: LabelColor = LabelColor.Default,
    description: String? = null,
    startContent: (@Composable () -> Unit)? = null,
) {
    val t = CeroFiaoDesign.colors

    val fontSize: TextUnit = when (size) {
        LabelSize.Sm -> 12.sp
        LabelSize.Md -> 14.sp
        LabelSize.Lg -> 16.sp
    }
    val descFontSize: TextUnit = when (size) {
        LabelSize.Sm -> 11.sp
        LabelSize.Md -> 12.sp
        LabelSize.Lg -> 13.sp
    }

    val labelColor: Color = when (color) {
        LabelColor.Default -> t.TextSecondary
        LabelColor.Primary -> t.Primary
        LabelColor.Success -> t.Success
        LabelColor.Warning -> t.Warning
        LabelColor.Danger -> t.Error
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (startContent != null) {
                startContent()
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = labelColor,
                fontSize = fontSize,
                fontWeight = FontWeight.Medium,
                lineHeight = fontSize * 1.4,
            )
            if (isRequired) {
                Text(
                    text = " $requiredIndicator",
                    color = t.Error,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
        if (description != null) {
            Text(
                text = description,
                color = t.TextSecondary,
                fontSize = descFontSize,
                lineHeight = descFontSize * 1.4,
            )
        }
    }
}

// ─── Visual tag label ─────────────────────────────────────────────────────────

/** Display style for [CeroFiaoTagLabel] */
enum class TagLabelVariant {
    Flat,     // soft tinted background, no border  [default]
    Solid,    // full accent background, white/dark text
    Bordered, // transparent background + 1dp colored border
    Faded,    // very subtle bg + faint border
}

/** Color token for [CeroFiaoTagLabel] */
enum class TagLabelColor { Default, Primary, Success, Warning, Danger }

/**
 * Compact visual label / tag badge.
 *
 * HeroUI V3 Label (node 19865-36063) — a small chip-like element used for
 * categorization, status, and inline annotations.
 *
 * Sizes:
 *  - Sm → height ~18dp, font 10sp, hPad 6dp, radius 4dp
 *  - Md → height ~22dp, font 12sp, hPad 8dp, radius 6dp  [default]
 *  - Lg → height ~28dp, font 14sp, hPad 10dp, radius 8dp
 *
 * Usage:
 * ```
 * CeroFiaoTagLabel(text = "Activo",   color = TagLabelColor.Success)
 * CeroFiaoTagLabel(text = "Pendiente", color = TagLabelColor.Warning, variant = TagLabelVariant.Bordered)
 * CeroFiaoTagLabel(text = "Beta",     color = TagLabelColor.Primary,  variant = TagLabelVariant.Solid)
 * ```
 */
@Composable
fun CeroFiaoTagLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: TagLabelColor = TagLabelColor.Default,
    variant: TagLabelVariant = TagLabelVariant.Flat,
    size: LabelSize = LabelSize.Md,
    icon: ImageVector? = null,
    dot: Boolean = false,
) {
    val t = CeroFiaoDesign.colors

    val accentColor: Color = when (color) {
        TagLabelColor.Default -> t.TextSecondary
        TagLabelColor.Primary -> t.Primary
        TagLabelColor.Success -> t.Success
        TagLabelColor.Warning -> t.Warning
        TagLabelColor.Danger  -> t.Error
    }
    val softBg: Color = when (color) {
        TagLabelColor.Default -> t.SurfaceVariant
        TagLabelColor.Primary -> t.AccentSoft
        TagLabelColor.Success -> t.SuccessSoft
        TagLabelColor.Warning -> t.WarningSoft
        TagLabelColor.Danger  -> t.DangerSoft
    }

    val bgColor: Color = when (variant) {
        TagLabelVariant.Flat   -> softBg
        TagLabelVariant.Solid  -> accentColor
        TagLabelVariant.Bordered, TagLabelVariant.Faded -> Color.Transparent
    }
    val border: BorderStroke? = when (variant) {
        TagLabelVariant.Bordered -> BorderStroke(1.dp, accentColor)
        TagLabelVariant.Faded    -> BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
        else                     -> null
    }
    val textColor: Color = when (variant) {
        TagLabelVariant.Solid -> if (color == TagLabelColor.Default) t.TextPrimary else Color.White
        else                  -> if (color == TagLabelColor.Default) t.TextSecondary else accentColor
    }

    val fontSize = when (size) { LabelSize.Sm -> 10.sp; LabelSize.Md -> 12.sp; LabelSize.Lg -> 14.sp }
    val hPad = when (size) { LabelSize.Sm -> 6.dp; LabelSize.Md -> 8.dp; LabelSize.Lg -> 10.dp }
    val vPad = when (size) { LabelSize.Sm -> 2.dp; LabelSize.Md -> 3.dp; LabelSize.Lg -> 4.dp }
    val radius = when (size) { LabelSize.Sm -> 4.dp; LabelSize.Md -> 6.dp; LabelSize.Lg -> 8.dp }
    val iconSize = when (size) { LabelSize.Sm -> 10.dp; LabelSize.Md -> 12.dp; LabelSize.Lg -> 14.dp }
    val dotSize = when (size) { LabelSize.Sm -> 5.dp; LabelSize.Md -> 6.dp; LabelSize.Lg -> 7.dp }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        color = bgColor,
        border = border,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = hPad, vertical = vPad),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (dot) {
                Surface(
                    modifier = Modifier.size(dotSize),
                    shape = RoundedCornerShape(50),
                    color = accentColor,
                    content = {},
                )
            }
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(iconSize),
                )
            }
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = FontWeight.Medium,
                lineHeight = fontSize * 1.4,
            )
        }
    }
}
