package com.schwarckdev.cerofiao.feature.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonGroupItem
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonSize
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButtonGroup
import com.schwarckdev.cerofiao.core.designsystem.components.CeroFiaoPrimaryButton
import com.schwarckdev.cerofiao.core.designsystem.components.data_display.CeroFiaoChip
import com.schwarckdev.cerofiao.core.designsystem.components.data_display.ChipVariant
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.BudgetPeriod

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddBudgetScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddBudgetViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors

    ConfigureTopBar(
        variant = TopBarVariant.Detail,
        title = if (uiState.isEditMode) "Editar presupuesto" else "Nuevo presupuesto",
        onBackClick = onBack,
    )

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 80.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Name
        CeroFiaoTextField(
            value = uiState.name,
            onValueChange = viewModel::setName,
            label = "Nombre",
            placeholder = "Ej: Comida del mes",
            modifier = Modifier.fillMaxWidth(),
        )

        // Amount
        CeroFiaoTextField(
            value = uiState.limitAmount,
            onValueChange = viewModel::setLimitAmount,
            label = "Monto limite",
            placeholder = "0.00",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )

        // Currency selector
        CeroFiaoButtonGroup(
            items = listOf("USD", "VES", "USDT", "EUR").map { code ->
                ButtonGroupItem(
                    key = code,
                    text = code,
                    isActive = uiState.currencyCode == code,
                    onClick = { viewModel.setCurrencyCode(code) },
                )
            },
            size = ButtonSize.Medium,
            modifier = Modifier.fillMaxWidth(),
        )

        // Period selector
        CeroFiaoButtonGroup(
            items = listOf(
                ButtonGroupItem(
                    key = "weekly",
                    text = "Semanal",
                    isActive = uiState.period == BudgetPeriod.WEEKLY,
                    onClick = { viewModel.setPeriod(BudgetPeriod.WEEKLY) },
                ),
                ButtonGroupItem(
                    key = "biweekly",
                    text = "Quincenal",
                    isActive = uiState.period == BudgetPeriod.BIWEEKLY,
                    onClick = { viewModel.setPeriod(BudgetPeriod.BIWEEKLY) },
                ),
                ButtonGroupItem(
                    key = "monthly",
                    text = "Mensual",
                    isActive = uiState.period == BudgetPeriod.MONTHLY,
                    onClick = { viewModel.setPeriod(BudgetPeriod.MONTHLY) },
                ),
            ),
            size = ButtonSize.Medium,
            modifier = Modifier.fillMaxWidth(),
        )

        // Category selection (chips)
        if (uiState.categories.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.categories.forEach { category ->
                    val isSelected = uiState.selectedCategoryId == category.id
                    CeroFiaoChip(
                        label = category.name,
                        variant = if (isSelected) ChipVariant.Filled else ChipVariant.Soft,
                        color = if (isSelected) colors.Primary else colors.TextSecondary,
                        onClick = { viewModel.selectCategory(category.id) },
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Save button
        CeroFiaoPrimaryButton(
            text = if (uiState.isEditMode) "Guardar cambios" else "Crear presupuesto",
            onClick = viewModel::save,
            enabled = uiState.name.isNotBlank() &&
                (uiState.limitAmount.toDoubleOrNull() ?: 0.0) > 0 &&
                !uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
