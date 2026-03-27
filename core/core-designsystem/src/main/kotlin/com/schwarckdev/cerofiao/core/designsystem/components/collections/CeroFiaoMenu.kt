package com.schwarckdev.cerofiao.core.designsystem.components.collections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Floating context menu with selection groups.
 * Replaces HeroUI's Menu component.
 */
@Composable
fun CeroFiaoMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero,
    content: @Composable CeroFiaoMenuScope.() -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = offset
    ) {
        val scope = CeroFiaoMenuScopeImpl()
        scope.content()
    }
}

interface CeroFiaoMenuScope {
    @Composable
    fun Item(
        text: String,
        onClick: () -> Unit,
        icon: ImageVector? = null,
        enabled: Boolean = true,
        isDestructive: Boolean = false
    )

    @Composable
    fun Divider()

    @Composable
    fun Section(
        title: String,
        content: @Composable CeroFiaoMenuScope.() -> Unit
    )
}

private class CeroFiaoMenuScopeImpl : CeroFiaoMenuScope {

    @Composable
    override fun Item(
        text: String,
        onClick: () -> Unit,
        icon: ImageVector?,
        enabled: Boolean,
        isDestructive: Boolean
    ) {
        val t = CeroFiaoDesign.colors
        val textColor = when {
            !enabled -> t.InactiveColor
            isDestructive -> t.Error
            else -> t.TextPrimary
        }

        DropdownMenuItem(
            text = {
                Text(
                    text = text,
                    color = textColor,
                    fontSize = 15.sp
                )
            },
            onClick = onClick,
            enabled = enabled,
            leadingIcon = if (icon != null) {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null
        )
    }

    @Composable
    override fun Divider() {
        HorizontalDivider(
            color = CeroFiaoDesign.colors.CardBorder,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

    @Composable
    override fun Section(
        title: String,
        content: @Composable CeroFiaoMenuScope.() -> Unit
    ) {
        Text(
            text = title,
            color = CeroFiaoDesign.colors.TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}
