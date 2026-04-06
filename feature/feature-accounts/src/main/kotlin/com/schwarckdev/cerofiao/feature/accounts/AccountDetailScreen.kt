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
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCard
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.AccountBadgeColors
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType

@Composable
fun AccountDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val account = uiState.account
    val colors = CeroFiaoDesign.colors

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
            .background(colors.Background)
            .statusBarsPadding()
            .padding(top = 70.dp),
    ) {
        if (account == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando...", color = colors.TextSecondary)
            }
            return@Column
        }

        // Account hero card
        val (badgeBg, badgeText) = when (account.type) {
            AccountType.BANK -> AccountBadgeColors.BankBg to AccountBadgeColors.BankText
            AccountType.CRYPTO_EXCHANGE -> AccountBadgeColors.CryptoBg to AccountBadgeColors.CryptoText
            AccountType.DIGITAL_WALLET -> AccountBadgeColors.WalletBg to AccountBadgeColors.WalletText
            AccountType.CASH -> AccountBadgeColors.CashBg to AccountBadgeColors.CashText
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = colors.CardBackground,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
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

                Spacer(Modifier.height(12.dp))

                Text(
                    text = CurrencyFormatter.format(account.balance, account.currencyCode),
                    color = colors.TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${account.platform.displayName} · ${account.currencyCode}",
                    color = colors.TextSecondary,
                    fontSize = 13.sp,
                )

                Spacer(Modifier.height(16.dp))

                // Delete button
                Surface(
                    modifier = Modifier
                        .pressableFeedback(
                            onClick = { viewModel.deleteAccount() },
                            variant = FeedbackVariant.ScaleHighlight,
                        ),
                    shape = RoundedCornerShape(10.dp),
                    color = colors.ExpenseColor.copy(alpha = 0.1f),
                ) {
                    Text(
                        text = "Eliminar cuenta",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = colors.ExpenseColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Transactions header
        Text(
            text = "Transacciones",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = colors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(Modifier.height(8.dp))

        if (uiState.transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Sin transacciones en esta cuenta",
                    color = colors.TextSecondary,
                    fontSize = 14.sp,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
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
    }
}

@Composable
private fun AccountTransactionItem(transaction: Transaction) {
    val colors = CeroFiaoDesign.colors
    val isExpense = transaction.type == TransactionType.EXPENSE

    CeroFiaoCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isExpense) colors.ExpenseColor.copy(alpha = 0.1f)
                        else colors.IncomeColor.copy(alpha = 0.1f)
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

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.note ?: transaction.type.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    color = colors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = DateUtils.formatDisplayDate(transaction.date),
                    color = colors.TextSecondary,
                    fontSize = 12.sp,
                )
            }

            Text(
                text = "${if (isExpense) "-" else "+"}${CurrencyFormatter.format(transaction.amount, transaction.currencyCode)}",
                color = if (isExpense) colors.ExpenseColor else colors.IncomeColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
