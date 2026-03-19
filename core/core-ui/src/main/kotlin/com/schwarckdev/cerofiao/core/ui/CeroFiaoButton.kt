package com.schwarckdev.cerofiao.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.DangerGradient

enum class CeroFiaoButtonVariant {
    Primary,
    Secondary,
    Text,
    Danger
}

@Composable
fun CeroFiaoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: CeroFiaoButtonVariant = CeroFiaoButtonVariant.Primary,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(CeroFiaoShapes.ButtonRadius),
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
) {
    val t = CeroFiaoTheme.tokens
    
    val backgroundModifier = when (variant) {
        CeroFiaoButtonVariant.Primary -> Modifier.background(
            if (enabled) BrandGradient else Brush.linearGradient(listOf(t.surfaceHover, t.surfaceHover))
        )
        CeroFiaoButtonVariant.Danger -> Modifier.background(
            if (enabled) DangerGradient else Brush.linearGradient(listOf(t.surfaceHover, t.surfaceHover))
        )
        else -> Modifier // Secondary and Text handle backgrounds via Surface color
    }

    val contentColor = when (variant) {
        CeroFiaoButtonVariant.Primary, CeroFiaoButtonVariant.Danger -> if (enabled) Color.White else t.textGhost
        CeroFiaoButtonVariant.Secondary -> if (enabled) t.text else t.textGhost
        CeroFiaoButtonVariant.Text -> if (enabled) t.text else t.textGhost
    }

    val surfaceColor = when (variant) {
        CeroFiaoButtonVariant.Secondary -> if (enabled) t.surface else t.surface.copy(alpha = 0.02f)
        CeroFiaoButtonVariant.Text -> Color.Transparent
        else -> Color.Transparent
    }

    val surfaceBorder = when (variant) {
        CeroFiaoButtonVariant.Secondary -> BorderStroke(1.dp, if (enabled) t.surfaceBorder else t.surfaceBorder.copy(alpha=0.02f))
        else -> null
    }

    Surface(
        modifier = modifier.clip(shape),
        shape = shape,
        color = surfaceColor,
        border = surfaceBorder
    ) {
        Box(
            modifier = Modifier
                .then(backgroundModifier)
                .clickable(enabled = enabled && !isLoading, onClick = onClick)
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = text,
                        color = contentColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
