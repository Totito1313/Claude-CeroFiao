package com.schwarckdev.cerofiao.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Box
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

@Composable
fun MoneyAmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    currency: String,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val t = CeroFiaoDesign.colors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = amount,
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = t.TextPrimary,
                textAlign = TextAlign.Start,
                letterSpacing = (-1.2).sp
            ),
            singleLine = true,
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (amount.isEmpty()) {
                        Text(
                            text = "0.00",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = t.InactiveColor,
                            letterSpacing = (-1.2).sp
                        )
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primary,
            onClick = onCurrencyClick
        ) {
            Text(
                text = currency,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
