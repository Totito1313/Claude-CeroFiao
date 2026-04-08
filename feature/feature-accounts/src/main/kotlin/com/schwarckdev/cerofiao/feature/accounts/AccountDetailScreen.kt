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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Receipt
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonVariant
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButton
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCard
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.AccountBadgeColors
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.ui.EmptyState

@Composable
fun AccountDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val account = uiState.account
    val colors = CeroFiaoDesign.colors
    val spacing = CeroFiaoDesign.spacing
    val radius = CeroFiaoDesign.radius

    ConfigureTopBar(
        variant = TopBarVariant.Detail,
        title = account?.name ?: "Detalle de Cuenta",
        onBackClick = onBack,
    )

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background)
            .statusBarsPadding()
            .padding(top = 70.dp),
    ) {
        if (account == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando...", color = colors.TextSecondary)
            }
            return@Column
        }

        // ── Hero Card ──
        val (badgeBg, badgeText) = when (account.type) {
            AccountType.BANK -> AccountBadgeColors.BankBg to AccountBadgeColors.BankText
            AccountType.CRYPTO_EXCHANGE -> AccountBadgeColors.CryptoBg to AccountBadgeColors.CryptoText
            AccountType.DIGITAL_WALLET -> AccountBadgeColors.WalletBg to AccountBadgeColors.WalletText
            AccountType.CASH -> AccountBadgeColors.CashBg to AccountBadgeColors.CashText
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.screenHorizontal),
            shape = RoundedCornerShape(radius.xxl),
            color = colors.CardBackground,
        ) {
            Column(
                modifier = Modifier.padding(spacing.xxl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Account avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(radius.lg))
                        .background(badgeBg),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = account.name.take(2).uppercase(),
                        color = badgeText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(Modifier.height(spacing.md))

                // Balance
                Text(
                    text = CurrencyFormatter.format(account.balance, account.currencyCode),
                    color = colors.TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(spacing.xxs))

                // Platform & currency info
                Text(
                    text = "${account.platform.displayName} · ${account.currencyCode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextSecondary,
                )

                // Initial balance indicator
                if (account.initialBalance > 0.0 && account.initialBalance != account.balance) {
                    Spacer(Modifier.height(spacing.sm))
                    Text(
                        text = "Saldo inicial: ${CurrencyFormatter.format(account.initialBalance, account.currencyCode)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextSecondary.copy(alpha = 0.7f),
                    )
                }
            }
        }

        Spacer(Modifier.height(spacing.xxl))

        // ── Transactions Section ──
        Text(
            text = "Transacciones",
            style = MaterialTheme.typography.titleMedium,
            color = colors.TextPrimary,
            modifier = Modifier.padding(horizontal = spacing.screenHorizontal),
        )

        Spacer(Modifier.height(spacing.sm))

        if (uiState.transactions.isEmpty()) {
            EmptyState(
                icon = Lucide.Receipt,
                title = "Sin transacciones",
                description = "Las transacciones de esta cuenta aparecerán aquí",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = spacing.screenHorizontal),
                verticalArrangement = Arrangement.spacedBy(spacing.xs),
            ) {
                items(
                    items = uiState.transactions,
                    key = { it.id },
                ) { transaction ->
                    AccountTransactionItem(transaction = transaction)
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }

        // ── Delete Button at bottom ──
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.screenHorizontal)
                .padding(bottom = 110.dp),
            contentAlignment = Alignment.Center,
        ) {
            CeroFiaoButton(
                onClick = { viewModel.deleteAccount() },
                text = "Eliminar cuenta",
                variant = ButtonVariant.DangerSoft,
            )
        }
    }
}

@Composable
private fun AccountTransactionItem(transaction: Transaction) {
    val colors = CeroFiaoDesign.colors
    val spacing = CeroFiaoDesign.spacing
    val radius = CeroFiaoDesign.radius
    val isExpense = transaction.type == TransactionType.EXPENSE

    CeroFiaoCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(radius.sm))
                    .background(
                        if (isExpense) colors.expenseSoft
                        else colors.incomeSoft,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isExpense) CeroFiaoIcons.ArrowExpense else CeroFiaoIcons.ArrowIncome,
                    contentDescription = null,
                    tint = if (isExpense) colors.ExpenseColor else colors.IncomeColor,
                    modifier = Modifier.size(18.dp),
                )
            }

            Spacer(Modifier.width(spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.note ?: transaction.type.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = colors.TextPrimary,
                )
                Text(
                    text = DateUtils.formatDisplayDate(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextSecondary,
                )
            }

            Text(
                text = "${if (isExpense) "-" else "+"}${CurrencyFormatter.format(transaction.amount, transaction.currencyCode)}",
                color = if (isExpense) colors.ExpenseColor else colors.IncomeColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
