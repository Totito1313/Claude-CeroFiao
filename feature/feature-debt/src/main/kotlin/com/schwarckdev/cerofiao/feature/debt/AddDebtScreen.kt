package com.schwarckdev.cerofiao.feature.debt
import androidx.compose.foundation.layout.statusBarsPadding

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
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
import com.schwarckdev.cerofiao.core.model.DebtType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddDebtScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddDebtViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).statusBarsPadding(),
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
                text = if (uiState.isEditMode) "Editar deuda" else "Nueva deuda",
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

            // Debt type selector
            Text(
                text = "Tipo de deuda",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val types = listOf(
                    DebtType.THEY_OWE to "Me deben",
                    DebtType.I_OWE to "Debo",
                )
                types.forEach { (type, label) ->
                    OptionChip(
                        label = label,
                        selected = uiState.debtType == type,
                        onClick = { viewModel.setDebtType(type) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Person name
            CeroFiaoTextField(
                value = uiState.personName,
                onValueChange = viewModel::setPersonName,
                label = "Nombre de la persona",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount
            CeroFiaoTextField(
                value = uiState.amount,
                onValueChange = viewModel::setAmount,
                label = "Monto",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
                val currencies = listOf("USD", "VES", "USDT", "EUR", "EURI")
                currencies.forEach { code ->
                    OptionChip(
                        label = code,
                        selected = uiState.currencyCode == code,
                        onClick = { viewModel.setCurrencyCode(code) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Note
            CeroFiaoTextField(
                value = uiState.note,
                onValueChange = viewModel::setNote,
                label = "Nota (opcional)",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Due date
            CeroFiaoTextField(
                value = uiState.dueDate,
                onValueChange = viewModel::setDueDate,
                label = "Fecha de vencimiento (opcional)",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            CeroFiaoButton(
                text = if (uiState.isEditMode) "Guardar cambios" else "Registrar deuda",
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.personName.isNotBlank() &&
                    (uiState.amount.toDoubleOrNull() ?: 0.0) > 0 &&
                    !uiState.isSaving,
            )

            Spacer(modifier = Modifier.height(100.dp))
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
    val t = CeroFiaoTheme.tokens
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
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

