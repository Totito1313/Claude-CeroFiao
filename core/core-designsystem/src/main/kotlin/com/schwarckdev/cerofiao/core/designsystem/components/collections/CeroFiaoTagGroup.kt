package com.schwarckdev.cerofiao.core.designsystem.components.collections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.Spacing

/**
 * Selectable tags with optional removal.
 * Replaces HeroUI's TagGroup component.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> CeroFiaoTagGroup(
    tags: List<T>,
    selectedTags: Set<T>,
    onTagSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    displayText: (T) -> String = { it.toString() },
    removable: Boolean = false,
    onTagRemoved: ((T) -> Unit)? = null
) {
    val t = CeroFiaoDesign.colors

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        tags.forEach { tag ->
            val isSelected = tag in selectedTags

            Surface(
                modifier = Modifier.pressableFeedback(
                    onClick = { onTagSelected(tag) },
                    variant = FeedbackVariant.Scale,
                    scaleDown = 0.95f
                ),
                shape = RoundedCornerShape(CeroFiaoDesign.radius.chip),
                color = if (isSelected) t.AccentSoft else t.SurfaceVariant,
                border = if (isSelected) {
                    BorderStroke(1.dp, t.Primary.copy(alpha = 0.3f))
                } else {
                    BorderStroke(1.dp, t.CardBorder)
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayText(tag),
                        color = if (isSelected) t.Primary else t.TextPrimary,
                        fontSize = 13.sp
                    )

                    if (removable && onTagRemoved != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = { onTagRemoved(tag) },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                imageVector = Lucide.X,
                                contentDescription = "Eliminar",
                                tint = if (isSelected) t.Primary else t.TextSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
