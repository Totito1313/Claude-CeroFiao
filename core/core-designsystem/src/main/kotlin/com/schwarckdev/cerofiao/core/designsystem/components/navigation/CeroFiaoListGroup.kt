package com.schwarckdev.cerofiao.core.designsystem.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCard
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CardVariant
import com.schwarckdev.cerofiao.core.designsystem.components.layout.CeroFiaoSeparator
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.ComponentSize

/**
 * Groups related list items with consistent layout and spacing.
 * Replaces HeroUI's ListGroup component.
 */
@Composable
fun CeroFiaoListGroup(
    modifier: Modifier = Modifier,
    title: String? = null,
    dividers: Boolean = true,
    content: @Composable CeroFiaoListGroupScope.() -> Unit
) {
    Column(modifier = modifier) {
        if (title != null) {
            Text(
                text = title,
                color = CeroFiaoDesign.colors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
        }

        CeroFiaoCard(variant = CardVariant.Default, feedback = FeedbackVariant.ScaleHighlight) {
            val scope = CeroFiaoListGroupScopeImpl(dividers)
            scope.content()
        }
    }
}

interface CeroFiaoListGroupScope {
    @Composable
    fun Item(
        title: String,
        modifier: Modifier = Modifier,
        subtitle: String? = null,
        leading: (@Composable () -> Unit)? = null,
        trailing: (@Composable () -> Unit)? = null,
        onClick: (() -> Unit)? = null
    )
}

private class CeroFiaoListGroupScopeImpl(
    private val dividers: Boolean
) : CeroFiaoListGroupScope {

    private var itemCount = 0

    @Composable
    override fun Item(
        title: String,
        modifier: Modifier,
        subtitle: String?,
        leading: (@Composable () -> Unit)?,
        trailing: (@Composable () -> Unit)?,
        onClick: (() -> Unit)?
    ) {
        val isFirst = itemCount == 0
        itemCount++

        val t = CeroFiaoDesign.colors

        if (!isFirst && dividers) {
            CeroFiaoSeparator(inset = 16.dp)
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = ComponentSize.listItemHeight)
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leading != null) {
                leading()
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = t.TextPrimary,
                    fontSize = 15.sp
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = t.TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            if (trailing != null) {
                Spacer(modifier = Modifier.width(12.dp))
                trailing()
            }
        }
    }
}
