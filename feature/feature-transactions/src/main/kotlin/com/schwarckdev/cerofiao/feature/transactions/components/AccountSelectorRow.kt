package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Wallet
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Account

@Composable
fun AccountSelectorRow(
    accounts: List<Account>,
    selectedAccountId: String?,
    onAccountSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(items = accounts, key = { it.id }) { account ->
            AccountChip(
                account = account,
                isSelected = account.id == selectedAccountId,
                onClick = { onAccountSelected(account.id) },
            )
        }
    }
}

@Composable
private fun AccountChip(
    account: Account,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val haptic = LocalHapticFeedback.current

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) colors.Primary.copy(alpha = 0.08f) else colors.Surface,
        label = "accountChipBg",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.Primary else colors.CardBorder,
        label = "accountChipBorder",
    )

    Surface(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Lucide.Wallet,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isSelected) colors.Primary else colors.TextSecondary,
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = account.name,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) colors.Primary else colors.TextPrimary,
                )
                Text(
                    text = account.currencyCode,
                    fontSize = 10.sp,
                    color = colors.TextSecondary,
                )
            }
        }
    }
}
