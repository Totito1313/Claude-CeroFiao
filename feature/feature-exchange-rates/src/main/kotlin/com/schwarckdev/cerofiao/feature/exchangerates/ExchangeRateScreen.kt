package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.ui.ChartData
import com.schwarckdev.cerofiao.core.ui.ChartLine
import com.schwarckdev.cerofiao.core.ui.LineChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ExchangeRateViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                        CeroFiaoIcons.Back,
                        contentDescription = "Volver",
                        tint = t.text,
                    )
                }
            }
            Text(
                text = "Tasas de cambio",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = t.text,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            // USD section
            Text(
                text = "Dólar (USD)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = t.text,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.bcvUsdRate?.let { rate ->
                    RateCard(
                        title = "BCV",
                        subtitle = "Oficial",
                        currencyLabel = "1 USD",
                        rate = rate,
                        modifier = Modifier.weight(1f),
                    )
                }
                uiState.usdtRate?.let { rate ->
                    RateCard(
                        title = "USDT",
                        subtitle = "Mercado",
                        currencyLabel = "1 USD",
                        rate = rate,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // EUR section
            Text(
                text = "Euro (EUR)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = t.text,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.bcvEurRate?.let { rate ->
                    RateCard(
                        title = "BCV",
                        subtitle = "Oficial",
                        currencyLabel = "1 EUR",
                        rate = rate,
                        modifier = Modifier.weight(1f),
                    )
                }
                uiState.euriRate?.let { rate ->
                    RateCard(
                        title = "EURI",
                        subtitle = "Mercado",
                        currencyLabel = "1 EUR",
                        rate = rate,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            val hasNoRates = uiState.bcvUsdRate == null && uiState.usdtRate == null &&
                uiState.bcvEurRate == null && uiState.euriRate == null && !uiState.isLoading

            if (hasNoRates) {
                Text(
                    text = "No hay tasas disponibles. Verifica tu conexión.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = t.textSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Historical charts
            val usdChartData = remember(uiState.historicalUsd) {
                buildChartData(uiState.historicalUsd, "USD")
            }
            if (usdChartData.lines.any { it.points.isNotEmpty() }) {
                LineChart(
                    data = usdChartData,
                    title = "Histórico USD / Bs",
                )
            }

            val eurChartData = remember(uiState.historicalEur) {
                buildChartData(uiState.historicalEur, "EUR")
            }
            if (eurChartData.lines.any { it.points.isNotEmpty() }) {
                LineChart(
                    data = eurChartData,
                    title = "Histórico EUR / Bs",
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            CeroFiaoButton(
                text = "Actualizar tasas",
                onClick = viewModel::refresh,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

private fun buildChartData(rates: List<ExchangeRate>, currency: String): ChartData {
    if (rates.isEmpty()) return ChartData(lines = emptyList())

    val officialLabel = "BCV"
    val marketLabel = if (currency == "EUR") "EURI" else "USDT"

    val officialRates = rates.filter { it.source == ExchangeRateSource.BCV }
        .sortedBy { it.date }
    val marketRates = rates.filter {
        it.source == ExchangeRateSource.USDT || it.source == ExchangeRateSource.EURI
    }.sortedBy { it.date }

    // Take last 90 data points to keep chart readable
    val officialPoints = officialRates.takeLast(90).map { it.rate }
    val marketPoints = marketRates.takeLast(90).map { it.rate }

    val allDates = (officialRates + marketRates).map { it.date }.distinct().sorted()
    val dateLabels = if (allDates.size >= 2) {
        listOf(allDates.takeLast(90).first(), allDates.last())
    } else {
        emptyList()
    }

    val lines = mutableListOf<ChartLine>()
    if (officialPoints.isNotEmpty()) {
        lines.add(ChartLine(label = officialLabel, points = officialPoints, color = ChartColors.Official))
    }
    if (marketPoints.isNotEmpty()) {
        lines.add(ChartLine(label = marketLabel, points = marketPoints, color = ChartColors.Market))
    }

    return ChartData(lines = lines, dateLabels = dateLabels)
}

private object ChartColors {
    val Official = androidx.compose.ui.graphics.Color(0xFF4CAF50) // Green
    val Market = androidx.compose.ui.graphics.Color(0xFFFF9800) // Orange
}

@Composable
private fun RateCard(
    title: String,
    subtitle: String,
    currencyLabel: String,
    rate: ExchangeRate,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = t.surface,
        tonalElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF8A2BE2),
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = t.textSecondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = CurrencyFormatter.format(rate.rate, "VES"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = t.text,
            )
            Text(
                text = "$currencyLabel = Bs",
                style = MaterialTheme.typography.bodySmall,
                color = t.textSecondary,
            )
        }
    }
}
