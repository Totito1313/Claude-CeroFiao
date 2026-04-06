package com.schwarckdev.cerofiao.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.None)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(48.dp))

        // Step indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (index == uiState.currentStep) 28.dp else 8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (index <= uiState.currentStep) colors.Primary
                            else colors.TextSecondary.copy(alpha = 0.3f)
                        ),
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Animated step content
        AnimatedContent(
            targetState = uiState.currentStep,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                (slideInHorizontally { it * direction } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it * direction } + fadeOut())
            },
            modifier = Modifier.weight(1f),
            label = "onboarding_step",
        ) { step ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                when (step) {
                    0 -> WelcomeStep()
                    1 -> CurrencyStep(
                        selected = uiState.selectedCurrency,
                        onSelect = viewModel::selectCurrency,
                    )
                    2 -> RateSourceStep(
                        selected = uiState.selectedRateSource,
                        onSelect = viewModel::selectRateSource,
                    )
                    3 -> PlatformsStep(
                        selected = uiState.selectedPlatforms,
                        onToggle = viewModel::togglePlatform,
                    )
                }
            }
        }

        // Bottom buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (uiState.currentStep > 0) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .pressableFeedback(
                            onClick = { viewModel.previousStep() },
                            variant = FeedbackVariant.ScaleHighlight,
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.SurfaceVariant,
                ) {
                    Text(
                        text = "Atrás",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        color = colors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(if (uiState.currentStep > 0) 2f else 1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        BrandGradient,
                        RoundedCornerShape(16.dp),
                    )
                    .pressableFeedback(
                        onClick = {
                            if (uiState.currentStep < 3) {
                                viewModel.nextStep()
                            } else {
                                viewModel.completeOnboarding(onOnboardingComplete)
                            }
                        },
                        variant = FeedbackVariant.ScaleHighlight,
                    )
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (uiState.currentStep < 3) "Continuar" else "Comenzar",
                    color = colors.TextOnDark,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun WelcomeStep() {
    val colors = CeroFiaoDesign.colors

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(48.dp))

        Icon(
            imageVector = CeroFiaoIcons.Scan,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = colors.Primary,
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Bienvenido a CeroFiao",
            color = colors.TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Tu app de control de gastos multi-moneda para Venezuela. Configuremos tu experiencia en unos pasos.",
            color = colors.TextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun CurrencyStep(
    selected: String,
    onSelect: (String) -> Unit,
) {
    val colors = CeroFiaoDesign.colors
    val currencies = listOf(
        Triple("USD", "Dólar", "$"),
        Triple("VES", "Bolívar", "Bs"),
        Triple("USDT", "Tether", "₮"),
        Triple("EUR", "Euro", "€"),
    )

    Column {
        Text(
            text = "Moneda principal",
            color = colors.TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Selecciona la moneda en la que quieres ver tus balances.",
            color = colors.TextSecondary,
            fontSize = 15.sp,
            lineHeight = 22.sp,
        )

        Spacer(Modifier.height(24.dp))

        currencies.forEach { (code, name, symbol) ->
            val isSelected = selected == code
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .then(
                        if (isSelected) Modifier.border(
                            2.dp, colors.Primary, RoundedCornerShape(16.dp)
                        ) else Modifier
                    )
                    .pressableFeedback(
                        onClick = { onSelect(code) },
                        variant = FeedbackVariant.ScaleHighlight,
                    ),
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) colors.Primary.copy(alpha = 0.1f) else colors.CardBackground,
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) colors.Primary.copy(alpha = 0.2f)
                                else colors.SurfaceVariant
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = symbol,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) colors.Primary else colors.TextPrimary,
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = code,
                            color = colors.TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = name,
                            color = colors.TextSecondary,
                            fontSize = 13.sp,
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = CeroFiaoIcons.Check,
                            contentDescription = null,
                            tint = colors.Primary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RateSourceStep(
    selected: ExchangeRateSource,
    onSelect: (ExchangeRateSource) -> Unit,
) {
    val colors = CeroFiaoDesign.colors
    val sources = listOf(
        Triple(ExchangeRateSource.BCV, "BCV (Oficial)", "Tasa del Banco Central de Venezuela"),
        Triple(ExchangeRateSource.USDT, "USDT (Mercado)", "Tasa del dólar cripto en el mercado"),
        Triple(ExchangeRateSource.EURI, "EURI (Mercado)", "Tasa del euro en el mercado paralelo"),
    )

    Column {
        Text(
            text = "Tasa de cambio",
            color = colors.TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Elige la tasa preferida para tus conversiones.",
            color = colors.TextSecondary,
            fontSize = 15.sp,
            lineHeight = 22.sp,
        )

        Spacer(Modifier.height(24.dp))

        sources.forEach { (source, name, description) ->
            val isSelected = selected == source
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .then(
                        if (isSelected) Modifier.border(
                            2.dp, colors.Primary, RoundedCornerShape(16.dp)
                        ) else Modifier
                    )
                    .pressableFeedback(
                        onClick = { onSelect(source) },
                        variant = FeedbackVariant.ScaleHighlight,
                    ),
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) colors.Primary.copy(alpha = 0.1f) else colors.CardBackground,
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            color = colors.TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = description,
                            color = colors.TextSecondary,
                            fontSize = 13.sp,
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = CeroFiaoIcons.Check,
                            contentDescription = null,
                            tint = colors.Primary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlatformsStep(
    selected: Set<AccountPlatform>,
    onToggle: (AccountPlatform) -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    // Group platforms by type
    val banks = listOf(
        AccountPlatform.BANESCO, AccountPlatform.MERCANTIL,
        AccountPlatform.PROVINCIAL, AccountPlatform.VENEZUELA,
        AccountPlatform.BNC, AccountPlatform.BANCARIBE,
    )
    val digital = listOf(
        AccountPlatform.ZELLE, AccountPlatform.ZINLI,
        AccountPlatform.WALLY, AccountPlatform.PAYPAL,
    )
    val crypto = listOf(AccountPlatform.BINANCE)

    Column {
        Text(
            text = "Tus cuentas",
            color = colors.TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Selecciona las plataformas que usas. Podrás agregar más después.",
            color = colors.TextSecondary,
            fontSize = 15.sp,
            lineHeight = 22.sp,
        )

        Spacer(Modifier.height(24.dp))

        PlatformGroup(title = "Bancos", platforms = banks, selected = selected, onToggle = onToggle)
        Spacer(Modifier.height(16.dp))
        PlatformGroup(title = "Billeteras digitales", platforms = digital, selected = selected, onToggle = onToggle)
        Spacer(Modifier.height(16.dp))
        PlatformGroup(title = "Cripto", platforms = crypto, selected = selected, onToggle = onToggle)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlatformGroup(
    title: String,
    platforms: List<AccountPlatform>,
    selected: Set<AccountPlatform>,
    onToggle: (AccountPlatform) -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    Text(
        text = title,
        color = colors.TextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
    )

    Spacer(Modifier.height(8.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        platforms.forEach { platform ->
            val isSelected = platform in selected
            Surface(
                modifier = Modifier
                    .then(
                        if (isSelected) Modifier.border(
                            1.5.dp, colors.Primary, RoundedCornerShape(12.dp)
                        ) else Modifier
                    )
                    .pressableFeedback(
                        onClick = { onToggle(platform) },
                        variant = FeedbackVariant.ScaleHighlight,
                    ),
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) colors.Primary.copy(alpha = 0.1f) else colors.CardBackground,
            ) {
                Text(
                    text = platform.displayName,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = if (isSelected) colors.Primary else colors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}
