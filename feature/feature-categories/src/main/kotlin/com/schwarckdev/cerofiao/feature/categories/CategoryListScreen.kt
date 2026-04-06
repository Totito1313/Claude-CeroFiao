package com.schwarckdev.cerofiao.feature.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCard
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.resolveIconKey
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryNode

@Composable
fun CategoryListScreen(
    onBack: () -> Unit = {},
    onAddCategory: () -> Unit = {},
    onEditCategory: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CategoryListViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Detail, title = "Categorías", onBackClick = onBack)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors
    var selectedTab by remember { mutableStateOf(0) } // 0 = Gastos, 1 = Ingresos

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .statusBarsPadding()
            .padding(top = 70.dp),
    ) {
        // Tab selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf("Gastos" to 0, "Ingresos" to 1).forEach { (label, index) ->
                val isSelected = selectedTab == index
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .pressableFeedback(
                            onClick = { selectedTab = index },
                            variant = FeedbackVariant.ScaleHighlight,
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) colors.Primary else colors.CardBackground,
                ) {
                    Text(
                        text = label,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = if (isSelected) colors.TextOnDark else colors.TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Add button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(BrandGradient, RoundedCornerShape(14.dp))
                .pressableFeedback(onClick = onAddCategory, variant = FeedbackVariant.ScaleHighlight)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(CeroFiaoIcons.Add, contentDescription = null, tint = colors.TextOnDark, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Nueva categoría", color = colors.TextOnDark, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(12.dp))

        val categories = if (selectedTab == 0) uiState.expenseCategories else uiState.incomeCategories

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Sin categorías", color = colors.TextSecondary, fontSize = 15.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                itemsIndexed(
                    items = categories,
                    key = { _, node -> node.category.id },
                ) { _, node ->
                    CategoryNodeItem(
                        node = node,
                        total = uiState.categoryTotals[node.category.id],
                        subTotals = uiState.categoryTotals,
                        onEdit = { onEditCategory(it) },
                        onDelete = { viewModel.deleteCategory(it) },
                    )
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
private fun CategoryNodeItem(
    node: CategoryNode,
    total: Double?,
    subTotals: Map<String, Double>,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    val colors = CeroFiaoDesign.colors
    val cat = node.category
    val catColor = try {
        Color(android.graphics.Color.parseColor(cat.colorHex))
    } catch (_: Exception) {
        colors.Primary
    }

    Column(modifier = Modifier.animateContentSize()) {
        CeroFiaoCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEdit(cat.id) },
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Color dot + icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(catColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = resolveIconKey("ic_${cat.iconName.lowercase()}"),
                        contentDescription = null,
                        tint = catColor,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cat.name,
                        color = colors.TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    if (node.subcategories.isNotEmpty()) {
                        Text(
                            text = "${node.subcategories.size} subcategorías",
                            color = colors.TextSecondary,
                            fontSize = 12.sp,
                        )
                    }
                }

                if (total != null && total > 0) {
                    Text(
                        text = CurrencyFormatter.format(total, "USD"),
                        color = colors.TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        // Subcategories (indented)
        if (node.subcategories.isNotEmpty()) {
            node.subcategories.forEach { sub ->
                val subColor = try {
                    Color(android.graphics.Color.parseColor(sub.colorHex))
                } catch (_: Exception) {
                    colors.Primary
                }
                val subTotal = subTotals[sub.id]

                CeroFiaoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 4.dp),
                    onClick = { onEdit(sub.id) },
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(subColor),
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = sub.name,
                            modifier = Modifier.weight(1f),
                            color = colors.TextPrimary,
                            fontSize = 14.sp,
                        )
                        if (subTotal != null && subTotal > 0) {
                            Text(
                                text = CurrencyFormatter.format(subTotal, "USD"),
                                color = colors.TextSecondary,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
