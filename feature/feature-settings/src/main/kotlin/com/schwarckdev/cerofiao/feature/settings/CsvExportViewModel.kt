package com.schwarckdev.cerofiao.feature.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencsv.CSVWriter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.usecase.ExportTransactionsCsvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import javax.inject.Inject

data class CsvExportUiState(
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isExporting: Boolean = false,
    val exportedCount: Int? = null,
    val error: String? = null,
)

@HiltViewModel
class CsvExportViewModel @Inject constructor(
    private val exportTransactionsCsvUseCase: ExportTransactionsCsvUseCase,
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

    fun clearResult() {
        _uiState.update { it.copy(exportedCount = null, error = null) }
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
