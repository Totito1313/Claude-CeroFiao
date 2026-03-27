package com.schwarckdev.cerofiao.core.designsystem.components.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.designsystem.components.layout.CeroFiaoSeparator
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.ComponentSize

/**
 * Collapsible content panels for organizing information.
 * Replaces HeroUI's Accordion component.
 */
@Composable
fun CeroFiaoAccordion(
    modifier: Modifier = Modifier,
    allowMultipleOpen: Boolean = false,
    content: @Composable CeroFiaoAccordionScope.() -> Unit
) {
    val scope = remember(allowMultipleOpen) { CeroFiaoAccordionScopeImpl(allowMultipleOpen) }
    Column(modifier = modifier) {
        scope.content()
    }
}

interface CeroFiaoAccordionScope {
    @Composable
    fun Item(
        title: String,
        modifier: Modifier = Modifier,
        subtitle: String? = null,
        icon: ImageVector? = null,
        initiallyExpanded: Boolean = false,
        content: @Composable () -> Unit
    )
}

private class CeroFiaoAccordionScopeImpl(
    private val allowMultipleOpen: Boolean
) : CeroFiaoAccordionScope {

    private val expandedItems = mutableStateListOf<Int>()
    private var itemCounter = 0

    @Composable
    override fun Item(
        title: String,
        modifier: Modifier,
        subtitle: String?,
        icon: ImageVector?,
        initiallyExpanded: Boolean,
        content: @Composable () -> Unit
    ) {
        val itemIndex = remember { itemCounter++ }
        var isExpanded by remember {
            mutableStateOf(initiallyExpanded.also {
                if (it) expandedItems.add(itemIndex)
            })
        }

        val rotation by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f,
            label = "AccordionChevron"
        )

        val t = CeroFiaoDesign.colors

        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = ComponentSize.accordionItemHeight)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (isExpanded) {
                            expandedItems.remove(itemIndex)
                        } else {
                            if (!allowMultipleOpen) expandedItems.clear()
                            expandedItems.add(itemIndex)
                        }
                        isExpanded = itemIndex in expandedItems
                    }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = t.TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = t.TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            color = t.TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }

                Icon(
                    imageVector = Lucide.ChevronDown,
                    contentDescription = null,
                    tint = t.TextSecondary,
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    content()
                }
            }

            CeroFiaoSeparator()
        }
    }
}
