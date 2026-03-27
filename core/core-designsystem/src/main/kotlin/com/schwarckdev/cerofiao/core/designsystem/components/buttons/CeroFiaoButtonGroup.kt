package com.schwarckdev.cerofiao.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.DangerGradient

/**
 * A single segment inside a [CeroFiaoButtonGroup].
 *
 * @param key         Unique identifier for this segment.
 * @param text        Optional label. If null and [icon] is set → icon-only square segment.
 * @param icon        Optional leading icon (16dp Sm/Md, 18dp Lg).
 * @param badge       Optional count/badge shown after the label in [Primary] color (e.g. "43", "4").
 * @param isActive    When true the icon + text are tinted in [Primary] color regardless of variant.
 * @param onClick     Action triggered by this segment.
 * @param enabled     Whether this individual segment is interactive.
 */
data class ButtonGroupItem(
    val key: String,
    val text: String? = null,
    val icon: ImageVector? = null,
    val badge: String? = null,
    val isActive: Boolean = false,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
)

/**
 * Connected row of action-button segments sharing a pill-shaped outer container.
 *
 * Figma HeroUI V3 ButtonGroup (node 17664-69593 / 17676-92982):
 *
 *  Anatomy
 *  ───────
 *  ┌────────────────────────────────────────────────────────────┐
 *  │  [icon] text [badge]  │  [icon] text [badge]  │  [icon]   │
 *  └────────────────────────────────────────────────────────────┘
 *   ↑ 1dp VerticalDividers between segments
 *
 *  Sizing (matches CeroFiaoButton)
 *  ────────────────────────────────
 *  Sm  → height 36dp, font 14sp, hPad 12dp, icon 16dp
 *  Md  → height 40dp, font 16sp, hPad 16dp, icon 16dp
 *  Lg  → height 56dp, font 18sp, hPad 20dp, icon 18dp
 *
 *  Shape: fully-pill outer container (RoundedCornerShape(50))
 *
 *  Variants
 *  ────────
 *  Tertiary   → Flat     : SurfaceVariant bg, no outer border  [HeroUI default]
 *  Primary    → Solid    : Primary bg, OnPrimary text, semi-opaque dividers
 *  Secondary  → Bordered : Surface bg + 1dp CardBorder
 *  Outline    → Ghost+border: Transparent + 1dp CardBorder
 *  Ghost      → Ghost    : Transparent, no border
 *  Danger     → Gradient danger bg
 *  DangerSoft → Soft danger tint
 *
 *  Per-item states
 *  ───────────────
 *  isActive  → icon + text tinted in Primary (used for "Like", active alignment, etc.)
 *  badge     → small count label in Primary color appended after text
 *  isDisabled (group) or item.enabled=false → InactiveColor, non-clickable
 */
