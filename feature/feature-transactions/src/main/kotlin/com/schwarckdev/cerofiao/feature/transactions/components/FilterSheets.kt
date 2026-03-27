package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ArrowDown
import com.composables.icons.lucide.ArrowUp
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.CircleDollarSign
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.designsystem.components.overlays.CeroFiaoBottomSheet
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.feature.transactions.SortOrder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip as clipShape
import androidx.compose.ui.graphics.toArgb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountFilterSheet(
    accounts: List<Account>,
    selectedAccountId: String?,
    onAccountSelected: (String?) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    CeroFiaoBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "Filtrar por cuenta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary,
            )
            Spacer(Modifier.height(16.dp))

            // "All" option
            SheetOption(
                label = "Todas las cuentas",
                isSelected = selectedAccountId == null,
                onClick = {
                    onAccountSelected(null)
                    onDismiss()
                },
            )

            accounts.forEach { account ->
                SheetOption(
                    label = account.name,
                    isSelected = account.id == selectedAccountId,
                    onClick = {
                        onAccountSelected(account.id)
                        onDismiss()
                    },
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortOrderSheet(
    currentOrder: SortOrder,
    onOrderSelected: (SortOrder) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    CeroFiaoBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "Ordenar por",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary,
            )
            Spacer(Modifier.height(16.dp))

            SortOrder.entries.forEach { order ->
                val (label, icon) = when (order) {
                    SortOrder.DATE_DESC -> "Más reciente primero" to Lucide.Calendar
                    SortOrder.DATE_ASC -> "Más antiguo primero" to Lucide.Calendar
                    SortOrder.AMOUNT_DESC -> "Mayor monto primero" to Lucide.ArrowDown
                    SortOrder.AMOUNT_ASC -> "Menor monto primero" to Lucide.ArrowUp
                }
                SheetOption(
                    label = label,
                    isSelected = order == currentOrder,
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (order == currentOrder) colors.Primary else colors.TextSecondary,
                        )
                    },
                    onClick = {
                        onOrderSelected(order)
                        onDismiss()
                    },
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterSheet(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    CeroFiaoBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Filtrar por categoría",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary,
            )
            Spacer(Modifier.height(16.dp))

            SheetOption(
                label = "Todas las categorías",
                isSelected = selectedCategoryId == null,
                onClick = {
                    onCategorySelected(null)
                    onDismiss()
                },
            )

            categories.forEach { category ->
                val catColor = try {
                    Color(android.graphics.Color.parseColor(category.colorHex))
                } catch (_: Exception) {
                    colors.TextSecondary
                }
                SheetOption(
                    label = category.name,
                    isSelected = category.id == selectedCategoryId,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(catColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(catColor),
                            )
                        }
                    },
                    onClick = {
                        onCategorySelected(category.id)
                        onDismiss()
                    },
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SheetOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    val colors = CeroFiaoDesign.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (icon != null) {
            icon()
        }
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) colors.Primary else colors.TextPrimary,
            modifier = Modifier.weight(1f),
        )
        if (isSelected) {
            Icon(
                imageVector = Lucide.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = colors.Primary,
            )
        }
    }
}
