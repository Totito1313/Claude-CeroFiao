package com.schwarckdev.cerofiao.core.designsystem.components.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.ComponentSize

enum class AvatarSize {
    Small,
    Medium,
    Large
}

/**
 * User avatar with support for initials or fallback icon.
 * Replaces HeroUI's Avatar component.
 */
@Composable
fun CeroFiaoAvatar(
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Medium,
    initials: String? = null,
    fallbackIcon: ImageVector? = null,
    backgroundColor: Color = CeroFiaoDesign.colors.SurfaceVariant,
    contentColor: Color = CeroFiaoDesign.colors.TextPrimary
) {
    val dimension = when (size) {
        AvatarSize.Small -> ComponentSize.avatarSmall
        AvatarSize.Medium -> ComponentSize.avatarMedium
        AvatarSize.Large -> ComponentSize.avatarLarge
    }

    val fontSize = when (size) {
        AvatarSize.Small -> 12.sp
        AvatarSize.Medium -> 16.sp
        AvatarSize.Large -> 22.sp
    }

    val iconSize = when (size) {
        AvatarSize.Small -> 16.dp
        AvatarSize.Medium -> 20.dp
        AvatarSize.Large -> 28.dp
    }

    Box(
        modifier = modifier
            .size(dimension)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        when {
            initials != null -> {
                Text(
                    text = initials.take(2).uppercase(),
                    color = contentColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold
                )
            }
            else -> {
                Icon(
                    imageVector = fallbackIcon ?: Lucide.User,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}
