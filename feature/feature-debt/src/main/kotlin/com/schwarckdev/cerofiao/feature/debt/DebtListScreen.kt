package com.schwarckdev.cerofiao.feature.debt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtType
import com.schwarckdev.cerofiao.core.ui.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtListScreen(
    onBack: () -> Unit,
    onAddDebt: () -> Unit,
    onDebtClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DebtListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Deudas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(CeroFiaoIcons.Back, contentDescription = "Volver")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDebt) {
                Icon(CeroFiaoIcons.Add, contentDescription = "Nueva deuda")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // Summary card
            if (!uiState.isLoading && (uiState.theyOweTotals.isNotEmpty() || uiState.iOweTotals.isNotEmpty())) {
                DebtSummaryCard(
                    theyOweTotals = uiState.theyOweTotals,
                    iOweTotals = uiState.iOweTotals,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = uiState.selectedFilter == DebtFilter.ALL,
                    onClick = { viewModel.setFilter(DebtFilter.ALL) },
                    label = { Text("Todas") },
                )
                FilterChip(
                    selected = uiState.selectedFilter == DebtFilter.THEY_OWE,
                    onClick = { viewModel.setFilter(DebtFilter.THEY_OWE) },
                    label = { Text("Me deben") },
                )
                FilterChip(
                    selected = uiState.selectedFilter == DebtFilter.I_OWE,
                    onClick = { viewModel.setFilter(DebtFilter.I_OWE) },
                    label = { Text("Debo") },
                )
            }

            if (uiState.debts.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    EmptyState(
                        icon = CeroFiaoIcons.Accounts,
                        title = "Sin deudas",
                        description = "Registra las deudas que te deben o que debes",
                        actionLabel = "Agregar deuda",
                        onAction = onAddDebt,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = uiState.debts,
                        key = { it.id },
                    ) { debt ->
                        val dismissState = rememberSwipeToDismissBoxState()

                        LaunchedEffect(dismissState.currentValue) {
                            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteDebt(debt.id)
                            }
                        }

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
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
}

@Composable
private fun DebtSummaryCard(
    theyOweTotals: List<CurrencyTotal>,
    iOweTotals: List<CurrencyTotal>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Me deben",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (theyOweTotals.isEmpty()) {
                    Text(
                        text = "$0.00",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                    )
                } else {
                    theyOweTotals.forEach { total ->
                        Text(
                            text = CurrencyFormatter.format(total.amount, total.currencyCode),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50),
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Debo",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (iOweTotals.isEmpty()) {
                    Text(
                        text = "$0.00",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF44336),
                    )
                } else {
                    iOweTotals.forEach { total ->
                        Text(
                            text = CurrencyFormatter.format(total.amount, total.currencyCode),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF44336),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DebtCard(
    debt: Debt,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val typeColor = if (debt.type == DebtType.THEY_OWE) Color(0xFF4CAF50) else Color(0xFFF44336)
    val typeLabel = if (debt.type == DebtType.THEY_OWE) "Me debe" else "Debo"

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = debt.personName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = typeColor,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyFormatter.format(debt.remainingAmount, debt.currencyCode),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = typeColor,
                    )
                    if (debt.remainingAmount != debt.originalAmount) {
                        Text(
                            text = "de ${CurrencyFormatter.format(debt.originalAmount, debt.currencyCode)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            val dueDate = debt.dueDate
            if (dueDate != null || debt.isSettled) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    if (dueDate != null) {
                        Text(
                            text = "Vence: ${DateUtils.formatDisplayDate(dueDate)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (debt.isSettled) {
                        Text(
                            text = "Saldada",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50),
                        )
                    }
                }
            }

            val note = debt.note
            if (!note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
    }
}
