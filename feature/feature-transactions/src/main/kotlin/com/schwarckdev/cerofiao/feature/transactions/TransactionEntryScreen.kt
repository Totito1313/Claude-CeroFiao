package com.schwarckdev.cerofiao.feature.transactions
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTopAppBar
import com.schwarckdev.cerofiao.core.ui.MoneyAmountInput
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionEntryScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionEntryViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).statusBarsPadding(),
    ) {
        // Top bar row
        CeroFiaoTopAppBar(
            title = if (uiState.isEditMode) "Editar transacción" else "Nueva transacción",
            onNavigationClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Transaction type selector
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val types = listOf(
                    TransactionType.EXPENSE to "Gasto",
                    TransactionType.INCOME to "Ingreso",
                )
                types.forEach { (type, label) ->
                    OptionChip(
                        label = label,
                        selected = uiState.transactionType == type,
                        onClick = { viewModel.setTransactionType(type) }
                    )
                }
            }

            // Premium Amount Input
            Surface(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = t.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, t.surfaceBorder)
            ) {
                MoneyAmountInput(
                    amount = uiState.amountText,
                    onAmountChange = viewModel::setAmount,
                    currency = uiState.selectedCurrencyCode ?: "USD",
                    onCurrencyClick = {
                        val currentIdx = listOf("USD", "VES", "USDT", "EUR").indexOf(uiState.selectedCurrencyCode)
                        val nextIdx = (currentIdx + 1) % 4
                        viewModel.selectCurrency(listOf("USD", "VES", "USDT", "EUR")[nextIdx])
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Currency selector
            Text(
                text = "Moneda",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("USD", "VES", "USDT", "EUR").forEach { code ->
                    OptionChip(
                        label = code,
                        selected = uiState.selectedCurrencyCode == code,
                        onClick = { viewModel.selectCurrency(code) }
                    )
                }
            }

            // Multi-currency reference card
            if (uiState.currencyEquivalents.isNotEmpty() && uiState.amount > 0) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    padding = GlassCardPadding.Small
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Equivalentes",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF8A2BE2),
                        )
                        uiState.currencyEquivalents.forEach { (currency, amount) ->
                            Text(
                                text = CurrencyFormatter.format(amount, currency),
                                style = MaterialTheme.typography.bodyMedium,
                                color = t.textSecondary,
                            )
                        }
                    }
                }
            }

            // Account selector
            Text(
                text = "Cuenta",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.accounts.forEach { account ->
                    OptionChip(
                        label = "${account.name} (${account.currencyCode})",
                        selected = account.id == uiState.selectedAccountId,
                        onClick = { viewModel.selectAccount(account.id) }
                    )
                }
            }

            // Category selector
            if (uiState.categories.isNotEmpty()) {
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.labelLarge,
                    color = t.text,
                )

                val selectedCategoryId = uiState.selectedCategoryId
                val activeParentNode = uiState.categories.find { node ->
                    node.category.id == selectedCategoryId || node.subcategories.any { it.id == selectedCategoryId }
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    uiState.categories.forEach { node ->
                        val isSelected = activeParentNode?.category?.id == node.category.id
                        OptionChip(
                            label = node.category.name,
                            selected = isSelected,
                            onClick = { viewModel.selectCategory(node.category.id) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(CeroFiaoIcons.getCategoryIconRes(node.category.iconName)),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                )
                            },
                        )
                    }
                }

                if (activeParentNode != null && activeParentNode.subcategories.isNotEmpty()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        color = t.surface,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            activeParentNode.subcategories.forEach { sub ->
                                OptionChip(
                                    label = sub.name,
                                    selected = sub.id == selectedCategoryId,
                                    onClick = { viewModel.selectCategory(sub.id) }
                                )
                            }
                        }
                    }
                }
            }

            // Note
            CeroFiaoTextField(
                value = uiState.note,
                onValueChange = viewModel::setNote,
                placeholder = "Nota (opcional)",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Goal selector
            if (uiState.goals.isNotEmpty() && !uiState.isEditMode) {
                Text(
                    text = "Vincular a Objetivo (Opcional)",
                    style = MaterialTheme.typography.labelLarge,
                    color = t.text,
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    uiState.goals.forEach { goal ->
                        OptionChip(
                            label = goal.name,
                            selected = goal.id == uiState.selectedGoalId,
                            onClick = { viewModel.selectGoal(goal.id) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(CeroFiaoIcons.getCategoryIconRes(goal.iconName ?: "Target")),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = goal.colorHex?.let { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(it)) } ?: Color(0xFF8A2BE2)
                                )
                            },
                        )
                    }
                }
            }

            // Category suggestion chip
            val suggestedCategoryId = uiState.suggestedCategoryId
            if (suggestedCategoryId != null && uiState.selectedCategoryId == null) {
                val suggestedCategory = uiState.categories
                    .flatMap { listOf(it.category) + it.subcategories }
                    .find { it.id == suggestedCategoryId }
                if (suggestedCategory != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = t.pillBg,
                        tonalElevation = 1.dp,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.applySuggestedCategory() }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                painter = painterResource(CeroFiaoIcons.getCategoryIconRes(suggestedCategory.iconName)),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = t.text,
                            )
                            Text(
                                text = "Sugerencia: ${suggestedCategory.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = t.text,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = "Aplicar",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF8A2BE2),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            // Save button
            CeroFiaoButton(
                text = "Guardar",
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.amount > 0,
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun OptionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
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
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
            )
        }
    }
}

