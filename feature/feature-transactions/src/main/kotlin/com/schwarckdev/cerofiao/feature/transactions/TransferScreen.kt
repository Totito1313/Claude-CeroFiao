package com.schwarckdev.cerofiao.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransferScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

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
                text = "Transferencia",
                style = MaterialTheme.typography.titleMedium,
                color = t.text,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // From account
            Text(
                text = "Desde",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.accounts.forEach { account ->
                    FilterChip(
                        selected = account.id == uiState.fromAccountId,
                        onClick = { viewModel.selectFromAccount(account.id) },
                        label = {
                            Text("${account.name} (${account.currencyCode})")
                        },
                    )
                }
            }

            // To account
            Text(
                text = "Hacia",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.accounts
                    .filter { it.id != uiState.fromAccountId }
                    .forEach { account ->
                        FilterChip(
                            selected = account.id == uiState.toAccountId,
                            onClick = { viewModel.selectToAccount(account.id) },
                            label = {
                                Text("${account.name} (${account.currencyCode})")
                            },
                        )
                    }
            }

            // Amount
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = viewModel::setAmount,
                label = {
                    val currency = uiState.fromAccount?.currencyCode ?: ""
                    Text("Monto a enviar ($currency)")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            // Received amount (cross-currency)
            if (uiState.isCrossCurrency) {
                OutlinedTextField(
                    value = uiState.receivedAmount,
                    onValueChange = viewModel::setReceivedAmount,
                    label = {
                        val currency = uiState.toAccount?.currencyCode ?: ""
                        Text("Monto recibido ($currency)")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Commission
            OutlinedTextField(
                value = uiState.commissionPercent,
                onValueChange = viewModel::setCommissionPercent,
                label = { Text("Comisión % (opcional)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            // Note
            OutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::setNote,
                label = { Text("Nota (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))

            // Transfer button
            Button(
                onClick = viewModel::transfer,
                enabled = uiState.isValid && !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp),
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        strokeWidth = 2.dp,
                    )
                }
                Text("Transferir")
            }
        }
    }
}
