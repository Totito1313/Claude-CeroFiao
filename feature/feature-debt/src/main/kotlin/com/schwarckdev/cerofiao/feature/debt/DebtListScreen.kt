package com.schwarckdev.cerofiao.feature.debt

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.Users
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonGroupItem
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonSize
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButtonGroup
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtType
import com.schwarckdev.cerofiao.core.ui.CeroFiaoFAB
import com.schwarckdev.cerofiao.core.ui.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtListScreen(
    onAddDebt: () -> Unit,
    onDebtClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DebtListViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Standard, title = "Deudas")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 70.dp),
        ) {
            // Filter tabs
            CeroFiaoButtonGroup(
                items = listOf(
                    ButtonGroupItem(
                        key = "all",
                        text = "Todas",
                        isActive = uiState.selectedFilter == DebtFilter.ALL,
                        onClick = { viewModel.setFilter(DebtFilter.ALL) },
                    ),
                    ButtonGroupItem(
                        key = "they_owe",
                        text = "Me deben",
                        isActive = uiState.selectedFilter == DebtFilter.THEY_OWE,
                        onClick = { viewModel.setFilter(DebtFilter.THEY_OWE) },
                    ),
                    ButtonGroupItem(
                        key = "i_owe",
                        text = "Yo debo",
                        isActive = uiState.selectedFilter == DebtFilter.I_OWE,
                        onClick = { viewModel.setFilter(DebtFilter.I_OWE) },
                    ),
                ),
                size = ButtonSize.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(12.dp))

            // Summary totals
            if (uiState.theyOweTotals.isNotEmpty() || uiState.iOweTotals.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (uiState.theyOweTotals.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            color = colors.incomeSoft,
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Me deben",
                                    fontSize = 12.sp,
                                    color = colors.IncomeColor,
                                    fontWeight = FontWeight.Medium,
                                )
                                uiState.theyOweTotals.forEach { total ->
                                    Text(
                                        text = CurrencyFormatter.format(total.amount, total.currencyCode),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.IncomeColor,
                                    )
                                }
                            }
                        }
                    }
                    if (uiState.iOweTotals.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            color = colors.expenseSoft,
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Yo debo",
                                    fontSize = 12.sp,
                                    color = colors.ExpenseColor,
                                    fontWeight = FontWeight.Medium,
                                )
                                uiState.iOweTotals.forEach { total ->
                                    Text(
                                        text = CurrencyFormatter.format(total.amount, total.currencyCode),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.ExpenseColor,
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            if (!uiState.isLoading && uiState.debts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    EmptyState(
                        icon = Lucide.Users,
                        title = "Sin deudas",
                        description = "Registra deudas para llevar control de quien te debe y a quien le debes",
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 100.dp,
                    ),
                ) {
                    itemsIndexed(
                        items = uiState.debts,
                        key = { _, debt -> debt.id },
                    ) { _, debt ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { visible = true }

                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                                slideInVertically(
                                    initialOffsetY = { it / 3 },
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessMediumLow,
                                    ),
                                ),
                        ) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.deleteDebt(debt.id)
                                        true
                                    } else {
                                        false
                                    }
                                },
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(100.dp))
                                            .background(colors.ExpenseColor),
                                        contentAlignment = Alignment.CenterEnd,
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(end = 20.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        ) {
                                            Text(
                                                text = "Eliminar",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                            )
                                            Icon(
                                                imageVector = Lucide.Trash2,
                                                contentDescription = "Eliminar",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp),
                                            )
                                        }
                                    }
                                },
                            ) {
                                DebtCard(
                                    debt = debt,
                                    onClick = { onDebtClick(debt.id) },
                                )
                            }
                        }
                    }
                }
            }
        }

        CeroFiaoFAB(
            onClick = onAddDebt,
            icon = Lucide.Plus,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 100.dp),
        )
    }
}

@Composable
private fun DebtCard(
    debt: Debt,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val typeColor = if (debt.type == DebtType.THEY_OWE) colors.IncomeColor else colors.ExpenseColor

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.Foreground,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Type indicator dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(typeColor),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = debt.personName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary,
                )
                Text(
                    text = if (debt.type == DebtType.THEY_OWE) "Me debe" else "Le debo",
                    fontSize = 13.sp,
                    color = colors.TextSecondary,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.format(debt.remainingAmount, debt.currencyCode),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = typeColor,
                )
                if (debt.isSettled) {
                    Text(
                        text = "Saldada",
                        fontSize = 12.sp,
                        color = colors.Success,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}
