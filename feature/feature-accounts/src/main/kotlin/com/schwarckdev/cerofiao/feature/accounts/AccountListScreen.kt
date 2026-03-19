package com.schwarckdev.cerofiao.feature.accounts

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.ui.EmptyState
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding

@Composable
fun AccountListScreen(
    onAccountClick: (String) -> Unit,
    onAddAccount: () -> Unit,
    onTransfer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 100.dp),
    ) {
        // Header with title and subtitle
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Cuentas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = t.text,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Patrimonio y transferencias",
                fontSize = 13.sp,
                color = t.textMuted,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (uiState.accounts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                EmptyState(
                    icon = CeroFiaoIcons.Accounts,
                    title = "Sin cuentas",
                    description = "Agrega tu primera cuenta para comenzar",
                    actionLabel = "Agregar cuenta",
                    onAction = onAddAccount,
                )
            }
        } else {
            // Total balance summary card
            BalanceSummaryCard(
                totalBalance = uiState.totalBalance,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons row
            ActionButtonsRow(
                onTransfer = onTransfer,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Account cards
            uiState.accounts.forEachIndexed { index, account ->
                AccountRow(
                    account = account,
                    onClick = { onAccountClick(account.id) },
                )
                if (index < uiState.accounts.lastIndex) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add account dashed-border button
            AddAccountButton(
                onClick = onAddAccount,
            )
        }
    }
}

@Composable
private fun BalanceSummaryCard(
    totalBalance: Map<String, Double>,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        padding = GlassCardPadding.Large,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Balance Total",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = t.textSecondary,
            )

            if (totalBalance.isEmpty()) {
                Text(
                    text = "Sin datos",
                    fontSize = 15.sp,
                    color = t.textMuted,
                )
            } else {
                totalBalance.forEach { (currencyCode, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = currencyCode,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = t.textTertiary,
                        )
                        Text(
                            text = CurrencyFormatter.format(amount, currencyCode),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = t.text,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtonsRow(
    onTransfer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ActionButton(
            icon = CeroFiaoIcons.Transfer,
            label = "Transferir",
            accentColor = Color(0xFF00FF66), // green
            onClick = onTransfer,
            modifier = Modifier.weight(1f),
        )
        ActionButton(
            icon = CeroFiaoIcons.Transfer,
            label = "Cambiar",
            accentColor = Color(0xFFAA66FF), // purple
            onClick = { /* TODO: Currency exchange */ },
            modifier = Modifier.weight(1f),
        )
        ActionButton(
            icon = CeroFiaoIcons.Transfer,
            label = "Enviar",
            accentColor = Color(0xFF00CCCC), // cyan
            onClick = { /* TODO: Send */ },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
        color = accentColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp),
                tint = accentColor,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun AccountRow(
    account: Account,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        padding = GlassCardPadding.Medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Type-based emoji icon
            Text(
                text = accountTypeEmoji(account.type),
                fontSize = 28.sp,
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Name and platform
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = account.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = t.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = account.platform.displayName,
                    fontSize = 13.sp,
                    color = t.textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Balance
            Text(
                text = CurrencyFormatter.format(account.balance, account.currencyCode),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = t.text,
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Chevron
            Icon(
                imageVector = CeroFiaoIcons.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = t.textMuted,
            )
        }
    }
}

@Composable
private fun AddAccountButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(CeroFiaoShapes.CardRadius),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.5.dp,
            color = t.surfaceBorder,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = CeroFiaoIcons.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = t.textSecondary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agregar cuenta",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = t.textSecondary,
            )
        }
    }
}

private fun accountTypeEmoji(type: AccountType): String = when (type) {
    AccountType.BANK -> "\uD83C\uDFE6"
    AccountType.DIGITAL_WALLET -> "\uD83D\uDCF1"
    AccountType.CASH -> "\uD83D\uDCB5"
    AccountType.CRYPTO_EXCHANGE -> "\u20BF"
}