@Composable
fun CeroFiaoButtonGroup(
    items: List<ButtonGroupItem>,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Tertiary,
    size: ButtonSize = ButtonSize.Small,
    isDisabled: Boolean = false,
) {
    val t = CeroFiaoDesign.colors

    val height: Dp = when (size) {
        ButtonSize.Small -> 36.dp
        ButtonSize.Medium -> 40.dp
        ButtonSize.Large -> 56.dp
    }
    val fontSize = when (size) {
        ButtonSize.Small -> 14.sp
        ButtonSize.Medium -> 16.sp
        ButtonSize.Large -> 18.sp
    }
    val badgeFontSize = when (size) {
        ButtonSize.Small -> 13.sp
        ButtonSize.Medium -> 14.sp
        ButtonSize.Large -> 16.sp
    }
    val iconSize: Dp = when (size) {
        ButtonSize.Small -> 16.dp
        ButtonSize.Medium -> 16.dp
        ButtonSize.Large -> 18.dp
    }
    val hPad: Dp = when (size) {
        ButtonSize.Small -> 12.dp
        ButtonSize.Medium -> 16.dp
        ButtonSize.Large -> 20.dp
    }

    // Pill shape — matches HeroUI's fully-rounded ButtonGroup container
    val pillShape = RoundedCornerShape(50)

    // Outer container fill
    val groupColor = when (variant) {
        ButtonVariant.Primary    -> if (isDisabled) t.SurfaceVariant else t.Primary
        ButtonVariant.Tertiary   -> t.SurfaceVariant
        ButtonVariant.Secondary  -> t.Surface
        ButtonVariant.Outline,
        ButtonVariant.Ghost,
        ButtonVariant.Danger     -> Color.Transparent
        ButtonVariant.DangerSoft -> if (isDisabled) t.SurfaceVariant else t.DangerSoft
    }

    // Outer border
    val groupBorder = when (variant) {
        ButtonVariant.Secondary,
        ButtonVariant.Outline ->
            BorderStroke(1.dp, if (isDisabled) t.CardBorder.copy(alpha = 0.3f) else t.CardBorder)
        else -> null
    }

    // Divider between segments
    val dividerColor = when (variant) {
        ButtonVariant.Primary,
        ButtonVariant.Danger     -> t.OnPrimary.copy(alpha = 0.25f)
        ButtonVariant.DangerSoft -> t.Error.copy(alpha = 0.25f)
        else                     -> t.CardBorder
    }

    // Resolve content color for a segment
    fun contentColor(item: ButtonGroupItem): Color {
        if (isDisabled || !item.enabled) return t.InactiveColor
        if (item.isActive) return t.Primary
        return when (variant) {
            ButtonVariant.Primary    -> t.OnPrimary
            ButtonVariant.Danger     -> Color.White
            ButtonVariant.DangerSoft -> t.Error
            else                     -> t.TextPrimary
        }
    }

    // Badge color: always Primary when active, slightly muted otherwise
    fun badgeColor(item: ButtonGroupItem): Color {
        if (isDisabled || !item.enabled) return t.InactiveColor
        return t.Primary
    }

    val dangerGradientMod = if (variant == ButtonVariant.Danger && !isDisabled)
        Modifier.background(DangerGradient)
    else
        Modifier

    Surface(
        modifier = modifier.clip(pillShape),
        shape = pillShape,
        color = groupColor,
        border = groupBorder,
    ) {
        Row(
            modifier = Modifier
                .then(dangerGradientMod)
                .height(height),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val effectiveEnabled = !isDisabled && item.enabled
                val cc = contentColor(item)
                val bc = badgeColor(item)
                val isIconOnly = item.text == null && item.icon != null && item.badge == null

                Box(
                    modifier = Modifier
                        .then(
                            if (effectiveEnabled)
                                Modifier.pressableFeedback(
                                    onClick = item.onClick,
                                    variant = FeedbackVariant.ScaleHighlight,
                                )
                            else
                                Modifier.clickable(enabled = false, onClick = {})
                        )
                        .then(
                            if (isIconOnly)
                                Modifier.size(height)
                            else
                                Modifier
                                    .height(height)
                                    .padding(horizontal = hPad, vertical = 8.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        // Leading icon
                        if (item.icon != null) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = cc,
                                modifier = Modifier.size(iconSize),
                            )
                            if (item.text != null || item.badge != null) {
                                Spacer(Modifier.width(6.dp))
                            }
                        }
                        // Label
                        if (item.text != null) {
                            Text(
                                text = item.text,
                                color = cc,
                                fontSize = fontSize,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        // Badge / count (e.g. "43", "4")
                        if (item.badge != null) {
                            if (item.text != null) Spacer(Modifier.width(6.dp))
                            Text(
                                text = item.badge,
                                color = bc,
                                fontSize = badgeFontSize,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }

                // Divider between segments
                if (index < items.lastIndex) {
                    VerticalDivider(
                        modifier = Modifier.height(height * 0.6f),
                        thickness = 1.dp,
                        color = dividerColor,
                    )
                }
            }
        }
    }
}
