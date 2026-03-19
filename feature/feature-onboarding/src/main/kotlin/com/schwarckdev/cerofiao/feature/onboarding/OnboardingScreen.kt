package com.schwarckdev.cerofiao.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import com.schwarckdev.cerofiao.core.ui.CurrencyChip
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg),
    ) {
        // Animated step content
        AnimatedContent(
            targetState = uiState.currentStep,
            label = "onboarding_step",
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { it } + fadeIn())
                        .togetherWith(slideOutHorizontally { -it } + fadeOut())
                } else {
                    (slideInHorizontally { -it } + fadeIn())
                        .togetherWith(slideOutHorizontally { it } + fadeOut())
                }
            },
            modifier = Modifier.weight(1f),
        ) { step ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
            ) {
                when (step) {
                    0 -> WelcomeStep()
                    1 -> CurrencyStep(
                        selectedCurrency = uiState.selectedCurrency,
                        onCurrencySelected = viewModel::selectCurrency,
                    )
                    2 -> RateSourceStep(
                        selectedSource = uiState.selectedRateSource,
                        onSourceSelected = viewModel::selectRateSource,
                    )
                    3 -> AccountsStep(
                        selectedPlatforms = uiState.selectedPlatforms,
                        onTogglePlatform = viewModel::togglePlatform,
                    )
                }
            }
        }

        // Bottom bar
        OnboardingBottomBar(
            currentStep = uiState.currentStep,
            isLoading = uiState.isLoading,
            onBack = viewModel::previousStep,
            onNext = viewModel::nextStep,
            onComplete = { viewModel.completeOnboarding(onOnboardingComplete) },
        )
    }
}

@Composable
private fun WelcomeStep(
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Icon(
            imageVector = CeroFiaoIcons.Cash,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = Color(0xFF8A2BE2),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "CeroFiao",
            style = MaterialTheme.typography.displayMedium,
            color = Color(0xFF8A2BE2),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tu dinero, todas tus monedas, un solo lugar",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = t.textSecondary,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CurrencyStep(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val currencies = listOf("USD", "VES", "USDT", "EUR", "EURI")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "\u00BFEn qu\u00E9 moneda quieres ver tus totales?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = t.text,
        )

        Spacer(modifier = Modifier.height(40.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            currencies.forEach { currency ->
                CurrencyChip(
                    label = currency,
                    isSelected = currency == selectedCurrency,
                    onClick = { onCurrencySelected(currency) },
                )
            }
        }
    }
}

@Composable
private fun RateSourceStep(
    selectedSource: ExchangeRateSource,
    onSourceSelected: (ExchangeRateSource) -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "\u00BFQu\u00E9 tasa de cambio prefieres?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = t.text,
        )

        Spacer(modifier = Modifier.height(40.dp))

        RateSourceCard(
            title = "BCV",
            description = "Tasa oficial del BCV",
            isSelected = selectedSource == ExchangeRateSource.BCV,
            onClick = { onSourceSelected(ExchangeRateSource.BCV) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        RateSourceCard(
            title = "USDT",
            description = "Tasa del mercado (USD)",
            isSelected = selectedSource == ExchangeRateSource.USDT,
            onClick = { onSourceSelected(ExchangeRateSource.USDT) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        RateSourceCard(
            title = "EURI",
            description = "Tasa del mercado (EUR)",
            isSelected = selectedSource == ExchangeRateSource.EURI,
            onClick = { onSourceSelected(ExchangeRateSource.EURI) },
        )
    }
}

@Composable
private fun RateSourceCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0x148A2BE2) else t.pillBg,
        border = BorderStroke(
            1.dp,
            if (isSelected) Color(0x268A2BE2) else Color.Transparent
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color(0xFF8A2BE2) else t.text,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) Color(0xFF8A2BE2).copy(alpha = 0.8f) else t.textSecondary,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AccountsStep(
    selectedPlatforms: Set<AccountPlatform>,
    onTogglePlatform: (AccountPlatform) -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val platforms = AccountPlatform.entries.filter { it != AccountPlatform.NONE }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "\u00BFQu\u00E9 cuentas usas?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = t.text,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Podr\u00E1s agregar m\u00E1s despu\u00E9s",
            style = MaterialTheme.typography.bodyMedium,
            color = t.textSecondary,
        )

        Spacer(modifier = Modifier.height(32.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            platforms.forEach { platform ->
                OptionChip(
                    label = platform.displayName,
                    selected = platform in selectedPlatforms,
                    onClick = { onTogglePlatform(platform) }
                )
            }
        }
    }
}

@Composable
private fun OnboardingBottomBar(
    currentStep: Int,
    isLoading: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val totalSteps = 4

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(t.bg)
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        LinearProgressIndicator(
            progress = { (currentStep + 1).toFloat() / totalSteps },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF8A2BE2),
            trackColor = t.surfaceBorder,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (currentStep > 0) {
                CeroFiaoButton(
                    text = "Atrás",
                    onClick = onBack,
                    variant = CeroFiaoButtonVariant.Secondary,
                )
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            if (currentStep < totalSteps - 1) {
                CeroFiaoButton(
                    text = if (currentStep == 0) "Comenzar" else "Siguiente",
                    onClick = onNext,
                    enabled = !isLoading,
                )
            } else {
                CeroFiaoButton(
                    text = if (isLoading) "" else "Finalizar",
                    onClick = onComplete,
                    enabled = !isLoading,
                )
            }
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
            .clip(RoundedCornerShape(CeroFiaoShapes.ChipRadius))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CeroFiaoShapes.ChipRadius),
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
