package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RefreshCw
import com.composables.icons.lucide.TrendingUp
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.RateColors
import com.schwarckdev.cerofiao.core.designsystem.theme.TransferGradient

@Composable
fun ExchangeRateScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ExchangeRateViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Standard, title = "Tasas de Cambio")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 110.dp, top = 70.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Currency Calculator
        CurrencyCalculatorSection(
            amount = uiState.calculatorAmount,
            fromCurrency = uiState.calculatorFromCurrency,
            toCurrency = uiState.calculatorToCurrency,
            result = uiState.calculatorResult,
            resultSource = uiState.calculatorResultSource,
            customRate = uiState.calculatorCustomRate,
            isCustomRateEnabled = uiState.isCustomRateEnabled,
            isParityLossWarning = uiState.isParityLossWarning,
            parityDifferenceAmount = uiState.parityDifferenceAmount,
            parityDifferenceVes = uiState.parityDifferenceVes,
            onAmountChange = viewModel::updateCalculatorAmount,
            onFromCurrencyChange = viewModel::updateCalculatorFromCurrency,
            onToCurrencyChange = viewModel::updateCalculatorToCurrency,
            onSwap = viewModel::swapCalculatorCurrencies,
            onCustomRateChange = viewModel::updateCalculatorCustomRate,
            onToggleCustomRate = viewModel::toggleCustomRate,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(24.dp))

        // Main rates container
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(38.dp),
            color = colors.Foreground,
            shadowElevation = 8.dp,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp),
            ) {
                // EUR rate cards row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ExchangeRateCard(
                        label = "€ Euro (BCV)",
                        rate = uiState.bcvEurRate?.rate,
                        color = RateColors.BcvGreen,
                        modifier = Modifier.weight(1f),
                    )
                    ExchangeRateCard(
                        label = "€ EURI",
                        rate = uiState.euriRate?.rate,
                        color = RateColors.ParallelTeal,
                        modifier = Modifier.weight(1f),
                    )
                }

                // Historical EUR card
                HistoricalRateCard(
                    title = "Histórico Euro € / Bs",
                    currentRate = uiState.bcvEurRate?.rate,
                    historicalRates = uiState.historicalEur,
                )

                // USD rate cards row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ExchangeRateCard(
                        label = "$ Dólar (BCV)",
                        rate = uiState.bcvUsdRate?.rate,
                        color = RateColors.BcvGreen,
                        modifier = Modifier.weight(1f),
                    )
                    ExchangeRateCard(
                        label = "USDT ₮",
                        rate = uiState.usdtRate?.rate,
                        color = RateColors.ParallelTeal,
                        modifier = Modifier.weight(1f),
                    )
                }

                // Historical USD card
                HistoricalRateCard(
                    title = "Histórico USD $ / Bs",
                    currentRate = uiState.bcvUsdRate?.rate,
                    historicalRates = uiState.historicalUsd,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Gradient "Actualizar tasas" button
        GradientRefreshButton(
            onClick = { viewModel.refresh() },
            isLoading = uiState.isLoading,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun ExchangeRateCard(
    label: String,
    rate: Double?,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = colors.Background,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 17.dp, vertical = 9.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape),
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = color,
                )
            }
            Text(
                text = if (rate != null) String.format("%.2f Bs", rate) else "—",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (rate != null) "+0%" else "—",
                    fontSize = 12.sp,
                    color = colors.TextSecondary,
                )
                Icon(
                    imageVector = Lucide.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = color,
                )
            }
        }
    }
}

@Composable
private fun GradientRefreshButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(TransferGradient, RoundedCornerShape(28.dp))
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                )
            } else {
                Icon(
                    imageVector = Lucide.RefreshCw,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = "Actualizar tasas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF1F1F3),
                letterSpacing = 0.2.sp,
            )
        }
    }
}
