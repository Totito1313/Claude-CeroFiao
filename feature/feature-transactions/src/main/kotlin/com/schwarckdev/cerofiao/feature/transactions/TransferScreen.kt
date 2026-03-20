package com.schwarckdev.cerofiao.feature.transactions
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransferScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel = hiltViewModel(),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(shape = CircleShape, color = t.iconBg) {
                IconButton(onClick = onBack) {
                    Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
                }
            }
            Text(
                text = "Transferencia",
                style = MaterialTheme.typography.titleMedium,
                color = t.text,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // From account
            Text(
                text = "Desde",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.accounts.forEach { account ->
                    AccountChip(
                        accountName = account.name,
                        currency = account.currencyCode,
                        selected = account.id == uiState.fromAccountId,
                        onClick = { viewModel.selectFromAccount(account.id) }
                    )
                }
            }

            // To account
            Text(
                text = "Hacia",
                style = MaterialTheme.typography.labelLarge,
                color = t.text,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.accounts
                    .filter { it.id != uiState.fromAccountId }
                    .forEach { account ->
                        AccountChip(
                            accountName = account.name,
                            currency = account.currencyCode,
                            selected = account.id == uiState.toAccountId,
                            onClick = { viewModel.selectToAccount(account.id) }
                        )
                    }
            }

            // Amount
            CeroFiaoTextField(
                value = uiState.amount,
                onValueChange = viewModel::setAmount,
                label = "Monto a enviar (${uiState.fromAccount?.currencyCode ?: ""})",
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            // Received amount (cross-currency)
            if (uiState.isCrossCurrency) {
                CeroFiaoTextField(
                    value = uiState.receivedAmount,
                    onValueChange = viewModel::setReceivedAmount,
                    label = "Monto recibido (${uiState.toAccount?.currencyCode ?: ""})",
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Commission
            CeroFiaoTextField(
                value = uiState.commissionPercent,
                onValueChange = viewModel::setCommissionPercent,
                label = "Comisión % (opcional)",
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            // Note
            CeroFiaoTextField(
                value = uiState.note,
                onValueChange = viewModel::setNote,
                label = "Nota (opcional)",
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))

            // Transfer button
            CeroFiaoButton(
                text = "Transferir",
                onClick = viewModel::transfer,
                enabled = uiState.isValid && !uiState.isSaving,
                isLoading = uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp),
            )
        }
    }
}

@Composable
private fun AccountChip(
    accountName: String,
    currency: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val t = CeroFiaoTheme.tokens
    val bgColor = if (selected) Color(0x1400FF66) else t.pillBg
    val borderColor = if (selected) Color(0x2600FF66) else Color.Transparent
    val textColor = if (selected) Color(0xFF00FF66) else t.textSecondary

    Surface(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(CeroFiaoShapes.ChipRadius))
            .clickable(onClick = onClick),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(CeroFiaoShapes.ChipRadius),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        androidx.compose.material3.Text(
            text = "$accountName ($currency)",
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

