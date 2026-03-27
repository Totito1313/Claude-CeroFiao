package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.feature.transactions.TransactionDateGroup

@Composable
fun TransactionHistorySection(
    groups: List<TransactionDateGroup>,
    selectedTypeFilter: TransactionType?,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors

    val typeLabel = when (selectedTypeFilter) {
        TransactionType.INCOME -> "Ingreso"
        TransactionType.EXPENSE -> "Gasto"
        else -> "Movimiento"
    }

    Column(modifier = modifier) {
        // Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Historial",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.TextPrimary,
                letterSpacing = (-0.6).sp,
            )
            Text(
                text = typeLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colors.TextSecondary,
            )
        }

        Spacer(Modifier.height(12.dp))

        // Date groups
        groups.forEach { group ->
            TransactionDateHeader(dateMillis = group.dateMillis)

            group.transactions.forEach { txWithCat ->
                TransactionItem(
                    item = txWithCat,
                    onClick = { onTransactionClick(txWithCat.transaction.id) },
                )
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}
