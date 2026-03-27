package com.schwarckdev.cerofiao.core.designsystem.components.buttons

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Single toggle button — inactive/active states.
 * Figma: height 32dp, radius 16dp, padding 6/10dp, icon 16dp, 14sp medium.
 * Active: AccentSoft bg + Primary text. Inactive: SurfaceVariant bg + TextSecondary.
 */
@Composable
fun CeroFiaoToggleButton(
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val t = CeroFiaoDesign.colors
    val bgColor = if (selected) t.AccentSoft else t.SurfaceVariant
    val contentColor = if (selected) t.Primary else t.TextSecondary
    val isIconOnly = text == null && icon != null

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = bgColor
    ) {
        Box(
            modifier = Modifier
                .pressableFeedback(onClick = onToggle, variant = FeedbackVariant.ScaleHighlight)
                .then(
                    if (isIconOnly) Modifier.size(32.dp)
                    else Modifier
                        .height(32.dp)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    if (text != null) Spacer(Modifier.width(6.dp))
                }
                if (text != null) {
                    Text(
                        text = text,
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class ToggleButtonItem(
    val key: String,
    val text: String? = null,
    val icon: ImageVector? = null,
)

/**
 * Connected row of toggle buttons with dividers between them.
 * Figma: same sizing as CeroFiaoToggleButton, connected with thin dividers.
 */
@Composable
fun CeroFiaoToggleButtonGroup(
    items: List<ToggleButtonItem>,
    selectedKey: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoDesign.colors

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = t.SurfaceVariant
    ) {
        Row(
            modifier = Modifier.height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val selected = item.key == selectedKey
                val contentColor = if (selected) t.Primary else t.TextSecondary
                val isIconOnly = item.text == null && item.icon != null

                Box(
                    modifier = Modifier
                        .then(
                            if (selected) Modifier.background(t.AccentSoft, RoundedCornerShape(16.dp))
                            else Modifier
                        )
                        .pressableFeedback(onClick = { onSelect(item.key) }, variant = FeedbackVariant.ScaleHighlight)
                        .then(
                            if (isIconOnly) Modifier.size(32.dp)
                            else Modifier
                                .height(32.dp)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (item.icon != null) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            if (item.text != null) Spacer(Modifier.width(6.dp))
                        }
                        if (item.text != null) {
                            Text(
                                text = item.text,
                                color = contentColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                if (index < items.lastIndex) {
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        thickness = 1.dp,
                        color = t.CardBorder
                    )
                }
            }
        }
    }
}
