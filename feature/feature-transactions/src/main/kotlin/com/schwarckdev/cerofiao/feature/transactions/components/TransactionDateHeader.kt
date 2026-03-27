package com.schwarckdev.cerofiao.feature.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionDateHeader(
    dateMillis: Long,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    val dateFormat = SimpleDateFormat("MMM dd", Locale.forLanguageTag("es"))
    val dateText = dateFormat.format(Date(dateMillis)).replaceFirstChar { it.uppercase() }

    val isToday = DateUtils.isToday(dateMillis)
    val displayDate = if (isToday) "Hoy" else dateText

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = displayDate,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextSecondary,
            letterSpacing = (-0.2).sp,
        )
    }
}
