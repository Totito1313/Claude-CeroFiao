package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.Account

@Composable
fun AccountFilterDropdown(
    expanded: Boolean,
    accounts: List<Account>,
    selectedAccountId: String?,
    onAccountSelected: (String?) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = colors.fondoMenus,
        shadowElevation = 8.dp,
        modifier = Modifier.width(220.dp),
        offset = DpOffset(0.dp, 4.dp),
    ) {
        Text(
            text = "Cuentas",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colors.TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        AccountDropdownItem(
            label = "Todas las cuentas",
            isSelected = selectedAccountId == null,
            onClick = {
                onAccountSelected(null)
                onDismiss()
            },
        )

        accounts.forEach { account ->
            AccountDropdownItem(
                label = account.name,
                isSelected = account.id == selectedAccountId,
                onClick = {
                    onAccountSelected(account.id)
                    onDismiss()
                },
            )
        }
    }
}

@Composable
private fun AccountDropdownItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    DropdownMenuItem(
        text = {
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) colors.Primary else colors.TextPrimary,
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (isSelected) Lucide.CircleCheck else Lucide.Circle,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isSelected) colors.Primary else colors.CardBorder,
            )
        },
        onClick = onClick,
    )
}
