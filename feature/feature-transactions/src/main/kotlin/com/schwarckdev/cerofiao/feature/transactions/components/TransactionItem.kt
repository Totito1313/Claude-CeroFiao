package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.clip
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
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.feature.transactions.TransactionWithCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionItem(
    item: TransactionWithCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val tx = item.transaction
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.US)
    val dateText = dateFormat.format(Date(tx.date))

    val icon = when (tx.type) {
        TransactionType.INCOME -> Lucide.ArrowDownLeft
        TransactionType.EXPENSE -> Lucide.ArrowUpRight
        TransactionType.TRANSFER -> Lucide.ArrowLeftRight
    }

    val iconColor = when (tx.type) {
        TransactionType.INCOME -> colors.IncomeColor
        TransactionType.EXPENSE -> colors.ExpenseColor
        TransactionType.TRANSFER -> colors.InternalTransferColor
    }

    val amountPrefix = when (tx.type) {
        TransactionType.INCOME -> "+"
        TransactionType.EXPENSE -> "-"
        TransactionType.TRANSFER -> ""
    }

    val (badgeBg, badgeText) = when (item.accountType) {
        AccountType.CRYPTO_EXCHANGE -> AccountBadgeColors.CryptoBg to AccountBadgeColors.CryptoText
        AccountType.BANK -> AccountBadgeColors.BankBg to AccountBadgeColors.BankText
        AccountType.DIGITAL_WALLET -> AccountBadgeColors.WalletBg to AccountBadgeColors.WalletText
        AccountType.CASH -> AccountBadgeColors.CashBg to AccountBadgeColors.CashText
    }
    val badgeLabel = when (item.accountType) {
        AccountType.CRYPTO_EXCHANGE -> "CRYPTO"
        AccountType.CASH -> "EFECTIVO"
        else -> item.accountPlatform.displayName.uppercase()
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100.dp),
        color = colors.CardBackground,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Category icon circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colors.SurfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = iconColor,
                )
            }

            Spacer(Modifier.width(12.dp))

            // Name + date
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.note ?: item.categoryName,
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

            Spacer(Modifier.width(8.dp))

            // Amount + account badge
            Column(horizontalAlignment = Alignment.End) {
                // Amount line - show conversion for transfers, plain for others
                if (tx.type == TransactionType.TRANSFER && tx.currencyCode != "USD") {
                    Text(
                        text = "${CurrencyFormatter.format(tx.amount, tx.currencyCode)} = ${CurrencyFormatter.format(tx.amountInUsd, "USD")}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary,
                    )
                } else {
                    Text(
                        text = "$amountPrefix${CurrencyFormatter.format(tx.amount, tx.currencyCode)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary,
                    )
                }

                // Account badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBg,
                ) {
                    Text(
                        text = badgeLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText,
                        letterSpacing = (-0.3).sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                    )
                }
            }
        }
    }
}
