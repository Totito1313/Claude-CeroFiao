package com.schwarckdev.cerofiao.core.designsystem.components.feedback

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.TriangleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.CircleX
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoCloseButton
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

enum class AlertVariant {
    Default,
    Accent,
    Success,
    Warning,
    Danger
}

/**
 * Displays important messages with status indicators.
 * Replaces HeroUI's Alert component.
 */
@Composable
fun CeroFiaoAlert(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    variant: AlertVariant = AlertVariant.Default,
    icon: ImageVector? = null,
    action: (@Composable () -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    val t = CeroFiaoDesign.colors

    val colors = when (variant) {
        AlertVariant.Default -> AlertColors(t.SurfaceVariant, t.TextPrimary, t.TextSecondary)
        AlertVariant.Accent -> AlertColors(t.AccentSoft, t.Primary, t.TextSecondary)
        AlertVariant.Success -> AlertColors(t.SuccessSoft, t.Success, t.TextSecondary)
        AlertVariant.Warning -> AlertColors(t.WarningSoft, t.Warning, t.TextSecondary)
        AlertVariant.Danger -> AlertColors(t.DangerSoft, t.Error, t.TextSecondary)
    }

    val effectiveIcon = icon ?: when (variant) {
        AlertVariant.Default -> Lucide.Info
        AlertVariant.Accent -> Lucide.Info
        AlertVariant.Success -> Lucide.CircleCheck
        AlertVariant.Warning -> Lucide.TriangleAlert
        AlertVariant.Danger -> Lucide.CircleX
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CeroFiaoDesign.radius.md))
            .background(colors.background)
            .padding(16.dp),
        verticalAlignment = if (description != null) Alignment.Top else Alignment.CenterVertically
    ) {
        Icon(
            imageVector = effectiveIcon,
            contentDescription = null,
            tint = colors.foreground,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = colors.foreground,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (description != null) {
                Text(
                    text = description,
                    color = colors.descriptionColor,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
            if (action != null) {
                Spacer(modifier = Modifier.width(8.dp))
                action()
            }
        }

        if (onDismiss != null) {
            CeroFiaoCloseButton(
                onClick = onDismiss,
                tint = colors.foreground
            )
        }
    }
}

private data class AlertColors(
    val background: Color,
    val foreground: Color,
    val descriptionColor: Color
)
