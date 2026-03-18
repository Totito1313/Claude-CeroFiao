package com.schwarckdev.cerofiao.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.ui.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onAddTransaction: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onActivityClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: TransactionListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Transacciones") },
                actions = {
                    IconButton(onClick = onActivityClick) {
                        Icon(CeroFiaoIcons.More, contentDescription = "Actividad reciente")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransaction) {
                Icon(CeroFiaoIcons.Add, contentDescription = "Nueva transacción")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedTypeFilter == null,
                        onClick = { viewModel.setTypeFilter(null) },
                        label = { Text("Todas") },
                    )
                }
                item {
                    FilterChip(
                        selected = uiState.selectedTypeFilter == TransactionType.EXPENSE,
                        onClick = {
                            viewModel.setTypeFilter(
                                if (uiState.selectedTypeFilter == TransactionType.EXPENSE) null
                                else TransactionType.EXPENSE,
                            )
                        },
                        label = { Text("Gastos") },
                    )
                }
                item {
                    FilterChip(
                        selected = uiState.selectedTypeFilter == TransactionType.INCOME,
                        onClick = {
                            viewModel.setTypeFilter(
                                if (uiState.selectedTypeFilter == TransactionType.INCOME) null
                                else TransactionType.INCOME,
                            )
                        },
                        label = { Text("Ingresos") },
                    )
                }
                item {
                    FilterChip(
                        selected = uiState.selectedTypeFilter == TransactionType.TRANSFER,
                        onClick = {
                            viewModel.setTypeFilter(
                                if (uiState.selectedTypeFilter == TransactionType.TRANSFER) null
                                else TransactionType.TRANSFER,
                            )
                        },
                        label = { Text("Transferencias") },
                    )
                }
            }

            // Account filter chips
            if (uiState.accounts.size > 1) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedAccountId == null,
                            onClick = { viewModel.setAccountFilter(null) },
                            label = { Text("Todas las cuentas") },
                        )
                    }
                    items(
                        items = uiState.accounts,
                        key = { it.id },
                    ) { account ->
                        FilterChip(
                            selected = uiState.selectedAccountId == account.id,
                            onClick = {
                                viewModel.setAccountFilter(
                                    if (uiState.selectedAccountId == account.id) null
                                    else account.id,
                                )
                            },
                            label = { Text(account.name) },
                        )
                    }
                }
            }

            if (uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    EmptyState(
                        icon = CeroFiaoIcons.Expense,
                        title = "Sin transacciones",
                        description = "Registra tu primer gasto o ingreso",
                        actionLabel = "Agregar",
                        onAction = onAddTransaction,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = uiState.transactions,
                        key = { it.id },
                    ) { transaction ->
                        val dismissState = rememberSwipeToDismissBoxState()

                        LaunchedEffect(dismissState.currentValue) {
                            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteTransaction(transaction.id)
                            }
                        }

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.errorContainer),
                                    contentAlignment = Alignment.CenterEnd,
                                ) {
                                    Icon(
                                        imageVector = CeroFiaoIcons.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(end = 16.dp),
                                    )
                                }
                            },
                        ) {
                            TransactionListItem(
                                transaction = transaction,
                                accountName = uiState.accounts
                                    .find { it.id == transaction.accountId }?.name,
                                onClick = { onTransactionClick(transaction.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionListItem(
    transaction: Transaction,
    accountName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = when (transaction.type) {
        TransactionType.INCOME -> CeroFiaoIcons.Income
        TransactionType.EXPENSE -> CeroFiaoIcons.Expense
        TransactionType.TRANSFER -> CeroFiaoIcons.Transfer
    }
    val iconColor = when (transaction.type) {
        TransactionType.INCOME -> Color(0xFF4CAF50)
        TransactionType.EXPENSE -> Color(0xFFF44336)
        TransactionType.TRANSFER -> Color(0xFF2196F3)
    }
    val sign = when (transaction.type) {
        TransactionType.INCOME -> "+"
        TransactionType.EXPENSE -> "-"
        TransactionType.TRANSFER -> ""
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = iconColor.copy(alpha = 0.12f),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = transaction.type.name,
                    tint = iconColor,
                    modifier = Modifier.padding(8.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.note ?: transaction.type.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = DateUtils.formatDisplayDate(transaction.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (accountName != null) {
                        Text(
                            text = accountName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Text(
                text = "$sign${CurrencyFormatter.format(transaction.amount, transaction.currencyCode)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = iconColor,
            )
        }
    }
}
