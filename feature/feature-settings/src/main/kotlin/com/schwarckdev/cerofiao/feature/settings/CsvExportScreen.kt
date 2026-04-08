package com.schwarckdev.cerofiao.feature.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.components.CeroFiaoPrimaryButton
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonVariant
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

@Composable
fun CsvExportScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CsvExportViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Detail, title = "Exportar CSV", onBackClick = onBack)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.exportToUri(context, uri)
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.importFromUri(context, uri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 80.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Export section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.Foreground,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Exportar transacciones",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary,
                )
                Text(
                    text = "Exporta todas tus transacciones en formato CSV para respaldo o analisis externo",
                    fontSize = 14.sp,
                    color = colors.TextSecondary,
                )

                CeroFiaoPrimaryButton(
                    text = if (uiState.isExporting) "Exportando..." else "Exportar CSV",
                    onClick = { exportLauncher.launch(CsvExportViewModel.defaultFileName()) },
                    enabled = !uiState.isExporting,
                    modifier = Modifier.fillMaxWidth(),
                )

                val exportedCount = uiState.exportedCount
                if (exportedCount != null) {
                    Text(
                        text = "$exportedCount transacciones exportadas",
                        fontSize = 14.sp,
                        color = colors.Success,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        // Import section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.Foreground,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Importar transacciones",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary,
                )
                Text(
                    text = "Importa transacciones desde un archivo CSV previamente exportado",
                    fontSize = 14.sp,
                    color = colors.TextSecondary,
                )

                CeroFiaoPrimaryButton(
                    text = if (uiState.isImporting) "Importando..." else "Importar CSV",
                    onClick = { importLauncher.launch(arrayOf("text/csv", "text/comma-separated-values", "*/*")) },
                    enabled = !uiState.isImporting,
                    modifier = Modifier.fillMaxWidth(),
                )

                val importedCount = uiState.importedCount
                if (importedCount != null) {
                    Text(
                        text = "$importedCount importadas, ${uiState.importSkipped ?: 0} omitidas",
                        fontSize = 14.sp,
                        color = colors.Success,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        // Error display
        val error = uiState.error
        if (error != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = colors.DangerSoft,
            ) {
                Text(
                    text = error,
                    fontSize = 14.sp,
                    color = colors.Error,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}
