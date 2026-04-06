package com.schwarckdev.cerofiao.feature.accounts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Banknote
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Landmark
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Wallet
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonGroupItem
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonSize
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButtonGroup
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.AccountBadgeColors
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCardConfig
import com.schwarckdev.cerofiao.core.model.AccountType

// ── Pencil design tokens (from .pen file) ──
private val PieCardCornerRadius = 48.dp
private val AccountCardCornerRadius = 32.dp
private val ProgressGreen = Color(0xFF0C7652)

@Composable
fun AccountListScreen(
    onAccountClick: (String) -> Unit,
    onAddAccount: () -> Unit,
    onTransfer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountListViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Standard, title = "Cuentas")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .statusBarsPadding()
            .padding(top = 70.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // ── Section: Repartición por cuentas ──
        if (uiState.pieSlices.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Repartición por cuentas",
                    titleFontSize = 18,
                    actionText = "Ver más",
                )
            }

            item {
                PieChartCard(
                    slices = uiState.pieSlices,
                    totalConverted = uiState.totalConverted,
                    displayCurrency = uiState.displayCurrency,
                    onCurrencyChange = viewModel::setChartCurrency,
                )
            }
        }

        // ── Section: Cuentas header ──
        item {
            SectionHeader(
                title = "Cuentas",
                titleFontSize = 20,
                actionText = "Crear Nueva Cuenta",
                onAction = onAddAccount,
            )
        }

        // ── Account Cards ──
        if (uiState.accounts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Lucide.Wallet,
                            contentDescription = null,
                            tint = colors.TextPrimary.copy(alpha = 0.25f),
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "No tienes cuentas",
                            color = colors.TextPrimary.copy(alpha = 0.5f),
                            fontSize = 15.sp,
                        )
                        Text(
                            "Agrega tu primera cuenta para empezar",
                            color = colors.TextPrimary.copy(alpha = 0.35f),
                            fontSize = 13.sp,
                        )
                    }
                }
            }
        } else {
            itemsIndexed(
                items = uiState.accounts,
                key = { _, data -> data.account.id },
            ) { _, cardData ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 }),
                ) {
                    AccountCard(
                        data = cardData,
                        onClick = { onAccountClick(cardData.account.id) },
                    )
                }
            }
        }

        // ── "CREAR CUENTA" card ──
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .pressableFeedback(onClick = onAddAccount, variant = FeedbackVariant.ScaleHighlight),
                shape = RoundedCornerShape(AccountCardCornerRadius),
                color = colors.CardBackground,
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    // Plus icon circle — Pencil: #1616160d bg, #00000066 icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color(0x0D161616),
                                CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Lucide.Plus,
                            contentDescription = null,
                            tint = colors.TextPrimary.copy(alpha = 0.4f),
                            modifier = Modifier.size(14.dp),
                        )
                    }
                    Text(
                        text = "CREAR cuenta",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary.copy(alpha = 0.4f),
                        letterSpacing = 1.2.sp,
                    )
                }
            }
        }

        item { Spacer(Modifier.height(110.dp)) }
    }
}

// ── Section Header ──

@Composable
private fun SectionHeader(
    title: String,
    titleFontSize: Int = 20,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val colors = CeroFiaoDesign.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = titleFontSize.sp,
            fontWeight = FontWeight.Bold,
            color = colors.TextPrimary,
            letterSpacing = (-0.6).sp,
        )
        if (actionText != null) {
            Text(
                text = actionText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                // Pencil: #00000080
                color = colors.TextPrimary.copy(alpha = 0.5f),
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .then(
                        if (onAction != null) Modifier.clickable(onClick = onAction) else Modifier,
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            )
        }
    }
}

// ── Pie Chart Card ──

