package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryNode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelectorSection(
    categories: List<CategoryNode>,
    selectedCategoryId: String?,
    suggestedCategoryId: String?,
    onCategorySelected: (String) -> Unit,
    onSuggestionAccepted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val haptic = LocalHapticFeedback.current

    // Find suggested category name
    val suggestedName = suggestedCategoryId?.let { id ->
        categories.flatMap { listOf(it.category) + it.subcategories }
            .find { it.id == id }?.name
    }

    Column(modifier = modifier) {
        Text(
            text = "Categoría",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextPrimary,
        )

        // AI suggestion chip
        AnimatedVisibility(
            visible = suggestedName != null && selectedCategoryId == null,
            enter = fadeIn() + slideInVertically { -it / 2 },
        ) {
            Surface(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSuggestionAccepted()
                },
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(100.dp),
                color = colors.AccentBlue.copy(alpha = 0.10f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Lucide.Sparkles,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = colors.AccentBlue,
                    )
                    Text(
                        text = "Sugerencia: ${suggestedName ?: ""}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.AccentBlue,
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Category grid using FlowRow
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            maxItemsInEachRow = 4,
        ) {
            categories.forEach { node ->
                CategoryItem(
                    category = node.category,
                    isSelected = node.category.id == selectedCategoryId,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onCategorySelected(node.category.id)
                    },
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val catColor = try {
        Color(android.graphics.Color.parseColor(category.colorHex))
    } catch (_: Exception) {
        colors.TextSecondary
    }

    Surface(
        onClick = onClick,
        modifier = modifier.width(76.dp),
        color = Color.Transparent,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) catColor
                        else catColor.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) Color.White else catColor),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = category.name,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) catColor else colors.TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
