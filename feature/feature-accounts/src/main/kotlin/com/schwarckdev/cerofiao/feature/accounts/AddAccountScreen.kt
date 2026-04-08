package com.schwarckdev.cerofiao.feature.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoToggleButtonGroup
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ToggleButtonItem
import com.schwarckdev.cerofiao.core.designsystem.components.controls.CeroFiaoSwitch
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoSelect
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.AccountPlatform

@Composable
fun AddAccountScreen(
    onBack: () -> Unit,
    onAccountCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddAccountViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Detail, title = "Nueva Cuenta", onBackClick = onBack)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors
    val spacing = CeroFiaoDesign.spacing
    val radius = CeroFiaoDesign.radius

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onAccountCreated()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                start = spacing.screenHorizontal,
                end = spacing.screenHorizontal,
                top = 70.dp,
                bottom = 110.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(spacing.xl),
    ) {
        // ── Form fields inside a card ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(radius.xxl),
            color = colors.CardBackground,
        ) {
            Column(
                modifier = Modifier.padding(spacing.xxl),
                verticalArrangement = Arrangement.spacedBy(spacing.xl),
            ) {
                // Platform selection
                CeroFiaoSelect(
                    selected = uiState.platform.takeIf { it != AccountPlatform.NONE },
                    onSelectedChange = viewModel::setPlatform,
                    options = AccountPlatform.entries.toList(),
                    label = "Plataforma",
                    placeholder = "Seleccionar plataforma...",
                    displayText = { it.displayName },
                    modifier = Modifier.fillMaxWidth(),
                )

                // Name
                CeroFiaoTextField(
                    value = uiState.name,
                    onValueChange = viewModel::setName,
                    label = "Nombre",
                    placeholder = "Ej: Banesco, Efectivo",
                    modifier = Modifier.fillMaxWidth(),
                )

                // Currency selection
                Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    Text(
                        text = "Moneda",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.TextPrimary,
                    )
                    CeroFiaoToggleButtonGroup(
                        items = listOf("USD", "VES", "USDT", "EUR").map {
                            ToggleButtonItem(key = it, text = it)
                        },
                        selectedKey = uiState.currencyCode,
                        onSelect = viewModel::setCurrency,
                    )
                }

                // Initial balance
                CeroFiaoTextField(
                    value = uiState.initialBalance,
                    onValueChange = viewModel::setInitialBalance,
                    label = "Saldo inicial",
                    placeholder = "0.00",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // ── Include in total toggle ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(radius.xxl),
            color = colors.CardBackground,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.xxl, vertical = spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Incluir en balance total",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = colors.TextPrimary,
                    )
                    Text(
                        "Suma al balance global",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextSecondary,
                    )
                }
                CeroFiaoSwitch(
                    checked = uiState.includeInTotal,
                    onCheckedChange = { viewModel.setIncludeInTotal(it) },
                )
            }
        }

        Spacer(Modifier.height(spacing.sm))

        // ── Save button ──
        val isEnabled = uiState.name.isNotBlank()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(radius.lg))
                .background(
                    if (isEnabled) BrandGradient
                    else Brush.horizontalGradient(listOf(colors.SurfaceVariant, colors.SurfaceVariant)),
                    RoundedCornerShape(radius.lg),
                )
                .then(
                    if (isEnabled) Modifier.pressableFeedback(
                        onClick = { viewModel.save() },
                        variant = FeedbackVariant.ScaleHighlight,
                    ) else Modifier,
                )
                .padding(vertical = spacing.lg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (uiState.isSaving) "Guardando..." else "Crear cuenta",
                color = if (isEnabled) colors.TextOnDark else colors.TextSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
