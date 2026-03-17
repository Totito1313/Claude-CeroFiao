package com.schwarckdev.cerofiao.feature.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) onBack()
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar transacción") },
            text = { Text("¿Estás seguro de que deseas eliminar esta transacción? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteTransaction()
                        onBack()
                    },
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    val tx = uiState.transaction
                    if (tx != null && tx.type != TransactionType.TRANSFER) {
                        IconButton(onClick = { onEdit(tx.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    }
                    if (tx != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        val transaction = uiState.transaction

        if (transaction == null) {
            // Loading or not found
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Amount header
                AmountHeader(
                    transaction = transaction,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Details card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow(
                            label = "Tipo",
                            value = when (transaction.type) {
                                TransactionType.EXPENSE -> "Gasto"
                                TransactionType.INCOME -> "Ingreso"
                                TransactionType.TRANSFER -> "Transferencia"
                            },
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        DetailRow(
                            label = "Cuenta",
                            value = uiState.account?.name ?: "—",
                        )

                        if (transaction.type == TransactionType.TRANSFER && uiState.transferToAccount != null) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Cuenta destino",
                                value = uiState.transferToAccount?.name ?: "—",
                            )
                        }

                        if (uiState.category != null) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Categoría",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Icon(
                                        imageVector = CeroFiaoIcons.getCategoryIcon(uiState.category!!.iconName),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    )
                                    Text(
                                        text = uiState.category!!.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        DetailRow(
                            label = "Fecha",
                            value = DateUtils.formatDisplayDateTime(transaction.date),
                        )

                        val note = transaction.note
                        if (!note.isNullOrBlank()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Nota",
                                value = note,
                            )
                        }

                        val commission = transaction.transferCommission
                        if (commission != null && commission > 0) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Comisión",
                                value = CurrencyFormatter.format(
                                    commission,
                                    transaction.transferCommissionCurrency ?: transaction.currencyCode,
                                ),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Exchange rate info
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Información cambiaria",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        DetailRow(
                            label = "Equivalente USD",
                            value = CurrencyFormatter.format(transaction.amountInUsd, "USD"),
                        )

                        if (transaction.currencyCode != "USD") {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Tasa usada",
                                value = "1 USD = ${CurrencyFormatter.format(1.0 / transaction.exchangeRateToUsd, transaction.currencyCode, showSymbol = false)} ${transaction.currencyCode}",
                            )
                        }

                        val rateSource = transaction.exchangeRateSource
                        if (rateSource != null) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Fuente",
                                value = rateSource.name,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Metadata
                Text(
                    text = "Creado: ${DateUtils.formatDisplayDateTime(transaction.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                if (transaction.updatedAt != transaction.createdAt) {
                    Text(
                        text = "Modificado: ${DateUtils.formatDisplayDateTime(transaction.updatedAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AmountHeader(
    transaction: com.schwarckdev.cerofiao.core.model.Transaction,
) {
    val iconColor = when (transaction.type) {
        TransactionType.INCOME -> Color(0xFF4CAF50)
        TransactionType.EXPENSE -> Color(0xFFF44336)
        TransactionType.TRANSFER -> Color(0xFF2196F3)
    }
    val icon = when (transaction.type) {
        TransactionType.INCOME -> CeroFiaoIcons.Income
        TransactionType.EXPENSE -> CeroFiaoIcons.Expense
        TransactionType.TRANSFER -> CeroFiaoIcons.Transfer
    }
    val sign = when (transaction.type) {
        TransactionType.INCOME -> "+"
        TransactionType.EXPENSE -> "-"
        TransactionType.TRANSFER -> ""
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = iconColor.copy(alpha = 0.12f),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = transaction.type.name,
                tint = iconColor,
                modifier = Modifier.padding(14.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$sign${CurrencyFormatter.format(transaction.amount, transaction.currencyCode)}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = iconColor,
        )

        Text(
            text = when (transaction.type) {
                TransactionType.EXPENSE -> "Gasto"
                TransactionType.INCOME -> "Ingreso"
                TransactionType.TRANSFER -> "Transferencia"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}
