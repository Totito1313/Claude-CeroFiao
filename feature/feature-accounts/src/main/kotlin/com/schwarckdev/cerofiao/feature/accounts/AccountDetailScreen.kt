package com.schwarckdev.cerofiao.feature.accounts

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.ui.MoneyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountDetailViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // After delete, account becomes null (soft-deleted, filtered by DAO)
    // We don't auto-navigate here since deleteAccount already calls onBack()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Estás seguro de que quieres eliminar esta cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                        onBack()
                    },
                ) {
                    Text("Eliminar", color = t.danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
        )
    }

    val account = uiState.account

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Top bar row with back button, title, and delete action
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = t.iconBg,
                    modifier = Modifier.size(40.dp),
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            CeroFiaoIcons.Back,
                            contentDescription = "Volver",
                            tint = t.text,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = account?.name ?: "Cuenta",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = t.text,
                    modifier = Modifier.weight(1f),
                )
                Surface(
                    shape = CircleShape,
                    color = t.iconBg,
                    modifier = Modifier.size(40.dp),
                ) {
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

        // Account info card
        if (account != null) {
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
                            text = account.platform.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            color = t.textSecondary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        MoneyText(
                            amount = account.balance,
                            currencyCode = account.currencyCode,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = t.text,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = account.currencyCode,
                            style = MaterialTheme.typography.bodyMedium,
                            color = t.textSecondary,
                        )
                    }
                }
            }
        }

        // Transaction history header
        item {
            Text(
                text = "Historial de transacciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = t.text,
            )
        }

        if (uiState.transactions.isEmpty()) {
            item {
                Text(
                    text = "No hay transacciones en esta cuenta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = t.textSecondary,
                )
            }
        } else {
            items(uiState.transactions, key = { it.id }) { transaction ->
                AccountTransactionRow(transaction = transaction)
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun AccountTransactionRow(
    transaction: Transaction,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val (icon, iconColor, sign) = when (transaction.type) {
        TransactionType.INCOME -> Triple(CeroFiaoIcons.Income, Color(0xFF4CAF50), "+")
        TransactionType.EXPENSE -> Triple(CeroFiaoIcons.Expense, Color(0xFFF44336), "-")
        TransactionType.TRANSFER -> Triple(CeroFiaoIcons.Transfer, Color(0xFF8A2BE2), "")
    }

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
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(8.dp),
                color = iconColor.copy(alpha = 0.12f),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.padding(6.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.note ?: transaction.type.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = t.text,
                    maxLines = 1,
                )
                Text(
                    text = DateUtils.formatDisplayDate(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = t.textSecondary,
                )
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
