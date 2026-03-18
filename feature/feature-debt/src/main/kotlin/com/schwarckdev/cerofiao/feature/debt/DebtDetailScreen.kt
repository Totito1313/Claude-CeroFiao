package com.schwarckdev.cerofiao.feature.debt

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.model.DebtPayment
import com.schwarckdev.cerofiao.core.model.DebtType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DebtDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showPaymentDialog) {
        PaymentDialog(
            currencyCode = uiState.debt?.currencyCode ?: "USD",
            maxAmount = uiState.debt?.remainingAmount ?: 0.0,
            onDismiss = { showPaymentDialog = false },
            onConfirm = { amount, note ->
                viewModel.registerPayment(amount, note)
                showPaymentDialog = false
            },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar deuda") },
            text = { Text("¿Estás seguro de que quieres eliminar esta deuda? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteDebt()
                        showDeleteDialog = false
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

    val debt = uiState.debt

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(debt?.personName ?: "Deuda") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(CeroFiaoIcons.Back, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            CeroFiaoIcons.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Debt info card
            if (debt != null) {
                val typeColor = if (debt.type == DebtType.THEY_OWE) Color(0xFF4CAF50) else Color(0xFFF44336)
                val typeLabel = if (debt.type == DebtType.THEY_OWE) "Me debe" else "Le debo"

                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = typeLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = CurrencyFormatter.format(debt.remainingAmount, debt.currencyCode),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = typeColor,
                            )
                            if (debt.remainingAmount != debt.originalAmount) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "de ${CurrencyFormatter.format(debt.originalAmount, debt.currencyCode)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                )
                            }
                            if (debt.isSettled) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Saldada",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50),
                                )
                            }
                        }
                    }
                }

                // Details
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            DetailRow(label = "Persona", value = debt.personName)
                            DetailRow(label = "Moneda", value = debt.currencyCode)
                            DetailRow(label = "Creada", value = DateUtils.formatDisplayDate(debt.createdAt))
                            val dueDate = debt.dueDate
                            if (dueDate != null) {
                                DetailRow(label = "Vence", value = DateUtils.formatDisplayDate(dueDate))
                            }
                            val debtNote = debt.note
                            if (!debtNote.isNullOrBlank()) {
                                DetailRow(label = "Nota", value = debtNote)
                            }
                            val settledAt = debt.settledAt
                            if (settledAt != null) {
                                DetailRow(label = "Saldada", value = DateUtils.formatDisplayDate(settledAt))
                            }
                        }
                    }
                }

                // Action buttons
                if (!debt.isSettled) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Button(
                                onClick = { showPaymentDialog = true },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("Registrar pago")
                            }
                            OutlinedButton(
                                onClick = { viewModel.markAsSettled() },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("Marcar saldada")
                            }
                        }
                    }
                }
            }

            // Payment history header
            item {
                Text(
                    text = "Historial de pagos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (uiState.payments.isEmpty()) {
                item {
                    Text(
                        text = "No hay pagos registrados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                items(uiState.payments, key = { it.id }) { payment ->
                    PaymentRow(payment = payment)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
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

@Composable
private fun PaymentRow(
    payment: DebtPayment,
    modifier: Modifier = Modifier,
) {
    Surface(
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = payment.note ?: "Pago",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )
                Text(
                    text = DateUtils.formatDisplayDate(payment.paidAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = CurrencyFormatter.format(payment.amount, payment.currencyCode),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4CAF50),
            )
        }
    }
}

@Composable
private fun PaymentDialog(
    currencyCode: String,
    maxAmount: Double,
    onDismiss: () -> Unit,
    onConfirm: (amount: String, note: String) -> Unit,
) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar pago") },
        text = {
            Column {
                Text(
                    text = "Monto pendiente: ${CurrencyFormatter.format(maxAmount, currencyCode)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Nota (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(amount, note) },
                enabled = (amount.toDoubleOrNull() ?: 0.0) > 0,
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}
