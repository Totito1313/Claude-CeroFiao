package com.schwarckdev.cerofiao.feature.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencsv.CSVWriter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.opencsv.CSVReader
import com.schwarckdev.cerofiao.core.domain.usecase.ExportTransactionsCsvUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.ImportTransactionsCsvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import javax.inject.Inject

data class CsvExportUiState(
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val exportedCount: Int? = null,
    val importedCount: Int? = null,
    val importSkipped: Int? = null,
    val error: String? = null,
)

@HiltViewModel
class CsvExportViewModel @Inject constructor(
    private val exportTransactionsCsvUseCase: ExportTransactionsCsvUseCase,
    private val importTransactionsCsvUseCase: ImportTransactionsCsvUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CsvExportUiState())
    val uiState: StateFlow<CsvExportUiState> = _uiState.asStateFlow()

    fun setStartDate(date: Long?) {
        _uiState.update { it.copy(startDate = date) }
    }

    fun setEndDate(date: Long?) {
        _uiState.update { it.copy(endDate = date) }
    }

    fun exportToUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, error = null, exportedCount = null) }
            try {
                val rows = exportTransactionsCsvUseCase(
                    startDate = _uiState.value.startDate,
                    endDate = _uiState.value.endDate,
                )

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val writer = CSVWriter(OutputStreamWriter(outputStream, Charsets.UTF_8))
                    writer.use { csv ->
                        csv.writeNext(CSV_HEADERS)
                        rows.forEach { row ->
                            csv.writeNext(
                                arrayOf(
                                    row.fecha,
                                    row.tipo,
                                    row.monto.toString(),
                                    row.moneda,
                                    row.montoUsd.toString(),
                                    row.tasaUsd.toString(),
                                    row.fuenteTasa,
                                    row.categoria,
                                    row.cuenta,
                                    row.nota,
                                ),
                            )
                        }
                    }
                }

                _uiState.update { it.copy(isExporting = false, exportedCount = rows.size) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isExporting = false, error = e.message ?: "Error al exportar") }
            }
        }
    }

    fun importFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImporting = true, error = null, importedCount = null, importSkipped = null) }
            try {
                val rows = mutableListOf<Array<String>>()
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = CSVReader(InputStreamReader(inputStream, Charsets.UTF_8))
                    reader.use { csv ->
                        val allRows = csv.readAll()
                        // Skip header row if it matches our format
                        val dataRows = if (allRows.isNotEmpty() && allRows[0].firstOrNull()?.lowercase() == "fecha") {
                            allRows.drop(1)
                        } else {
                            allRows
                        }
                        rows.addAll(dataRows)
                    }
                }

                val result = importTransactionsCsvUseCase(rows)
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        importedCount = result.imported,
                        importSkipped = result.skipped,
                        error = if (result.errors.isNotEmpty()) result.errors.take(3).joinToString("\n") else null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isImporting = false, error = e.message ?: "Error al importar") }
            }
        }
    }

    fun clearResult() {
        _uiState.update { it.copy(exportedCount = null, importedCount = null, importSkipped = null, error = null) }
    }

    companion object {
        private val CSV_HEADERS = arrayOf(
            "fecha", "tipo", "monto", "moneda", "monto_usd",
            "tasa_usd", "fuente_tasa", "categoria", "cuenta", "nota",
        )

        fun defaultFileName(): String {
            val date = DateUtils.todayIsoDate()
            return "cerofiao_export_$date.csv"
        }
    }
}
