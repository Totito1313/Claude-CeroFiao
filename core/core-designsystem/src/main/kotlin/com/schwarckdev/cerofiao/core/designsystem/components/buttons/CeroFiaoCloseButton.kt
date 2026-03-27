package com.schwarckdev.cerofiao.core.designsystem.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.IconSize

/**
 * Dismiss button for dialogs, modals, or content panels.
 * Figma: SurfaceVariant bg, 20dp radius, 6dp padding, 24dp X icon.
 */
@Composable
fun CeroFiaoCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = IconSize.md,
    tint: Color = CeroFiaoDesign.colors.TextSecondary,
    contentDescription: String = "Cerrar"
) {
    val t = CeroFiaoDesign.colors
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = t.SurfaceVariant
    ) {
        Box(
            modifier = Modifier
                .pressableFeedback(onClick = onClick, variant = FeedbackVariant.ScaleHighlight)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Lucide.X,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(size)
            )
        }
    }
}
