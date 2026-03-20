package com.schwarckdev.cerofiao.feature.settings
import com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CsvExportScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CsvExportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
    ) { uri ->
        if (uri != null) {
            viewModel.exportToUri(context, uri)
        }
    }

    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            viewModel.importFromUri(context, uri)
        }
    }

    LaunchedEffect(uiState.exportedCount) {
        val count = uiState.exportedCount
        if (count != null) {
            Toast.makeText(context, "$count transacciones exportadas", Toast.LENGTH_SHORT).show()
            viewModel.clearResult()
        }
    }

    LaunchedEffect(uiState.importedCount) {
        val count = uiState.importedCount
        if (count != null) {
            val skipped = uiState.importSkipped ?: 0
            val msg = "$count importadas" + if (skipped > 0) ", $skipped omitidas" else ""
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearResult()
        }
    }

    LaunchedEffect(uiState.error) {
        val error = uiState.error
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearResult()
        }
    }

    val t = CeroFiaoTheme.tokens

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).padding(top = 90.dp),
    ) {
        // CeroFiao top bar
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
                    Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
                }
            }
            Text(
                text = "Importar / Exportar CSV",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = t.text,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Exportar",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF8A2BE2),
            )

            Text(
                text = "Exporta tus transacciones a un archivo CSV con tasas de cambio incluidas.",
                style = MaterialTheme.typography.bodyMedium,
                color = t.textSecondary,
            )

            Text(
                text = "Rango de fechas (opcional)",
                style = MaterialTheme.typography.labelLarge,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CeroFiaoButton(
                    text = uiState.startDate?.let { DateUtils.formatDisplayDate(it) } ?: "Desde",
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier.weight(1f),
                    variant = CeroFiaoButtonVariant.Secondary,
                )

                CeroFiaoButton(
                    text = uiState.endDate?.let { DateUtils.formatDisplayDate(it) } ?: "Hasta",
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier.weight(1f),
                    variant = CeroFiaoButtonVariant.Secondary,
                )
            }

            if (uiState.startDate != null || uiState.endDate != null) {
                CeroFiaoButton(
                    text = "Limpiar filtro (exportar todo)",
                    onClick = {
                        viewModel.setStartDate(null)
                        viewModel.setEndDate(null)
                    },
                    variant = CeroFiaoButtonVariant.Text,
                )
            }

            CeroFiaoButton(
                text = if (uiState.isExporting) "" else "Exportar transacciones",
                onClick = { createFileLauncher.launch(CsvExportViewModel.defaultFileName()) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isExporting,
            )

            Text(
                text = "Columnas: fecha, tipo, monto, moneda, monto_usd, tasa_usd, fuente_tasa, categoria, cuenta, nota",
                style = MaterialTheme.typography.bodySmall,
                color = t.textMuted,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Importar",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF8A2BE2),
            )

            Text(
                text = "Importa transacciones desde un CSV con el mismo formato de exportación. Las cuentas deben existir con el mismo nombre.",
                style = MaterialTheme.typography.bodyMedium,
                color = t.textSecondary,
            )

            CeroFiaoButton(
                text = if (uiState.isImporting) "" else "Importar desde CSV",
                onClick = { openFileLauncher.launch(arrayOf("text/csv", "text/comma-separated-values", "*/*")) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isImporting,
                variant = CeroFiaoButtonVariant.Secondary,
            )
        }
    }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.startDate,
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                CeroFiaoButton(
                    text = "OK",
                    onClick = {
                        viewModel.setStartDate(datePickerState.selectedDateMillis)
                        showStartDatePicker = false
                    },
                    variant = CeroFiaoButtonVariant.Text
                )
            },
            dismissButton = {
                CeroFiaoButton(
                    text = "Cancelar",
                    onClick = { showStartDatePicker = false },
                    variant = CeroFiaoButtonVariant.Text
                )
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.endDate,
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                CeroFiaoButton(
                    text = "OK",
                    onClick = {
                        viewModel.setEndDate(datePickerState.selectedDateMillis)
                        showEndDatePicker = false
                    },
                    variant = CeroFiaoButtonVariant.Text
                )
            },
            dismissButton = {
                CeroFiaoButton(
                    text = "Cancelar",
                    onClick = { showEndDatePicker = false },
                    variant = CeroFiaoButtonVariant.Text
                )
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

