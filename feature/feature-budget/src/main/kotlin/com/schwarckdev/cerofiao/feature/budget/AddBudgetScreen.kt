package com.schwarckdev.cerofiao.feature.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.BudgetPeriod

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddBudgetScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddBudgetViewModel = hiltViewModel(),
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
        // Top bar row with back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (uiState.isEditMode) "Editar presupuesto" else "Nuevo presupuesto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = t.text,
            )

            Spacer(modifier = Modifier.weight(1f))

            // Invisible spacer to balance the back button
            Spacer(modifier = Modifier.size(40.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Name
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::setName,
                label = { Text("Nombre del presupuesto") },
                placeholder = { Text("Ej: Comida del mes") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Currency selector
            Text(
                text = "Moneda",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("USD", "VES", "USDT", "EUR", "EURI").forEach { code ->
                    FilterChip(
                        selected = uiState.currencyCode == code,
                        onClick = { viewModel.setCurrencyCode(code) },
                        label = { Text(code) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount
            OutlinedTextField(
                value = uiState.limitAmount,
                onValueChange = viewModel::setLimitAmount,
                label = { Text("Límite (${uiState.currencyCode})") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Period selector
            Text(
                text = "Período",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            Spacer(modifier = Modifier.height(4.dp))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                val periods = listOf(
                    BudgetPeriod.WEEKLY to "Semanal",
                    BudgetPeriod.BIWEEKLY to "Quincenal",
                    BudgetPeriod.MONTHLY to "Mensual",
                )
                periods.forEachIndexed { index, (period, label) ->
                    SegmentedButton(
                        selected = uiState.period == period,
                        onClick = { viewModel.setPeriod(period) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = periods.size,
                        ),
                    ) {
                        Text(label)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category selector (optional)
            Text(
                text = "Categoría (opcional)",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            Text(
                text = "Si no seleccionas categoría, aplica a todos los gastos",
                style = MaterialTheme.typography.bodySmall,
                color = t.textSecondary,
            )
            Spacer(modifier = Modifier.height(4.dp))
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

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.name.isNotBlank() &&
                    (uiState.limitAmount.toDoubleOrNull() ?: 0.0) > 0 &&
                    !uiState.isSaving,
            ) {
                Text(if (uiState.isEditMode) "Guardar cambios" else "Crear presupuesto")
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
