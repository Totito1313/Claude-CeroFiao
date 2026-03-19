package com.schwarckdev.cerofiao.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

/**
 * Glassmorphism card matching the Figma CeroFiao design.
 * Translucent surface with subtle border, large rounded corners.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    padding: GlassCardPadding = GlassCardPadding.Medium,
    cornerRadius: Dp = CeroFiaoShapes.CardRadius,
    content: @Composable BoxScope.() -> Unit,
) {
    val t = CeroFiaoTheme.tokens
    val shape = RoundedCornerShape(cornerRadius)

    Surface(
        modifier = modifier
            .clip(shape)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier,
            ),
        shape = shape,
        color = t.surface,
        border = BorderStroke(1.dp, t.surfaceBorder),
    ) {
        Box(
            modifier = Modifier.padding(padding.value),
            content = content,
        )
    }
}

enum class GlassCardPadding(val value: Dp) {
    None(0.dp),
    Small(12.dp),
    Medium(16.dp),
    Large(20.dp),
}
