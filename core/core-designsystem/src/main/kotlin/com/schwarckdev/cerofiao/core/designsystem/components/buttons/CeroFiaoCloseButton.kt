package com.schwarckdev.cerofiao.core.designsystem.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.IconSize

/**
 * Dismiss button for dialogs, modals, or content panels.
 * Replaces HeroUI's CloseButton component.
 */
@Composable
fun CeroFiaoCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = IconSize.md,
    tint: Color = CeroFiaoDesign.colors.TextSecondary,
    contentDescription: String = "Cerrar"
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(CeroFiaoDesign.componentSize.closeButtonSize)
    ) {
        Icon(
            imageVector = Lucide.X,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}
