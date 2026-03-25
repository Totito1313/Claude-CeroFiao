package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.model.AccountBalance
import com.schwarckdev.cerofiao.core.model.AccountType

@Composable
fun AccountsSection(
    accounts: List<AccountBalance>,
    onViewAll: () -> Unit,
    onAddAccount: () -> Unit,
    onAccountClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionHeader(title = "Cuentas", onViewAll = onViewAll)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            accounts.forEach { accountBalance ->
                AccountCard(
                    accountBalance = accountBalance,
                    onClick = { onAccountClick(accountBalance.account.id) },
                )
            }
            AddAccountCard(onClick = onAddAccount)
        }
    }
}

@Composable
private fun AccountCard(
    accountBalance: AccountBalance,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val account = accountBalance.account
    val currencySymbol = when (account.currencyCode) {
        "USD" -> "$"
        "VES" -> "Bs"
        "USDT" -> "₮ USDT"
        "EUR" -> "€"
        "EURI" -> "€ EURI"
        else -> account.currencyCode
    }

    val (badgeBg, badgeText, badgeLabel) = when (account.type) {
        AccountType.CRYPTO_EXCHANGE -> Triple(
            Color(0xFFFBFF00).copy(alpha = 0.2f),
            Color(0xFFAAA700),
            "CRYPTO"
        )
        AccountType.BANK -> Triple(
            Color(0xFF00EAFF).copy(alpha = 0.2f),
            Color(0xFF009CAA),
            account.platform.displayName
        )
        AccountType.DIGITAL_WALLET -> Triple(
            Color(0xFF00FF51).copy(alpha = 0.1f),
            Color(0xFF0CA523),
            account.platform.displayName
        )
        AccountType.CASH -> Triple(
            Color(0xFF00FF51).copy(alpha = 0.1f),
            Color(0xFF0CA523),
            "Efectivo"
        )
    }

    Surface(
        modifier = modifier
            .width(190.dp)
            .height(124.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(32.dp),
        color = Color(0xFFFCFCFF),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Top: currency + name
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = currencySymbol,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                Text(
                    text = account.name.take(8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.5f),
                )
            }
            // Bottom: badge + total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                // Platform badge
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFF1F1F3),
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
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
                // Total
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.7f),
                    )
                    Text(
                        text = CurrencyFormatter.format(accountBalance.balanceInOriginalCurrency, account.currencyCode),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

@Composable
private fun AddAccountCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .height(124.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(32.dp),
        color = Color(0xFFFCFCFF),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.05f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Lucide.Plus,
                    contentDescription = "Add account",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Black.copy(alpha = 0.4f),
                )
            }
            Text(
                text = "CREAR CUENTA",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.4f),
                letterSpacing = 1.2.sp,
            )
        }
    }
}
