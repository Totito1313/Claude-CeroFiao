package com.schwarckdev.cerofiao.feature.goals
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
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
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditGoalScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AddEditGoalViewModel = hiltViewModel()
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
            .background(t.bg).statusBarsPadding()
    ) {
        // Top Bar
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
                text = if (uiState.isEditMode) "Editar Objetivo" else "Nuevo Objetivo",
                style = MaterialTheme.typography.titleLarge,
                color = t.text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CeroFiaoTextField(
                value = uiState.name,
                onValueChange = viewModel::setName,
                label = "Nombre del objetivo",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            CeroFiaoTextField(
                value = uiState.amountText,
                onValueChange = viewModel::setAmount,
                label = "Monto meta",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
            )

            Text(text = "Moneda", style = MaterialTheme.typography.labelLarge, color = t.text)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("USD", "VES", "USDT", "EUR").forEach { code ->
                    OptionChip(
                        label = code,
                        selected = uiState.currencyCode == code,
                        onClick = { viewModel.setCurrency(code) }
                    )
                }
            }

            Text(text = "Fecha límite (Opcional)", style = MaterialTheme.typography.labelLarge, color = t.text)
            CeroFiaoButton(
                text = uiState.deadline?.let { DateUtils.formatDisplayDate(it) } ?: "Sin fecha límite",
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                variant = CeroFiaoButtonVariant.Secondary,
            )

            Spacer(modifier = Modifier.weight(1f))

            CeroFiaoButton(
                text = if (uiState.isEditMode) "Guardar cambios" else "Crear objetivo",
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.name.isNotBlank() && uiState.amountText.isNotBlank(),
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.deadline ?: DateUtils.now(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                CeroFiaoButton(
                    text = "OK",
                    onClick = {
                        datePickerState.selectedDateMillis?.let { viewModel.setDeadline(it) }
                        showDatePicker = false
                    },
                    variant = CeroFiaoButtonVariant.Text
                )
            },
            dismissButton = {
                CeroFiaoButton(
                    text = "Quitar fecha",
                    onClick = { viewModel.setDeadline(null); showDatePicker = false },
                    variant = CeroFiaoButtonVariant.Text
                )
            },
        ) {
            DatePicker(state = datePickerState)
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

