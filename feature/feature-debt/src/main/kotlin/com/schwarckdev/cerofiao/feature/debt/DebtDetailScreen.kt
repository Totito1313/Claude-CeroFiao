package com.schwarckdev.cerofiao.feature.debt
import com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.ui.CeroFiaoDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
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
    val t = CeroFiaoTheme.tokens
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
        CeroFiaoDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = "Eliminar deuda",
            text = "¿Estás seguro de que quieres eliminar esta deuda? Esta acción no se puede deshacer.",
            confirmButton = {
                CeroFiaoButton(
                    text = "Eliminar",
                    onClick = {
                        viewModel.deleteDebt()
                        showDeleteDialog = false
                        onBack()
                    },
                    variant = CeroFiaoButtonVariant.Danger
                )
            },
            dismissButton = {
                CeroFiaoButton(
                    text = "Cancelar",
                    onClick = { showDeleteDialog = false },
                    variant = CeroFiaoButtonVariant.Text
                )
            },
        )
    }

    val debt = uiState.debt

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).padding(top = 90.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Top bar with back button, title, and delete action
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = t.iconBg,
                    modifier = Modifier.size(40.dp),
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = CeroFiaoIcons.Back,
                            contentDescription = "Volver",
                            tint = t.text,
                        )
                    }
                }

                Text(
                    text = debt?.personName ?: "Deuda",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = t.text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                )

                Surface(
                    shape = CircleShape,
                    color = t.iconBg,
                    modifier = Modifier.size(40.dp),
                ) {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = CeroFiaoIcons.Delete,
                            contentDescription = "Eliminar",
                            tint = t.danger,
                        )
                    }
                }
            }
        }

        // Debt info card
        if (debt != null) {
            val typeColor = if (debt.type == DebtType.THEY_OWE) Color(0xFF4CAF50) else Color(0xFFF44336)
            val typeLabel = if (debt.type == DebtType.THEY_OWE) "Me debe" else "Le debo"

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF8A2BE2).copy(alpha = 0.15f),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = typeLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = t.textSecondary,
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
                                color = t.textSecondary,
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
                    color = t.surface,
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
                        CeroFiaoButton(
                            text = "Registrar pago",
                            onClick = { showPaymentDialog = true },
                            modifier = Modifier.weight(1f),
                        )
                        CeroFiaoButton(
                            text = "Marcar saldada",
                            onClick = { viewModel.markAsSettled() },
                            modifier = Modifier.weight(1f),
                            variant = CeroFiaoButtonVariant.Secondary,
                        )
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
                color = t.text,
            )
        }

        if (uiState.payments.isEmpty()) {
            item {
                Text(
                    text = "No hay pagos registrados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = t.textSecondary,
                )
            }
        } else {
            items(uiState.payments, key = { it.id }) { payment ->
                PaymentRow(payment = payment)
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
    val t = CeroFiaoTheme.tokens
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = t.textSecondary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = t.text,
        )
    }
}

@Composable
private fun PaymentRow(
    payment: DebtPayment,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = t.surface,
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
                    color = t.text,
                )
                Text(
                    text = DateUtils.formatDisplayDate(payment.paidAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = t.textSecondary,
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
    val t = CeroFiaoTheme.tokens
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    CeroFiaoDialog(
        onDismissRequest = onDismiss,
        title = "Registrar pago",
        text = "Monto pendiente: ${CurrencyFormatter.format(maxAmount, currencyCode)}",
        content = {
            Column {
                Spacer(modifier = Modifier.height(4.dp))
                CeroFiaoTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Monto",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
                Spacer(modifier = Modifier.height(8.dp))
                CeroFiaoTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = "Nota (opcional)",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            CeroFiaoButton(
                text = "Guardar",
                onClick = { onConfirm(amount, note) },
                enabled = (amount.toDoubleOrNull() ?: 0.0) > 0,
            )
        },
        dismissButton = {
            CeroFiaoButton(
                text = "Cancelar",
                onClick = onDismiss,
                variant = CeroFiaoButtonVariant.Text
            )
        },
    )
}

