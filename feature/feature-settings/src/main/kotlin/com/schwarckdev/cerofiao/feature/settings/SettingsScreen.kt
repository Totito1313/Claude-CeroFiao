package com.schwarckdev.cerofiao.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToCategories: () -> Unit = {},
    onNavigateToExchangeRates: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToDebts: () -> Unit = {},
    onNavigateToCsvExport: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Ajustes") })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            // Theme section
            SectionHeader("Apariencia")

            val themeModes = listOf(
                ThemeMode.SYSTEM to "Sistema",
                ThemeMode.LIGHT to "Claro",
                ThemeMode.DARK to "Oscuro",
            )
            themeModes.forEach { (mode, label) ->
                RadioRow(
                    label = label,
                    selected = prefs.themeMode == mode,
                    onClick = { viewModel.setThemeMode(mode) },
                )
            }

            SwitchRow(
                label = "Color dinámico (Material You)",
                checked = prefs.useDynamicColor,
                onCheckedChange = viewModel::setDynamicColor,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Currency section
            SectionHeader("Moneda de referencia")

            val currencies = listOf("USD", "VES", "USDT", "EUR", "EURI")
            currencies.forEach { code ->
                RadioRow(
                    label = code,
                    selected = prefs.displayCurrencyCode == code,
                    onClick = { viewModel.setDisplayCurrency(code) },
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Rate source section
            SectionHeader("Fuente de tasa de cambio")

            val sources = listOf(
                ExchangeRateSource.USDT to "USDT (Mercado)",
                ExchangeRateSource.BCV to "BCV (Oficial)",
                ExchangeRateSource.EURI to "EURI (Mercado EUR)",
            )
            sources.forEach { (source, label) ->
                RadioRow(
                    label = label,
                    selected = prefs.preferredRateSource == source,
                    onClick = { viewModel.setPreferredRateSource(source) },
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Navigation links
            SectionHeader("Gestión")

            NavigationRow(
                icon = CeroFiaoIcons.ExchangeRate,
                label = "Tasas de cambio",
                onClick = onNavigateToExchangeRates,
            )

            NavigationRow(
                icon = CeroFiaoIcons.Transactions,
                label = "Categorías",
                onClick = onNavigateToCategories,
            )

            NavigationRow(
                icon = CeroFiaoIcons.Budget,
                label = "Presupuestos",
                onClick = onNavigateToBudgets,
            )

            NavigationRow(
                icon = CeroFiaoIcons.Debt,
                label = "Deudas",
                onClick = onNavigateToDebts,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Data section
            SectionHeader("Datos")

            NavigationRow(
                icon = Icons.Default.Share,
                label = "Exportar CSV",
                onClick = onNavigateToCsvExport,
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun RadioRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun NavigationRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
