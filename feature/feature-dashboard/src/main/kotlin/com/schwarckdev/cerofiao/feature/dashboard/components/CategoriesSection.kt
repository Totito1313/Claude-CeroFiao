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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.feature.dashboard.CategoryExpense

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesSection(
    categories: List<CategoryExpense>,
    displayCurrencyCode: String,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionHeader(title = "Categorias", onViewAll = onViewAll)
        Spacer(Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(48.dp),
            color = Color(0xFFFCFCFF),
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(11.dp),
                maxItemsInEachRow = 2,
            ) {
                categories.forEach { category ->
                    CategorySpendCard(
                        category = category,
                        displayCurrencyCode = displayCurrencyCode,
                        modifier = Modifier.width(180.dp),
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
    modifier: Modifier = Modifier,
) {
    val dotColor = Color(0xFFA855F7) // Default purple

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = Color(0xFFF1F1F3),
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
                color = Color(0xFF111827),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${(category.percentage * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                )
            }
        }
    }
}
