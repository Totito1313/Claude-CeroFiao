package com.schwarckdev.cerofiao.feature.transactions
import com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.schwarckdev.cerofiao.core.model.RecurrenceType
import com.schwarckdev.cerofiao.core.model.RecurringTransaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.ui.EmptyState
import com.schwarckdev.cerofiao.core.ui.GlassCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringListScreen(
    onBack: () -> Unit,
    onAddRecurring: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecurringListViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).padding(top = 90.dp),
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
                text = "Transacciones recurrentes",
                style = MaterialTheme.typography.titleMedium,
                color = t.text,
                fontWeight = FontWeight.SemiBold,
            )
            Surface(shape = CircleShape, color = t.iconBg) {
                IconButton(onClick = onAddRecurring) {
                    Icon(CeroFiaoIcons.Add, contentDescription = "Agregar recurrente", tint = t.text)
                }
            }
        }

        if (uiState.recurringTransactions.isEmpty()) {
            EmptyState(
                icon = CeroFiaoIcons.Transactions,
                title = "Sin recurrentes",
                description = "Agrega transacciones que se repiten automáticamente",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 100.dp),
            ) {
                if (uiState.upcomingTransactions.isNotEmpty()) {
                    item {
                        Text(
                            text = "Próximas (30 días)",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF8A2BE2),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                    items(uiState.upcomingTransactions, key = { "upcoming_${it.id}" }) { recurring ->
                        UpcomingCard(recurring = recurring)
                    }
                    item {
                        Text(
                            text = "Todas las recurrentes",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF8A2BE2),
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                        )
                    }
                }
                items(uiState.recurringTransactions, key = { it.id }) { recurring ->
                    RecurringCard(
                        recurring = recurring,
                        onToggleActive = { viewModel.toggleActive(recurring.id, !recurring.isActive) },
                        onDelete = { viewModel.delete(recurring.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun UpcomingCard(
    recurring: RecurringTransaction,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val typeLabel = when (recurring.type) {
        TransactionType.EXPENSE -> "Gasto"
        TransactionType.INCOME -> "Ingreso"
        TransactionType.TRANSFER -> "Transferencia"
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF8A2BE2).copy(alpha = 0.12f),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(recurring.title, style = MaterialTheme.typography.bodyLarge, color = t.text)
                Text(
                    "$typeLabel · ${CurrencyFormatter.format(recurring.amount, recurring.currencyCode)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = t.textSecondary,
                )
            }
            Text(
                text = DateUtils.formatDisplayDate(recurring.nextDueDate),
                style = MaterialTheme.typography.labelMedium,
                color = t.textSecondary,
            )
        }
    }
}

@Composable
private fun RecurringCard(
    recurring: RecurringTransaction,
    onToggleActive: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val recurrenceLabel = when (recurring.recurrence) {
        RecurrenceType.DAILY -> "Diario"
        RecurrenceType.WEEKLY -> "Semanal"
        RecurrenceType.MONTHLY -> "Mensual"
        RecurrenceType.YEARLY -> "Anual"
    }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(recurring.title, style = MaterialTheme.typography.bodyLarge, color = t.text)
                Text(
                    "${CurrencyFormatter.format(recurring.amount, recurring.currencyCode)} · $recurrenceLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = t.textSecondary,
                )
                Text(
                    "Próxima: ${DateUtils.formatDisplayDate(recurring.nextDueDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8A2BE2),
                )
            }
            Switch(checked = recurring.isActive, onCheckedChange = { onToggleActive() })
            IconButton(onClick = onDelete) {
                Icon(CeroFiaoIcons.Delete, contentDescription = "Eliminar", tint = t.danger)
            }
        }
    }
}

