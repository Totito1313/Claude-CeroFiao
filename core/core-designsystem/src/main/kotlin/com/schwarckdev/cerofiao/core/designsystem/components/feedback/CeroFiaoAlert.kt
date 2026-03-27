package com.schwarckdev.cerofiao.core.designsystem.components.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TriangleAlert
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoCloseButton
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/** Semantic color of the alert — maps to icon/text/border color */
enum class AlertColor {
    Default,
    Primary,
    Success,
    Warning,
    Danger
}

/**
 * Display style of the alert.
 * - Flat: soft translucent background (default)
 * - Solid: saturated background, white text
 * - Bordered: transparent background with colored border
 * - Faded: very subtle background with soft colored border
 */
enum class AlertVariant {
    Flat,
    Solid,
    Bordered,
    Faded
}

/**
 * Displays important messages with status indicators.
 * Matches HeroUI Alert V3 — 4 variants × 5 colors.
 *
 * @param endContent Optional slot rendered inline at the trailing edge (e.g. action button).
 *                   Takes precedence over [onDismiss] close button when both are provided.
 */
@Composable
fun CeroFiaoAlert(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    color: AlertColor = AlertColor.Default,
    variant: AlertVariant = AlertVariant.Flat,
    icon: ImageVector? = null,
    action: (@Composable () -> Unit)? = null,
    endContent: (@Composable () -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    val t = CeroFiaoDesign.colors

    // Resolve base accent color for each AlertColor
    val accentColor = when (color) {
        AlertColor.Default -> t.TextSecondary
        AlertColor.Primary -> t.Primary
        AlertColor.Success -> t.Success
        AlertColor.Warning -> t.Warning
        AlertColor.Danger -> t.Error
    }

    // Background and border per variant
    val bgColor = when (variant) {
        AlertVariant.Flat -> when (color) {
            AlertColor.Default -> t.SurfaceVariant
            AlertColor.Primary -> t.AccentSoft
            AlertColor.Success -> t.SuccessSoft
            AlertColor.Warning -> t.WarningSoft
            AlertColor.Danger -> t.DangerSoft
        }
        AlertVariant.Solid -> accentColor
        AlertVariant.Bordered -> Color.Transparent
        AlertVariant.Faded -> when (color) {
            AlertColor.Default -> t.SurfaceVariant.copy(alpha = 0.5f)
            else -> accentColor.copy(alpha = 0.05f)
        }
    }

    val border = when (variant) {
        AlertVariant.Bordered -> BorderStroke(1.5.dp, accentColor)
        AlertVariant.Faded -> BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
        else -> null
    }

    // Text colors: white on Solid, accent on colored variants, primary on default
    val titleColor = when (variant) {
        AlertVariant.Solid -> Color.White
        AlertVariant.Flat, AlertVariant.Bordered, AlertVariant.Faded -> when (color) {
            AlertColor.Default -> t.TextPrimary
            else -> accentColor
        }
    }
    val descColor = when (variant) {
        AlertVariant.Solid -> Color.White.copy(alpha = 0.85f)
        else -> t.TextSecondary
    }
    val iconTint = when (variant) {
        AlertVariant.Solid -> Color.White
        else -> accentColor
    }

    val effectiveIcon = icon ?: when (color) {
        AlertColor.Default -> Lucide.Info
        AlertColor.Primary -> Lucide.Info
        AlertColor.Success -> Lucide.CircleCheck
        AlertColor.Warning -> Lucide.TriangleAlert
        AlertColor.Danger -> Lucide.CircleX
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = border,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = if (description != null || action != null) Alignment.Top else Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = effectiveIcon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .padding(top = if (description != null || action != null) 1.dp else 0.dp)
                    .size(20.dp),
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                )
                if (description != null) {
                    Text(
                        text = description,
                        color = descColor,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                    )
                }
                if (action != null) {
                    Spacer(Modifier.size(4.dp))
                    action()
                }
            }

            when {
                endContent != null -> {
                    Spacer(Modifier.width(8.dp))
                    endContent()
                }
                onDismiss != null -> {
                    Spacer(Modifier.width(8.dp))
                    CeroFiaoCloseButton(
                        onClick = onDismiss,
                        tint = iconTint,
                    )
                }
            }
        }
    }
}
