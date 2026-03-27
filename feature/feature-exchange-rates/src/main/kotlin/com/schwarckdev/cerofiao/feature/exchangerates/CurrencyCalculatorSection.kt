package com.schwarckdev.cerofiao.feature.exchangerates

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ArrowUpDown
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.TransferGradient
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource

private data class CurrencyOption(
    val code: String,
    val symbol: String,
    val label: String,
)

private val currencyOptions = listOf(
    CurrencyOption("USD", "$", "USD"),
    CurrencyOption("VES", "Bs", "Bs"),
    CurrencyOption("USDT", "₮", "USDT"),
    CurrencyOption("EUR", "€", "EUR"),
    CurrencyOption("EURI", "€", "EURI"),
)

@Composable
fun CurrencyCalculatorSection(
    amount: String,
    fromCurrency: String,
    toCurrency: String,
    result: Double?,
    resultSource: ExchangeRateSource?,
    customRate: String,
    isCustomRateEnabled: Boolean,
    isParityLossWarning: Boolean,
    parityDifferenceAmount: Double?,
    parityDifferenceVes: Double?,
    onAmountChange: (String) -> Unit,
    onFromCurrencyChange: (String) -> Unit,
    onToCurrencyChange: (String) -> Unit,
    onSwap: () -> Unit,
    onCustomRateChange: (String) -> Unit,
    onToggleCustomRate: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors

    var swapClicks by remember { mutableIntStateOf(0) }
    val rotation by animateFloatAsState(
        targetValue = swapClicks * 180f,
        animationSpec = tween(durationMillis = 500),
        label = "swap_rotation"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(38.dp),
        color = colors.Foreground,
        shadowElevation = 8.dp, // Clean, elegant shadow
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        ) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Conversor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextPrimary,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimatedContent(targetState = isCustomRateEnabled, label = "custom_rate_title") { isCustom ->
                        Text(
                            text = if (isCustom) "Tasa Manual" else "Tasa actual",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCustom) Color(0xFF22C9A6) else colors.TextSecondary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Switch(
                        checked = isCustomRateEnabled,
                        onCheckedChange = onToggleCustomRate,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF22C9A6),
                            uncheckedThumbColor = colors.Background,
                            uncheckedTrackColor = colors.Background,
                            uncheckedBorderColor = colors.TextSecondary.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.scale(0.80f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // --- FROM SECTION ---
            Text(
                text = "Monto a enviar",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CurrencyDropdown(
                    selectedCurrency = fromCurrency,
                    onCurrencySelect = onFromCurrencyChange
                )
                
                BasicTextField(
                    value = amount,
                    onValueChange = { 
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            onAmountChange(it) 
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = colors.TextPrimary,
                        textAlign = TextAlign.End
                    ),
                    cursorBrush = SolidColor(Color(0xFF22C9A6)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
                            if (amount.isEmpty()) {
                                Text(
                                    text = "0",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Black,
                                    color = colors.TextSecondary.copy(alpha = 0.2f),
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // --- SWAP BUTTON (CENTER) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        swapClicks++
                        onSwap()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(colors.Background, CircleShape)
                        .padding(4.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(TransferGradient, CircleShape),
                ) {
                    Icon(
                        imageVector = Lucide.ArrowUpDown,
                        contentDescription = "Intercambiar",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer { rotationZ = rotation }
                    )
                }
            }

            // --- TO SECTION ---
            Text(
                text = "Monto estimado",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CurrencyDropdown(
                    selectedCurrency = toCurrency,
                    onCurrencySelect = onToCurrencyChange
                )
                
                val finalRes = if (result != null) String.format("%.2f", result) else ""
                AnimatedContent(
                    targetState = finalRes, 
                    label = "amount_anim",
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                ) { targetAmount ->
                    Text(
                        text = targetAmount.ifEmpty { "0" },
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = if (targetAmount.isNotEmpty()) colors.TextPrimary else colors.TextSecondary.copy(alpha = 0.2f),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Minimal Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.TextSecondary.copy(alpha = 0.08f))
            )
            
            Spacer(modifier = Modifier.height(20.dp))

            // --- FOOTER (Custom Rate logic or Validation logic) ---
            AnimatedContent(
                targetState = isCustomRateEnabled,
                label = "footer_animation",
                modifier = Modifier.fillMaxWidth()
            ) { isCustom ->
                if (isCustom) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Establece tu propia tasa",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.TextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "1 $fromCurrency = ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.TextSecondary
                            )
                            Spacer(Modifier.width(8.dp))
                            BasicTextField(
                                value = customRate,
                                onValueChange = {
                                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                        onCustomRateChange(it)
                                    }
                                },
                                textStyle = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF22C9A6),
                                    textAlign = TextAlign.Center
                                ),
                                cursorBrush = SolidColor(Color(0xFF22C9A6)),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                modifier = Modifier
                                    .width(IntrinsicSize.Min)
                                    .defaultMinSize(minWidth = 40.dp),
                                decorationBox = { innerTextField ->
                                    Box {
                                        if (customRate.isEmpty()) {
                                            Text(
                                                text = "0",
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Black,
                                                color = colors.TextSecondary.copy(alpha = 0.3f),
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = toCurrency,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.TextPrimary
                            )
                        }
                    }
                } else {
                    AnimatedContent(
                        targetState = resultSource,
                        label = "source_animation",
                        modifier = Modifier.fillMaxWidth()
                    ) { targetSource ->
                        if (targetSource != null && result != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Lucide.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF22C9A6),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tasa actual: ${targetSource.name}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.TextSecondary,
                                )
                            }
                        } else {
                            Box(modifier = Modifier.height(16.dp).fillMaxWidth())
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = isParityLossWarning && !isCustomRateEnabled,
                label = "parity_warning"
            ) { showWarning ->
                if (showWarning) {
                    Surface(
                        color = colors.AccentOrange.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Lucide.Info, contentDescription = null, tint = colors.AccentOrange, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(12.dp))

                            val warningText = if (parityDifferenceAmount != null && parityDifferenceVes != null && parityDifferenceAmount > 0.0) {
                                val diffFmt = String.format("%.2f", parityDifferenceAmount)
                                val vesFmt = String.format("%.2f", parityDifferenceVes)
                                "Diferencia cambiaria: $diffFmt $fromCurrency ≈ $vesFmt Bs"
                            } else {
                                "Este cálculo triangula a Bolívares (VES) reflejando la diferencia de valor real."
                            }

                            Text(
                                text = warningText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.TextPrimary,
                                lineHeight = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyDropdown(selectedCurrency: String, onCurrencySelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val colors = CeroFiaoDesign.colors
    val displayText = currencyOptions.find { it.code == selectedCurrency }?.label ?: selectedCurrency

    Box {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colors.Background,
            onClick = { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.TextPrimary
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Seleccionar Moneda",
                    tint = colors.TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colors.Foreground)
        ) {
            currencyOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${option.symbol} ${option.label}",
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary
                        )
                    },
                    onClick = {
                        onCurrencySelect(option.code)
                        expanded = false
                    }
                )
            }
        }
    }
}
