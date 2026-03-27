package com.schwarckdev.cerofiao.feature.dashboard.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.composables.icons.lucide.Banknote
import com.composables.icons.lucide.Landmark
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Wallet
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.theme.AccountBadgeColors
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCardConfig
import com.schwarckdev.cerofiao.core.model.AccountBalance
import com.schwarckdev.cerofiao.core.model.AccountType

@Composable
fun AccountsSection(
    accounts: List<AccountBalance>,
    onViewAll: () -> Unit,
    onAddAccount: () -> Unit,
    onAccountClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(0.dp),
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = "Cuentas",
            onViewAll = onViewAll,
            modifier = Modifier.padding(contentPadding)
        )
        Spacer(Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(accounts) { accountBalance ->
                AccountCard(
                    accountBalance = accountBalance,
                    onClick = { onAccountClick(accountBalance.account.id) },
                )
            }
            item {
                AddAccountCard(onClick = onAddAccount)
            }
        }
    }
}

@Composable
private fun AccountCard(
    accountBalance: AccountBalance,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current
    val account = accountBalance.account
    val currencySymbol = when (account.currencyCode) {
        "USD" -> "$"
        "VES" -> "Bs"
        "USDT" -> "\u20AE USDT"
        "EUR" -> "\u20AC"
        "EURI" -> "\u20AC EURI"
        else -> account.currencyCode
    }

    val currencySuffix = when (account.currencyCode) {
        "USD" -> "$"
        "VES" -> "Bs"
        "USDT" -> "\u20AE"
        "EUR" -> "\u20AC"
        "EURI" -> "\u20AC"
        else -> account.currencyCode
    }

    val (badgeBg, badgeText, badgeLabel) = when (account.type) {
        AccountType.CRYPTO_EXCHANGE -> Triple(
            AccountBadgeColors.CryptoBg,
            AccountBadgeColors.CryptoText,
            "CRYPTO",
        )
        AccountType.BANK -> Triple(
            AccountBadgeColors.BankBg,
            AccountBadgeColors.BankText,
            account.platform.displayName,
        )
        AccountType.DIGITAL_WALLET -> Triple(
            AccountBadgeColors.WalletBg,
            AccountBadgeColors.WalletText,
            account.platform.displayName,
        )
        AccountType.CASH -> Triple(
            AccountBadgeColors.CashBg,
            AccountBadgeColors.CashText,
            "Efectivo",
        )
    }

    Surface(
        onClick = onClick,
        modifier = modifier
            .width(200.dp)
            .height(124.dp),
        shape = RoundedCornerShape(32.dp),
        color = colors.Foreground.copy(alpha = cardConfig.backgroundOpacity),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Top row: currency symbol + platform icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = currencySymbol,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.TextPrimary,
                )
                PlatformIcon(accountType = account.type)
            }

            // Bottom row: transaction badge + total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                // Transaction Info Badge
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = colors.Background,
                ) {
                    Column(
                        modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "+0.00 $currencySuffix",
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

                // Total Box
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextSecondary,
                    )
                    Text(
                        text = CurrencyFormatter.format(
                            accountBalance.balanceInOriginalCurrency,
                            account.currencyCode,
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlatformIcon(
    accountType: AccountType,
    modifier: Modifier = Modifier,
) {
    when (accountType) {
        AccountType.CRYPTO_EXCHANGE -> {
            // Diamond shape using a rotated box
            Box(
                modifier = modifier
                    .size(18.dp)
                    .rotate(45f)
                    .background(AccountBadgeColors.CryptoText, RoundedCornerShape(3.dp)),
            )
        }
        AccountType.BANK -> {
            Icon(
                imageVector = Lucide.Landmark,
                contentDescription = "Bank",
                modifier = modifier.size(20.dp),
                tint = AccountBadgeColors.BankText,
            )
        }
        AccountType.DIGITAL_WALLET -> {
            Icon(
                imageVector = Lucide.Wallet,
                contentDescription = "Wallet",
                modifier = modifier.size(20.dp),
                tint = AccountBadgeColors.WalletText,
            )
        }
        AccountType.CASH -> {
            Icon(
                imageVector = Lucide.Banknote,
                contentDescription = "Cash",
                modifier = modifier.size(20.dp),
                tint = AccountBadgeColors.CashText,
            )
        }
    }
}

@Composable
private fun AddAccountCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current
    Surface(
        onClick = onClick,
        modifier = modifier
            .width(200.dp)
            .height(124.dp),
        shape = RoundedCornerShape(32.dp),
        color = colors.Foreground.copy(alpha = cardConfig.backgroundOpacity),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(colors.SurfaceVariant, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Lucide.Plus,
                    contentDescription = "Add account",
                    modifier = Modifier.size(14.dp),
                    tint = colors.TextSecondary,
                )
            }
            Text(
                text = "CREAR CUENTA",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextSecondary,
                letterSpacing = 1.2.sp,
            )
        }
    }
}
