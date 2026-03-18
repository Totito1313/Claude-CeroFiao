package com.schwarckdev.cerofiao.feature.transactions

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionEntryScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditMode) "Editar transacción" else "Nueva transacción") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(CeroFiaoIcons.Back, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Transaction type selector
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                val types = listOf(
                    TransactionType.EXPENSE to "Gasto",
                    TransactionType.INCOME to "Ingreso",
                )
                types.forEachIndexed { index, (type, label) ->
                    SegmentedButton(
                        selected = uiState.transactionType == type,
                        onClick = { viewModel.setTransactionType(type) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = types.size,
                        ),
                    ) {
                        Text(label)
                    }
                }
            }

            // Amount (system keyboard)
            OutlinedTextField(
                value = uiState.amountText,
                onValueChange = viewModel::setAmount,
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
            )

            // Currency selector
            Text(
                text = "Moneda",
                style = MaterialTheme.typography.labelLarge,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("USD", "VES", "USDT", "EUR").forEach { code ->
                    FilterChip(
                        selected = uiState.selectedCurrencyCode == code,
                        onClick = { viewModel.selectCurrency(code) },
                        label = { Text(code) },
                    )
                }
            }

            // Multi-currency reference card
            if (uiState.currencyEquivalents.isNotEmpty() && uiState.amount > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Equivalentes",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        uiState.currencyEquivalents.forEach { (currency, amount) ->
                            Text(
                                text = CurrencyFormatter.format(amount, currency),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            // Account selector
            Text(
                text = "Cuenta",
                style = MaterialTheme.typography.labelLarge,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.accounts.forEach { account ->
                    FilterChip(
                        selected = account.id == uiState.selectedAccountId,
                        onClick = { viewModel.selectAccount(account.id) },
                        label = { Text("${account.name} (${account.currencyCode})") },
                    )
                }
            }

            // Category selector
            if (uiState.categories.isNotEmpty()) {
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.labelLarge,
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    uiState.categories.forEach { category ->
                        FilterChip(
                            selected = category.id == uiState.selectedCategoryId,
                            onClick = { viewModel.selectCategory(category.id) },
                            label = { Text(category.name) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(CeroFiaoIcons.getCategoryIconRes(category.iconName)),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                )
                            },
                        )
                    }
                }
            }

            // Note
            OutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::setNote,
                label = { Text("Nota (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Category suggestion chip
            val suggestedCategoryId = uiState.suggestedCategoryId
            if (suggestedCategoryId != null && uiState.selectedCategoryId == null) {
                val suggestedCategory = uiState.categories.find { it.id == suggestedCategoryId }
                if (suggestedCategory != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        tonalElevation = 1.dp,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.applySuggestedCategory() }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                painter = painterResource(CeroFiaoIcons.getCategoryIconRes(suggestedCategory.iconName)),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Text(
                                text = "Sugerencia: ${suggestedCategory.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = "Aplicar",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            // Save button
            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.amount > 0,
            ) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
