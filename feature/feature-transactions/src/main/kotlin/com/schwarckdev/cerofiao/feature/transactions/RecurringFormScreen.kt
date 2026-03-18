package com.schwarckdev.cerofiao.feature.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.model.RecurrenceType
import com.schwarckdev.cerofiao.core.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringFormScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecurringFormViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Nueva recurrente") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::setTitle,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = uiState.amount,
                onValueChange = viewModel::setAmount,
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
            )

            // Transaction type chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(TransactionType.EXPENSE to "Gasto", TransactionType.INCOME to "Ingreso").forEach { (type, label) ->
                    FilterChip(
                        selected = uiState.transactionType == type,
                        onClick = { viewModel.setTransactionType(type) },
                        label = { Text(label) },
                    )
                }
            }

            // Recurrence chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    RecurrenceType.DAILY to "Diario",
                    RecurrenceType.WEEKLY to "Semanal",
                    RecurrenceType.MONTHLY to "Mensual",
                    RecurrenceType.YEARLY to "Anual",
                ).forEach { (recurrence, label) ->
                    FilterChip(
                        selected = uiState.recurrence == recurrence,
                        onClick = { viewModel.setRecurrence(recurrence) },
                        label = { Text(label) },
                    )
                }
            }

            // Account selector
            if (uiState.accounts.isNotEmpty()) {
                var accountExpanded by remember { mutableStateOf(false) }
                val selectedAccount = uiState.accounts.find { it.id == uiState.selectedAccountId }
                ExposedDropdownMenuBox(
                    expanded = accountExpanded,
                    onExpandedChange = { accountExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedAccount?.name ?: "",
                        onValueChange = {},
                        label = { Text("Cuenta") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = accountExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    )
                    ExposedDropdownMenu(
                        expanded = accountExpanded,
                        onDismissRequest = { accountExpanded = false },
                    ) {
                        uiState.accounts.forEach { account ->
                            DropdownMenuItem(
                                text = { Text("${account.name} (${account.currencyCode})") },
                                onClick = {
                                    viewModel.selectAccount(account.id)
                                    accountExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            // Category selector
            if (uiState.categories.isNotEmpty()) {
                var categoryExpanded by remember { mutableStateOf(false) }
                val selectedCategory = uiState.categories.find { it.id == uiState.selectedCategoryId }
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Sin categoría",
                        onValueChange = {},
                        label = { Text("Categoría") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                    ) {
                        uiState.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.selectCategory(category.id)
                                    categoryExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::setNote,
                label = { Text("Nota (opcional)") },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.title.isNotBlank() && uiState.amount.isNotBlank(),
            ) {
                Text("Guardar recurrente")
            }
        }
    }
}
