package com.schwarckdev.cerofiao.feature.debt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.components.CeroFiaoPrimaryButton
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.DebtType

@Composable
fun DebtDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DebtDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors
    val debt = uiState.debt

    ConfigureTopBar(variant = TopBarVariant.Detail, title = "Detalle de deuda", onBackClick = onBack)

    if (debt == null) return

    val typeColor = if (debt.type == DebtType.THEY_OWE) colors.IncomeColor else colors.ExpenseColor
    val progress = if (debt.originalAmount > 0) {
        ((debt.originalAmount - debt.remainingAmount) / debt.originalAmount).toFloat().coerceIn(0f, 1f)
    } else 0f

    var paymentAmount by remember { mutableStateOf("") }
    var paymentNote by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background)
            .statusBarsPadding()
            .padding(top = 70.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp, end = 16.dp, bottom = 40.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colors.Foreground,
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = debt.personName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.TextPrimary,
                            )
                            Text(
                                text = if (debt.type == DebtType.THEY_OWE) "Me debe" else "Le debo",
                                fontSize = 14.sp,
                                color = typeColor,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        if (debt.isSettled) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = colors.Success.copy(alpha = 0.15f),
                            ) {
                                Text(
                                    text = "Saldada",
                                    fontSize = 13.sp,
                                    color = colors.Success,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                )
                            }
                        }
                    }

                    // Amounts
                    Text(
                        text = CurrencyFormatter.format(debt.remainingAmount, debt.currencyCode),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = typeColor,
                    )
                    Text(
                        text = "de ${CurrencyFormatter.format(debt.originalAmount, debt.currencyCode)}",
                        fontSize = 14.sp,
                        color = colors.TextSecondary,
                    )

                    // Progress
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = typeColor,
                        trackColor = colors.SurfaceVariant,
                        strokeCap = StrokeCap.Round,
                    )

                    val note = debt.note
                    if (!note.isNullOrBlank()) {
                        Text(
                            text = note,
                            fontSize = 14.sp,
                            color = colors.TextSecondary,
                        )
                    }
                }
            }
        }

        // Register payment (if not settled)
        if (!debt.isSettled) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = colors.Foreground,
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = "Registrar pago",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.TextPrimary,
                        )

                        CeroFiaoTextField(
                            value = paymentAmount,
                            onValueChange = { paymentAmount = it },
                            label = "Monto",
                            placeholder = "0.00",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        CeroFiaoTextField(
                            value = paymentNote,
                            onValueChange = { paymentNote = it },
                            label = "Nota (opcional)",
                            placeholder = "Transferencia, efectivo...",
                            modifier = Modifier.fillMaxWidth(),
                        )

                        CeroFiaoPrimaryButton(
                            text = "Registrar pago",
                            onClick = {
                                viewModel.registerPayment(paymentAmount, paymentNote)
                                paymentAmount = ""
                                paymentNote = ""
                            },
                            enabled = (paymentAmount.toDoubleOrNull() ?: 0.0) > 0,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }

        // Payment history
        if (uiState.payments.isNotEmpty()) {
            item {
                Text(
                    text = "Historial de pagos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextSecondary,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }

            items(uiState.payments, key = { it.id }) { payment ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.Foreground,
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = CurrencyFormatter.format(payment.amount, payment.currencyCode),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.TextPrimary,
                            )
                            val note = payment.note
                            if (!note.isNullOrBlank()) {
                                Text(
                                    text = note,
                                    fontSize = 13.sp,
                                    color = colors.TextSecondary,
                                )
                            }
                        }
                        Text(
                            text = DateUtils.formatDisplayDate(payment.paidAt),
                            fontSize = 13.sp,
                            color = colors.TextSecondary,
                        )
                    }
                }
            }
        }
    }
}
