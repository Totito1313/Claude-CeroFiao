package com.schwarckdev.cerofiao.feature.transactions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.LayoutList
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trash2
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.ui.EmptyState
import com.schwarckdev.cerofiao.feature.transactions.components.AccountFilterSheet
import com.schwarckdev.cerofiao.feature.transactions.components.CategoryFilterSheet
import com.schwarckdev.cerofiao.feature.transactions.components.SortOrderSheet
import com.schwarckdev.cerofiao.feature.transactions.components.TransactionDateHeader
import com.schwarckdev.cerofiao.feature.transactions.components.TransactionFilterChips
import com.schwarckdev.cerofiao.feature.transactions.components.TransactionItem
import com.schwarckdev.cerofiao.feature.transactions.components.TransactionSummaryHero
import com.schwarckdev.cerofiao.feature.transactions.components.TransactionTypeFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onAddTransaction: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onActivityClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: TransactionListViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Standard, title = "Historial de Movimientos")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAccountSheet by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background)
            .statusBarsPadding()
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Summary Hero with animated content
        TransactionSummaryHero(
            totalIncomeUsd = uiState.totalIncomeUsd,
            totalExpenseUsd = uiState.totalExpenseUsd,
            selectedTypeFilter = uiState.selectedTypeFilter,
            monthOverMonthPercent = uiState.monthOverMonthPercent,
        )

        // Type filter tabs
        TransactionTypeFilter(
            selectedType = uiState.selectedTypeFilter,
            onTypeSelected = { viewModel.setTypeFilter(it) },
        )

        Spacer(Modifier.height(20.dp))

        // Historial section header + filter chips
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            SectionHeaderRow(selectedTypeFilter = uiState.selectedTypeFilter)
            Spacer(Modifier.height(10.dp))
            TransactionFilterChips(
                hasAccountFilter = uiState.selectedAccountId != null,
                hasCategoryFilter = uiState.selectedCategoryId != null,
                onAccountsClick = { showAccountSheet = true },
                onSortClick = { showSortSheet = true },
                onCategoryClick = { showCategorySheet = true },
            )
        }

        Spacer(Modifier.height(8.dp))

        // Transaction list with crossfade on filter change
        AnimatedContent(
            targetState = uiState.selectedTypeFilter,
            transitionSpec = {
                fadeIn(tween(200)) togetherWith fadeOut(tween(150))
            },
            label = "listCrossfade",
        ) { _ ->
            if (uiState.groupedTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    EmptyState(
                        icon = Lucide.LayoutList,
                        title = "Sin movimientos",
                        description = "Agrega tu primer movimiento para verlo aquí",
                        actionLabel = "Agregar movimiento",
                        onAction = onAddTransaction,
                    )
                }
            } else {
                val animatedKeys = remember { mutableSetOf<String>() }
                var globalIndex = 0

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                ) {
                    uiState.groupedTransactions.forEach { group ->
                        item(key = "header_${group.dateMillis}") {
                            TransactionDateHeader(dateMillis = group.dateMillis)
                        }
                        items(
                            items = group.transactions,
                            key = { it.transaction.id },
                        ) { txWithCat ->
                            val txId = txWithCat.transaction.id
                            val isNew = txId !in animatedKeys
                            val staggerDelay = if (isNew && globalIndex < 12) {
                                (globalIndex * 40).coerceAtMost(480)
                            } else 0
                            globalIndex++
                            if (isNew) animatedKeys.add(txId)

                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(tween(300, delayMillis = staggerDelay)) +
                                    slideInVertically(
                                        initialOffsetY = { it / 5 },
                                        animationSpec = tween(300, delayMillis = staggerDelay),
                                    ),
                            ) {
                                SwipeableTransactionItem(
                                    item = txWithCat,
                                    onClick = { onTransactionClick(txId) },
                                    onDelete = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.deleteTransaction(txId)
                                    },
                                )
                            }
                        }
                    }
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }

    // Bottom sheets for filters
    if (showAccountSheet) {
        AccountFilterSheet(
            accounts = uiState.accounts,
            selectedAccountId = uiState.selectedAccountId,
            onAccountSelected = { viewModel.setAccountFilter(it) },
            onDismiss = { showAccountSheet = false },
        )
    }

    if (showSortSheet) {
        SortOrderSheet(
            currentOrder = uiState.sortOrder,
            onOrderSelected = { viewModel.setSortOrder(it) },
            onDismiss = { showSortSheet = false },
        )
    }

    if (showCategorySheet) {
        CategoryFilterSheet(
            categories = uiState.categories,
            selectedCategoryId = uiState.selectedCategoryId,
            onCategorySelected = { viewModel.setCategoryFilter(it) },
            onDismiss = { showCategorySheet = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableTransactionItem(
    item: TransactionWithCategory,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val colors = CeroFiaoDesign.colors
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
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
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextOnDark,
                    )
                    Icon(
                        imageVector = Lucide.Trash2,
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(18.dp),
                        tint = colors.TextOnDark,
                    )
                }
            }
        },
    ) {
        Box(modifier = Modifier.background(colors.Background)) {
            TransactionItem(
                item = item,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun SectionHeaderRow(
    selectedTypeFilter: TransactionType?,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val typeLabel = when (selectedTypeFilter) {
        TransactionType.INCOME -> "Ingreso"
        TransactionType.EXPENSE -> "Gasto"
        else -> "Movimiento"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Historial",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextPrimary,
            letterSpacing = (-0.6).sp,
        )
        Text(
            text = typeLabel,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = colors.TextSecondary,
        )
    }
}
