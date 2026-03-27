package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ArrowDownLeft
import com.composables.icons.lucide.ArrowLeftRight
import com.composables.icons.lucide.ArrowUpRight
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.theme.AccountBadgeColors
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCardConfig
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.feature.dashboard.EnrichedTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentTransactionsSection(
    transactions: List<EnrichedTransaction>,
    onViewAll: () -> Unit,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionHeader(title = "Recientes", onViewAll = onViewAll)
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            transactions.forEach { enriched ->
                TransactionItem(
                    enriched = enriched,
                    onClick = { onTransactionClick(enriched.transaction.id) },
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(
    enriched: EnrichedTransaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current
    val tx = enriched.transaction
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.US)
    val dateText = dateFormat.format(Date(tx.date))

    val icon = when (tx.type) {
        TransactionType.INCOME -> Lucide.ArrowDownLeft
        TransactionType.EXPENSE -> Lucide.ArrowUpRight
        TransactionType.TRANSFER -> Lucide.ArrowLeftRight
    }

    val amountPrefix = when (tx.type) {
        TransactionType.INCOME -> "+"
        TransactionType.EXPENSE -> "-"
        TransactionType.TRANSFER -> ""
    }

    val (badgeBg, badgeText) = when (enriched.accountType) {
        AccountType.CRYPTO_EXCHANGE -> AccountBadgeColors.CryptoBg to AccountBadgeColors.CryptoText
        AccountType.BANK -> AccountBadgeColors.BankBg to AccountBadgeColors.BankText
        AccountType.DIGITAL_WALLET -> AccountBadgeColors.WalletBg to AccountBadgeColors.WalletText
        AccountType.CASH -> AccountBadgeColors.CashBg to AccountBadgeColors.CashText
    }
    val badgeLabel = when (enriched.accountType) {
        AccountType.CRYPTO_EXCHANGE -> "CRYPTO"
        AccountType.CASH -> "Efectivo"
        else -> enriched.accountPlatform.displayName
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100.dp),
        color = colors.Foreground.copy(alpha = cardConfig.backgroundOpacity),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = colors.Background,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = colors.TextSecondary,
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = tx.note ?: enriched.categoryName ?: "Transacción",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = dateText,
                        fontSize = 12.sp,
                        color = colors.TextSecondary,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$amountPrefix${CurrencyFormatter.format(tx.amount, tx.currencyCode)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.TextPrimary,
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBg,
                ) {
                    Text(
                        text = badgeLabel.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText,
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 0.5.dp),
                    )
                }
            }
        }
    }
}
