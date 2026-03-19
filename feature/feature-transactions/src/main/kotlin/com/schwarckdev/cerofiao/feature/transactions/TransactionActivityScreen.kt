package com.schwarckdev.cerofiao.feature.transactions

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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RefreshCw
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionLog
import com.schwarckdev.cerofiao.core.model.TransactionLogAction
import com.schwarckdev.cerofiao.core.ui.EmptyState
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionActivityScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionActivityViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val logs by viewModel.logs.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg),
    ) {
        // Top bar row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(shape = CircleShape, color = t.iconBg) {
                IconButton(onClick = onBack) {
                    Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
                }
            }
            Text(
                text = "Actividad reciente",
                style = MaterialTheme.typography.titleMedium,
                color = t.text,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        if (logs.isEmpty()) {
            EmptyState(
                icon = CeroFiaoIcons.Transactions,
                title = "Sin actividad",
                description = "No hay actividad registrada",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 100.dp),
            ) {
                items(logs, key = { it.id }) { log ->
                    TransactionLogCard(
                        log = log,
                        onRestore = if (log.action == TransactionLogAction.DELETED) {
                            { viewModel.restoreTransaction(log) }
                        } else {
                            null
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionLogCard(
    log: TransactionLog,
    onRestore: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val transaction = try {
        Json.decodeFromString<Transaction>(log.snapshotJson)
    } catch (_: Exception) {
        null
    }

    val actionLabel = when (log.action) {
        TransactionLogAction.CREATED -> "Creada"
        TransactionLogAction.EDITED -> "Editada"
        TransactionLogAction.DELETED -> "Eliminada"
    }

    val actionColor = when (log.action) {
        TransactionLogAction.CREATED -> Color(0xFF8A2BE2)
        TransactionLogAction.EDITED -> Color(0xFFF59E0B)
        TransactionLogAction.DELETED -> t.danger
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = t.surface,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = actionColor,
                    )
                    Text(
                        text = DateUtils.formatDisplayDateTime(log.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = t.textSecondary,
                    )
                }

                if (transaction != null) {
                    Text(
                        text = "${transaction.type.name} · ${transaction.amount} ${transaction.currencyCode}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = t.text,
                    )
                    val note = transaction.note
                    if (!note.isNullOrBlank()) {
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodySmall,
                            color = t.textSecondary,
                        )
                    }
                }
            }

            if (onRestore != null) {
                IconButton(onClick = onRestore) {
                    Icon(
                        imageVector = Lucide.RefreshCw,
                        contentDescription = "Restaurar",
                        tint = Color(0xFF8A2BE2),
                    )
                }
            }
        }
    }
}
