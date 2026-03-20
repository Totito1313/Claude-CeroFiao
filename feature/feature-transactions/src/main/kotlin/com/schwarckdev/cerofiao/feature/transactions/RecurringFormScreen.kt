package com.schwarckdev.cerofiao.feature.transactions
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import androidx.compose.foundation.verticalScroll
import com.schwarckdev.cerofiao.core.model.RecurrenceType
import com.schwarckdev.cerofiao.core.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecurringFormScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecurringFormViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
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
        ) {
            Surface(shape = CircleShape, color = t.iconBg) {
                IconButton(onClick = onBack) {
                    Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
                }
            }
            Text(
                text = "Nueva recurrente",
                style = MaterialTheme.typography.titleMedium,
                color = t.text,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CeroFiaoTextField(
                value = uiState.title,
                onValueChange = viewModel::setTitle,
                label = "Título",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            CeroFiaoTextField(
                value = uiState.amount,
                onValueChange = viewModel::setAmount,
                label = "Monto",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
            )

            // Currency selector
            Text(text = "Moneda", style = MaterialTheme.typography.labelLarge, color = t.text)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("USD", "VES", "USDT", "EUR").forEach { code ->
                    OptionChip(
                        label = code,
                        selected = uiState.selectedCurrencyCode == code,
                        onClick = { viewModel.selectCurrency(code) }
                    )
                }
            }

            // Transaction type chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(TransactionType.EXPENSE to "Gasto", TransactionType.INCOME to "Ingreso").forEach { (type, label) ->
                    OptionChip(
                        label = label,
                        selected = uiState.transactionType == type,
                        onClick = { viewModel.setTransactionType(type) }
                    )
                }
            }

            // Recurrence chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf(
                    RecurrenceType.DAILY to "Diario",
                    RecurrenceType.WEEKLY to "Semanal",
                    RecurrenceType.MONTHLY to "Mensual",
                    RecurrenceType.YEARLY to "Anual",
                ).forEach { (recurrence, label) ->
                    OptionChip(
                        label = label,
                        selected = uiState.recurrence == recurrence,
                        onClick = { viewModel.setRecurrence(recurrence) }
                    )
                }
            }

            // Start date picker
            Text(text = "Fecha de inicio", style = MaterialTheme.typography.labelLarge, color = t.text)
            CeroFiaoButton(
                text = DateUtils.formatDisplayDate(uiState.startDate),
                onClick = { showDatePicker = true },
                variant = CeroFiaoButtonVariant.Secondary,
                modifier = Modifier.fillMaxWidth()
            )

            // Account selector
            if (uiState.accounts.isNotEmpty()) {
                var accountExpanded by remember { mutableStateOf(false) }
                val selectedAccount = uiState.accounts.find { it.id == uiState.selectedAccountId }
                ExposedDropdownMenuBox(
                    expanded = accountExpanded,
                    onExpandedChange = { accountExpanded = it },
                ) {
                    CeroFiaoTextField(
                        value = selectedAccount?.let { "${it.name} (${it.currencyCode})" } ?: "",
                        onValueChange = {},
                        label = "Cuenta",
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
                    CeroFiaoTextField(
                        value = selectedCategory?.name ?: "Sin categoría",
                        onValueChange = {},
                        label = "Categoría",
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

            CeroFiaoTextField(
                value = uiState.note,
                onValueChange = viewModel::setNote,
                label = "Nota (opcional)",
                modifier = Modifier.fillMaxWidth(),
            )

            CeroFiaoButton(
                text = "Guardar recurrente",
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.title.isNotBlank() && uiState.amount.isNotBlank(),
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = 100.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.startDate,
        )
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                CeroFiaoButton(
                    text = "OK",
                    variant = CeroFiaoButtonVariant.Text,
                    onClick = {
                        datePickerState.selectedDateMillis?.let { viewModel.setStartDate(it) }
                        showDatePicker = false
                    },
                )
            },
            dismissButton = {
                CeroFiaoButton(
                    text = "Cancelar",
                    variant = CeroFiaoButtonVariant.Text,
                    onClick = { showDatePicker = false }
                )
            },
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun OptionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme.tokens
    val bgColor = if (selected) Color(0x148A2BE2) else t.pillBg
    val borderColor = if (selected) Color(0x268A2BE2) else Color.Transparent
    val textColor = if (selected) Color(0xFF8A2BE2) else t.textSecondary

    Surface(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(CeroFiaoShapes.ChipRadius))
            .clickable(onClick = onClick),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(CeroFiaoShapes.ChipRadius),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        androidx.compose.material3.Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

