package com.schwarckdev.cerofiao.feature.debt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.DebtType

@Composable
fun AddDebtScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddDebtViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors

    ConfigureTopBar(
        variant = TopBarVariant.Detail,
        title = if (uiState.isEditMode) "Editar deuda" else "Nueva deuda",
        onBackClick = onBack,
    )

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 80.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Debt type selector
        CeroFiaoButtonGroup(
            items = listOf(
                ButtonGroupItem(
                    key = "they_owe",
                    text = "Me deben",
                    isActive = uiState.debtType == DebtType.THEY_OWE,
                    onClick = { viewModel.setDebtType(DebtType.THEY_OWE) },
                ),
                ButtonGroupItem(
                    key = "i_owe",
                    text = "Yo debo",
                    isActive = uiState.debtType == DebtType.I_OWE,
                    onClick = { viewModel.setDebtType(DebtType.I_OWE) },
                ),
            ),
            size = ButtonSize.Medium,
            modifier = Modifier.fillMaxWidth(),
        )

        // Person name
        CeroFiaoTextField(
            value = uiState.personName,
            onValueChange = viewModel::setPersonName,
            label = "Persona",
            placeholder = "Nombre de la persona",
            modifier = Modifier.fillMaxWidth(),
        )

        // Amount
        CeroFiaoTextField(
            value = uiState.amount,
            onValueChange = viewModel::setAmount,
            label = "Monto",
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

        // Note
        CeroFiaoTextField(
            value = uiState.note,
            onValueChange = viewModel::setNote,
            label = "Nota (opcional)",
            placeholder = "Motivo de la deuda",
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        // Save button
        CeroFiaoPrimaryButton(
            text = if (uiState.isEditMode) "Guardar cambios" else "Crear deuda",
            onClick = viewModel::save,
            enabled = uiState.personName.isNotBlank() &&
                (uiState.amount.toDoubleOrNull() ?: 0.0) > 0 &&
                !uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