@Composable
private fun PieChartCard(
    slices: List<AccountPieSlice>,
    totalConverted: Double,
    displayCurrency: String,
    onCurrencyChange: (ChartDisplayCurrency) -> Unit,
) {
    val colors = CeroFiaoDesign.colors

    // Pencil: cornerRadius 48, fill #fcfcffff, padding [20, 30], gap 12
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PieCardCornerRadius),
        color = colors.CardBackground,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Currency ButtonGroup + Total — Pencil: row with space_between
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CurrencyButtonGroup(
                    selected = ChartDisplayCurrency.entries.first { it.code == displayCurrency },
                    onSelect = onCurrencyChange,
                )

                // Pencil: fontSize 12, fontWeight normal, fill #000000cc
                AnimatedContent(
                    targetState = CurrencyFormatter.format(totalConverted, displayCurrency),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "total",
                ) { formattedTotal ->
                    Text(
                        text = "Total: $formattedTotal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = colors.TextPrimary.copy(alpha = 0.8f),
                    )
                }
            }

            // Chart + Legend — Pencil: gap 16
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Pencil: 130x130 chart
                DonutChart(
                    slices = slices,
                    modifier = Modifier.size(130.dp),
                )

                // Legend — Pencil: gap 8
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    slices.forEach { slice ->
                        PieLegendItem(
                            name = slice.accountName,
                            amount = CurrencyFormatter.format(slice.balanceConverted, displayCurrency),
                            color = slice.color,
                        )
                    }
                }
            }
        }
    }
}

// ── Currency ButtonGroup + Dropdown ──
// Pencil: ButtonGroup with "$ Dolar [BCV]" left button + ChevronDown right button

@Composable
private fun CurrencyButtonGroup(
    selected: ChartDisplayCurrency,
    onSelect: (ChartDisplayCurrency) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        CeroFiaoButtonGroup(
            items = listOf(
                ButtonGroupItem(
                    key = "currency",
                    text = "${selected.symbol}  ${selected.displayName}",
                    badge = selected.sourceLabel,
                    isActive = true,
                    onClick = { expanded = true },
                ),
                ButtonGroupItem(
                    key = "dropdown",
                    icon = Lucide.ChevronDown,
                    onClick = { expanded = true },
                ),
            ),
            size = ButtonSize.Small,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ChartDisplayCurrency.entries.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${currency.symbol}  ${currency.displayName}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = CeroFiaoDesign.colors.TextPrimary,
                            )
                            Text(
                                text = currency.sourceLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = CeroFiaoDesign.colors.Primary,
                            )
                        }
                    },
                    onClick = {
                        onSelect(currency)
                        expanded = false
                    },
                )
            }
        }
    }
}

// ── Donut Chart ──

@Composable
private fun DonutChart(
    slices: List<AccountPieSlice>,
    modifier: Modifier = Modifier,
) {
    val total = slices.sumOf { it.balanceConverted }.toFloat()
    if (total <= 0f) return

    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.18f
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
        val arcSize = Size(diameter, diameter)
        val gapDegrees = 4f
        val totalGap = gapDegrees * slices.size
        var startAngle = -90f

        slices.forEach { slice ->
            val sweep = (slice.balanceConverted.toFloat() / total) * (360f - totalGap)
            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
            startAngle += sweep + gapDegrees
        }
    }
}

@Composable
private fun PieLegendItem(
    name: String,
    amount: String,
    color: Color,
) {
    val colors = CeroFiaoDesign.colors

    // Pencil legend row: height 18, gap between dot+name and amount
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Pencil: 8x8 colored dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape),
            )
            // Pencil: fontSize 12, fontWeight 500, fill #0000008c
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = colors.TextPrimary.copy(alpha = 0.55f),
            )
        }
        // Pencil: fontSize 12, fontWeight normal, fill #000000ff
        Text(
            text = amount,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = colors.TextPrimary,
        )
    }
}

// ── Account Card ──

