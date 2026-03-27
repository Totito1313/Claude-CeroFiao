package com.schwarckdev.cerofiao.feature.dashboard.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ShoppingBag
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCardConfig
import com.schwarckdev.cerofiao.feature.dashboard.CategoryExpense

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesSection(
    categories: List<CategoryExpense>,
    displayCurrencyCode: String,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current
    val fallbackPalette = listOf(
        colors.AccentPurple,
        colors.AccentBlue,
        colors.AccentRed,
        colors.AccentOrange,
        colors.AccentGreen,
        colors.AccentUser,
    )
    Column(modifier = modifier) {
        SectionHeader(title = "Categorias", onViewAll = onViewAll)
        Spacer(Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(48.dp),
            color = colors.Foreground.copy(alpha = cardConfig.backgroundOpacity),
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp),
                maxItemsInEachRow = 2,
            ) {
                categories.forEachIndexed { index, category ->
                    val dotColor = if (category.colorHex.isNotBlank()) {
                        try {
                            Color(android.graphics.Color.parseColor(category.colorHex))
                        } catch (_: Exception) {
                            fallbackPalette[index % fallbackPalette.size]
                        }
                    } else {
                        fallbackPalette[index % fallbackPalette.size]
                    }
                    CategorySpendCard(
                        category = category,
                        displayCurrencyCode = displayCurrencyCode,
                        dotColor = dotColor,
                        modifier = Modifier.fillMaxWidth(0.48f),
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySpendCard(
    category: CategoryExpense,
    displayCurrencyCode: String,
    dotColor: Color,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = colors.Background.copy(alpha = cardConfig.backgroundOpacity),
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 17.dp, vertical = 9.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(dotColor, CircleShape),
                )
                Text(
                    text = category.categoryName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = dotColor,
                )
            }
            Text(
                text = CurrencyFormatter.format(category.amount, displayCurrencyCode),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${(category.percentage * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = colors.TextSecondary,
                )
                Icon(
                    imageVector = Lucide.ShoppingBag,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = colors.TextSecondary,
                )
            }
        }
    }
}
