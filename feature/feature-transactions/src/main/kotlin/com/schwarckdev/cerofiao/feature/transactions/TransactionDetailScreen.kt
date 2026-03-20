package com.schwarckdev.cerofiao.feature.transactions
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import com.schwarckdev.cerofiao.core.ui.CeroFiaoDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import com.schwarckdev.cerofiao.core.ui.CeroFiaoDialog
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
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.TransactionType

@Composable
fun TransactionDetailScreen(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionDetailViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) onBack()
    }

    if (showDeleteDialog) {
        CeroFiaoDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = "Eliminar transacción",
            text = "¿Estás seguro de que deseas eliminar esta transacción? Esta acción no se puede deshacer.",
            confirmButton = {
                CeroFiaoButton(
                    text = "Eliminar",
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteTransaction()
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).statusBarsPadding(),
    ) {
        // Top bar row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Surface(shape = CircleShape, color = t.iconBg) {
                IconButton(onClick = onBack) {
                    Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
                }
            }
            Text(
                text = "Detalle",
                style = MaterialTheme.typography.titleMedium,
                color = t.text,
                fontWeight = FontWeight.SemiBold,
            )
            Row {
                val tx = uiState.transaction
                if (tx != null && tx.type != TransactionType.TRANSFER) {
                    Surface(shape = CircleShape, color = t.iconBg) {
                        IconButton(onClick = { onEdit(tx.id) }) {
                            Icon(CeroFiaoIcons.Edit, contentDescription = "Editar", tint = t.text)
                        }
                    }
                }
                if (tx != null) {
                    Surface(shape = CircleShape, color = t.iconBg) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                CeroFiaoIcons.Delete,
                                contentDescription = "Eliminar",
                                tint = t.danger,
                            )
                        }
                    }
                }
            }
        }

        val transaction = uiState.transaction

        if (transaction == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = t.textSecondary,
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    color = t.surface,
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

                        CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))

                        DetailRow(
                            label = "Cuenta",
                            value = uiState.account?.name ?: "—",
                        )

                        if (transaction.type == TransactionType.TRANSFER && uiState.transferToAccount != null) {
                            CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Cuenta destino",
                                value = uiState.transferToAccount?.name ?: "—",
                            )
                        }

                        if (uiState.category != null) {
                            CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Categoría",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = t.textSecondary,
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(CeroFiaoIcons.getCategoryIconRes(uiState.category!!.iconName)),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = t.text,
                                    )
                                    Text(
                                        text = uiState.category!!.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = t.text,
                                    )
                                }
                            }
                        }

                        CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))

                        DetailRow(
                            label = "Fecha",
                            value = DateUtils.formatDisplayDateTime(transaction.date),
                        )

                        val note = transaction.note
                        if (!note.isNullOrBlank()) {
                            CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Nota",
                                value = note,
                            )
                        }

                        val commission = transaction.transferCommission
                        if (commission != null && commission > 0) {
                            CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))
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
                    color = t.surface,
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Información cambiaria",
                            style = MaterialTheme.typography.labelLarge,
                            color = t.textSecondary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        DetailRow(
                            label = "Equivalente USD",
                            value = CurrencyFormatter.format(transaction.amountInUsd, "USD"),
                        )

                        if (transaction.currencyCode != "USD") {
                            CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))
                            DetailRow(
                                label = "Tasa usada",
                                value = "1 USD = ${CurrencyFormatter.format(1.0 / transaction.exchangeRateToUsd, transaction.currencyCode, showSymbol = false)} ${transaction.currencyCode}",
                            )
                        }

                        val rateSource = transaction.exchangeRateSource
                        if (rateSource != null) {
                            CeroFiaoDivider(modifier = Modifier.padding(vertical = 8.dp))
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
                    color = t.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                if (transaction.updatedAt != transaction.createdAt) {
                    Text(
                        text = "Modificado: ${DateUtils.formatDisplayDateTime(transaction.updatedAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = t.textSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun AmountHeader(
    transaction: com.schwarckdev.cerofiao.core.model.Transaction,
) {
    val t = CeroFiaoTheme.tokens
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
            color = t.textSecondary,
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    val t = CeroFiaoTheme.tokens
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
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

