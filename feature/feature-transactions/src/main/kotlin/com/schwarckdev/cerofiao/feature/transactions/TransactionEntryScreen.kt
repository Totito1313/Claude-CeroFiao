package com.schwarckdev.cerofiao.feature.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.components.CeroFiaoCurrencyToggle
import com.schwarckdev.cerofiao.core.designsystem.components.CeroFiaoPrimaryButton
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.components.forms.InputDisplayVariant
import com.schwarckdev.cerofiao.core.designsystem.components.forms.InputSize
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.feature.transactions.components.AccountSelectorRow
import com.schwarckdev.cerofiao.feature.transactions.components.CategorySelectorSection
import com.schwarckdev.cerofiao.feature.transactions.components.EntryTypeSelector

@Composable
fun TransactionEntryScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    ConfigureTopBar(
        variant = TopBarVariant.Detail,
        title = if (uiState.isEditMode) "Editar Movimiento" else "Nuevo Movimiento",
        onBackClick = onBack,
    )

    val colors = CeroFiaoDesign.colors

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .imePadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(top = 70.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Amount input
            AmountInputSection(
                amountText = uiState.amountText,
                onAmountChange = { viewModel.setAmount(it) },
                selectedCurrency = uiState.selectedCurrencyCode,
                onCurrencySelected = { viewModel.selectCurrency(it) },
                currencyEquivalents = uiState.currencyEquivalents,
                accounts = uiState.accounts,
            )

            Spacer(Modifier.height(20.dp))

            // Transaction type selector
            EntryTypeSelector(
                selectedType = uiState.transactionType,
                onTypeSelected = { viewModel.setTransactionType(it) },
            )

            Spacer(Modifier.height(24.dp))

            // Account selector
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Cuenta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(10.dp))
                AccountSelectorRow(
                    accounts = uiState.accounts,
                    selectedAccountId = uiState.selectedAccountId,
                    onAccountSelected = { viewModel.selectAccount(it) },
                )
            }

            Spacer(Modifier.height(24.dp))

            // Category selector
            CategorySelectorSection(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                suggestedCategoryId = uiState.suggestedCategoryId,
                onCategorySelected = { viewModel.selectCategory(it) },
                onSuggestionAccepted = { viewModel.applySuggestedCategory() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(24.dp))

            // Note input
            CeroFiaoTextField(
                value = uiState.note,
                onValueChange = { viewModel.setNote(it) },
                label = "Nota",
                placeholder = "Descripción del movimiento...",
                displayVariant = InputDisplayVariant.Faded,
                size = InputSize.Lg,
                singleLine = false,
                minLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }

        // Sticky save button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            colors.Background.copy(alpha = 0.9f),
                            colors.Background,
                        ),
                    )
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            CeroFiaoPrimaryButton(
                text = if (uiState.isEditMode) "Actualizar" else "Guardar",
                onClick = { viewModel.save() },
                isLoading = uiState.isSaving,
                enabled = uiState.amount > 0 && uiState.selectedAccountId != null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun AmountInputSection(
    amountText: String,
    onAmountChange: (String) -> Unit,
    selectedCurrency: String?,
    onCurrencySelected: (String) -> Unit,
    currencyEquivalents: Map<String, Double>,
    accounts: List<com.schwarckdev.cerofiao.core.model.Account>,
) {
    val colors = CeroFiaoDesign.colors

    // Derive currency options from accounts
    val currencyOptions = accounts.map { it.currencyCode }.distinct().ifEmpty {
        listOf("USD", "VES", "USDT")
    }

    // Map currency codes for CeroFiaoCurrencyToggle (it uses BS not VES)
    val toggleOptions = currencyOptions.map { if (it == "VES") "BS" else it }
    val currentToggle = if (selectedCurrency == "VES") "BS" else (selectedCurrency ?: toggleOptions.firstOrNull() ?: "USD")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "MONTO",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colors.TextSecondary,
            letterSpacing = 2.4.sp,
        )

        Spacer(Modifier.height(12.dp))

        // Large amount input
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
        ) {
            val symbol = when (selectedCurrency) {
                "USD" -> "$"
                "VES" -> "Bs"
                "USDT" -> "₮"
                "EUR" -> "€"
                else -> "$"
            }
            Text(
                text = symbol,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = colors.TextSecondary,
                letterSpacing = (-2).sp,
            )
            Spacer(Modifier.width(4.dp))
            BasicTextField(
                value = amountText,
                onValueChange = { newText ->
                    // Only allow valid decimal input
                    val filtered = newText.filter { it.isDigit() || it == '.' }
                    if (filtered.count { it == '.' } <= 1) {
                        onAmountChange(filtered)
                    }
                },
                textStyle = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextPrimary,
                    letterSpacing = (-2).sp,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                cursorBrush = SolidColor(colors.Primary),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.animateContentSize()) {
                        if (amountText.isEmpty()) {
                            Text(
                                text = "0.00",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.InactiveColor,
                                letterSpacing = (-2).sp,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }

        Spacer(Modifier.height(12.dp))

        // Currency toggle
        CeroFiaoCurrencyToggle(
            current = currentToggle,
            onCurrencyChange = { code ->
                onCurrencySelected(if (code == "BS") "VES" else code)
            },
            options = toggleOptions,
            compact = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
        )

        // Currency equivalents
        AnimatedVisibility(
            visible = currencyEquivalents.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                currencyEquivalents.entries.forEach { (code, amount) ->
                    Text(
                        text = "≈ ${CurrencyFormatter.format(amount, code)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.TextSecondary,
                    )
                }
            }
        }
    }
}