@Composable
private fun AccountCard(
    data: AccountCardData,
    onClick: () -> Unit,
) {
    val account = data.account
    val colors = CeroFiaoDesign.colors

    val (badgeBg, badgeText) = when (account.type) {
        AccountType.BANK -> AccountBadgeColors.BankBg to AccountBadgeColors.BankText
        AccountType.CRYPTO_EXCHANGE -> AccountBadgeColors.CryptoBg to AccountBadgeColors.CryptoText
        AccountType.DIGITAL_WALLET -> AccountBadgeColors.WalletBg to AccountBadgeColors.WalletText
        AccountType.CASH -> AccountBadgeColors.CashBg to AccountBadgeColors.CashText
    }

    // Pencil: type labels use title case (e.g. "Banco -", "CRypto -")
    val typeLabel = when (account.type) {
        AccountType.BANK -> "Banco"
        AccountType.CRYPTO_EXCHANGE -> "Crypto"
        AccountType.DIGITAL_WALLET -> "Billetera"
        AccountType.CASH -> "Efectivo"
    }

    val hasProgress = data.progressRatio != null

    // Pencil: cornerRadius 32, fill #fcfcffff, padding 24, background_blur effect
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .pressableFeedback(onClick = onClick, variant = FeedbackVariant.ScaleHighlight),
        shape = RoundedCornerShape(AccountCardCornerRadius),
        color = colors.CardBackground,
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // ── Header: type label + name + platform badge ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Pencil: "Banco -" fontSize 11, fontWeight 700, letterSpacing 1.1, fill #00000066
                    //         "BS" fontSize 12, fontWeight 900, letterSpacing 1.1, fill #00000066
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "$typeLabel -",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.TextPrimary.copy(alpha = 0.4f),
                            letterSpacing = 1.1.sp,
                        )
                        Text(
                            text = account.currencyCode,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = colors.TextPrimary.copy(alpha = 0.4f),
                            letterSpacing = 1.1.sp,
                        )
                    }
                    // Pencil: fontSize 18, fontWeight 700, fill #000000ff, lineHeight 1.556
                    Text(
                        text = account.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary,
                        lineHeight = 28.sp,
                    )
                }

                // Pencil: 30x30, cornerRadius 12, image fill
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg),
                    contentAlignment = Alignment.Center,
                ) {
                    PlatformIcon(accountType = account.type, tintColor = badgeText)
                }
            }

            // Pencil: gap ~40dp for progress card, ~36dp for non-progress
            Spacer(Modifier.height(if (hasProgress) 40.dp else 36.dp))

            // ── Balance section ──
            if (hasProgress) {
                // Pencil: spent "800,00 Bs" fontSize 16, fontWeight 700, fill #000000b2
                //         "/" fontSize 20, fontWeight 700, fill #000000ff
                //         total "$1.200,00 Bs" fontSize 20, fontWeight 700, fill #000000ff
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = CurrencyFormatter.format(account.balance, account.currencyCode),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary.copy(alpha = 0.7f),
                    )
                    Text(
                        text = "/",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary,
                    )
                    Text(
                        text = CurrencyFormatter.format(account.initialBalance, account.currencyCode),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary,
                    )
                }

                // Pencil: gap 12.5 between balance and progress bar
                Spacer(Modifier.height(12.dp))

                // Pencil: height 4, cornerRadius 9999, bg #0000001a, fill #0c7652ff
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(9999.dp))
                        .background(colors.TextPrimary.copy(alpha = 0.1f)),
                ) {
                    val ratio = data.progressRatio ?: 0f
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(ratio)
                            .height(4.dp)
                            .clip(RoundedCornerShape(9999.dp))
                            .background(ProgressGreen),
                    )
                }
            } else {
                // Pencil: fontSize 20, fontWeight 700, fill #000000ff, lineHeight 1.6
                Text(
                    text = CurrencyFormatter.format(account.balance, account.currencyCode),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.TextPrimary,
                    lineHeight = 32.sp,
                )

                val label = data.lastTransactionLabel
                if (label != null) {
                    // Pencil: gap 4.5, fontSize 12, fontWeight 700, fill #000000b2
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextPrimary.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

// ── Platform Icon (mirrors dashboard pattern) ──

@Composable
private fun PlatformIcon(
    accountType: AccountType,
    tintColor: Color,
    modifier: Modifier = Modifier,
) {
    when (accountType) {
        AccountType.CRYPTO_EXCHANGE -> {
            Box(
                modifier = modifier
                    .size(16.dp)
                    .rotate(45f)
                    .background(tintColor, RoundedCornerShape(3.dp)),
            )
        }
        AccountType.BANK -> {
            Icon(
                imageVector = Lucide.Landmark,
                contentDescription = null,
                modifier = modifier.size(18.dp),
                tint = tintColor,
            )
        }
        AccountType.DIGITAL_WALLET -> {
            Icon(
                imageVector = Lucide.Wallet,
                contentDescription = null,
                modifier = modifier.size(18.dp),
                tint = tintColor,
            )
        }
        AccountType.CASH -> {
            Icon(
                imageVector = Lucide.Banknote,
                contentDescription = null,
                modifier = modifier.size(18.dp),
                tint = tintColor,
            )
        }
    }
}
