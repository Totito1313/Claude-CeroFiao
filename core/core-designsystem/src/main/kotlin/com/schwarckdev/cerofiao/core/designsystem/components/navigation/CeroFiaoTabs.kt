package com.schwarckdev.cerofiao.core.designsystem.components.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Tabbed views with animated indicator.
 * Replaces HeroUI's Tabs component.
 */
@Composable
fun CeroFiaoTabs(
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    tabs: List<String>,
    modifier: Modifier = Modifier,
    icons: List<ImageVector>? = null,
    indicatorColor: Color = CeroFiaoDesign.colors.Primary,
    content: (@Composable (selectedIndex: Int) -> Unit)? = null
) {
    val t = CeroFiaoDesign.colors
    val density = LocalDensity.current

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CeroFiaoDesign.radius.md))
                .background(t.SurfaceVariant)
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = index == selectedIndex

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(CeroFiaoDesign.radius.sm))
                            .then(
                                if (isSelected) {
                                    Modifier.background(t.Surface)
                                } else Modifier
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onSelectedChange(index) }
                            )
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (icons != null && index < icons.size) {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = null,
                                    tint = if (isSelected) t.TextPrimary else t.TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = tab,
                                color = if (isSelected) t.TextPrimary else t.TextSecondary,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        if (content != null) {
            Spacer(modifier = Modifier.height(16.dp))
            content(selectedIndex)
        }
    }
}
