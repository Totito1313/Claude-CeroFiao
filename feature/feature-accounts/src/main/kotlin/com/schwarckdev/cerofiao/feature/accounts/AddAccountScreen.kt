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

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onAccountCreated()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 70.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Platform selection — CeroFiaoSelect dropdown
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

        // Currency selection — CeroFiaoToggleButtonGroup
        Column {
            Text(
                text = "Moneda",
                color = colors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(8.dp))
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

        // Include in total toggle — CeroFiaoSwitch
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Incluir en balance total",
                    color = colors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    "Suma al balance global",
                    color = colors.TextSecondary,
                    fontSize = 12.sp,
                )
            }
            CeroFiaoSwitch(
                checked = uiState.includeInTotal,
                onCheckedChange = { viewModel.setIncludeInTotal(it) },
            )
        }

        // Save button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (uiState.name.isNotBlank()) BrandGradient
                    else Brush.horizontalGradient(listOf(colors.SurfaceVariant, colors.SurfaceVariant)),
                    RoundedCornerShape(16.dp),
                )
                .then(
                    if (uiState.name.isNotBlank()) Modifier.pressableFeedback(
                        onClick = { viewModel.save() },
                        variant = FeedbackVariant.ScaleHighlight,
                    ) else Modifier
                )
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (uiState.isSaving) "Guardando..." else "Crear cuenta",
                color = if (uiState.name.isNotBlank()) colors.TextOnDark else colors.TextSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
