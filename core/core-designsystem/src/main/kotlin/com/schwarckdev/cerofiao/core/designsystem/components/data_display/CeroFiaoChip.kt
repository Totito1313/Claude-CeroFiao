package com.schwarckdev.cerofiao.core.designsystem.components.data_display

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

enum class ChipVariant {
    Filled,
    Outlined,
    Soft
}

/**
 * Compact element in a capsule shape.
 * Replaces HeroUI's Chip component.
 */
@Composable
fun CeroFiaoChip(
    label: String,
    modifier: Modifier = Modifier,
    variant: ChipVariant = ChipVariant.Filled,
    icon: ImageVector? = null,
    color: Color = CeroFiaoDesign.colors.Primary,
    onClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    val t = CeroFiaoDesign.colors

    val backgroundColor = when (variant) {
        ChipVariant.Filled -> color
        ChipVariant.Outlined -> Color.Transparent
        ChipVariant.Soft -> color.copy(alpha = 0.12f)
    }

    val contentColor = when (variant) {
        ChipVariant.Filled -> Color.White
        ChipVariant.Outlined -> color
        ChipVariant.Soft -> color
    }

    val border = when (variant) {
        ChipVariant.Outlined -> BorderStroke(1.dp, color)
        else -> null
    }

    val clickModifier = if (onClick != null) {
        Modifier.pressableFeedback(
            onClick = onClick,
            variant = FeedbackVariant.Scale,
            scaleDown = 0.95f
        )
    } else Modifier

    Surface(
        modifier = modifier.then(clickModifier),
        shape = RoundedCornerShape(CeroFiaoDesign.radius.chip),
        color = backgroundColor,
        border = border
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = CeroFiaoDesign.componentSize.chipPaddingHorizontal,
                vertical = CeroFiaoDesign.componentSize.chipPaddingVertical
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = label,
                color = contentColor,
                fontSize = 13.sp
            )

            if (onDismiss != null) {
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        imageVector = Lucide.X,
                        contentDescription = "Eliminar",
                        tint = contentColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
