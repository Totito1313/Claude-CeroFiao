package com.schwarckdev.cerofiao.feature.accounts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Banknote
import com.composables.icons.lucide.ChartPie
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Landmark
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Trash2
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
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.ui.CeroFiaoFAB
import com.schwarckdev.cerofiao.core.ui.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
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
    val spacing = CeroFiaoDesign.spacing
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState.accounts.isEmpty() && uiState.pieSlices.isEmpty()) {
            // Full empty state — centered vertically with top bar offset
            Spacer(Modifier.weight(1f))
            EmptyState(
                icon = Lucide.Wallet,
                title = "Sin cuentas",
                description = "Agrega tu primera cuenta para empezar a rastrear tus finanzas",
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(spacing.lg),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = spacing.screenHorizontal,
                    end = spacing.screenHorizontal,
                    top = 100.dp,
                    bottom = 50.dp,
                ),
            ) {
                // ── Section: Repartición por cuentas ──
                item {
                    SectionHeader(
                        title = "Repartición por cuentas",
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

                // ── Section: Cuentas header ──
                item {
                    SectionHeader(
                        title = "Cuentas",
                        actionText = "Crear Nueva",
                        onAction = onAddAccount,
                    )
                }

                // ── Account Cards with swipe-to-delete ──
                itemsIndexed(
                    items = uiState.accounts,
                    key = { _, data -> data.account.id },
                ) { _, cardData ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { visible = true }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                            slideInVertically(
                                initialOffsetY = { it / 3 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow,
                                ),
                            ),
                    ) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.deleteAccount(cardData.account.id)
                                    true
                                } else {
                                    false
                                }
                            },
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(CeroFiaoDesign.radius.xxl))
                                        .background(colors.ExpenseColor),
                                    contentAlignment = Alignment.CenterEnd,
                                ) {
                                    Row(
                                        modifier = Modifier.padding(end = spacing.xl),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                                    ) {
                                        Text(
                                            text = "Eliminar",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                        )
                                        Icon(
                                            imageVector = Lucide.Trash2,
                                            contentDescription = "Eliminar",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp),
                                        )
                                    }
                                }
                            },
                        ) {
                            AccountCard(
                                data = cardData,
                                onClick = { onAccountClick(cardData.account.id) },
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(spacing.lg)) }
            }
        }

        // FAB — always visible
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = spacing.screenHorizontal, bottom = 100.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            CeroFiaoFAB(
                onClick = onAddAccount,
                icon = Lucide.Plus,
            )
        }
    }
}

// ── Section Header ──

@Composable
private fun SectionHeader(
    title: String,
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
            style = MaterialTheme.typography.titleMedium,
            color = colors.TextPrimary,
        )
        if (actionText != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelSmall,
                color = colors.Primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(CeroFiaoDesign.radius.sm))
                    .then(
                        if (onAction != null) Modifier.clickable(onClick = onAction) else Modifier,
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
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
    val spacing = CeroFiaoDesign.spacing
    val radius = CeroFiaoDesign.radius

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(radius.xxxl),
        color = colors.CardBackground,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = spacing.xxl, vertical = spacing.xl),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            // Currency ButtonGroup + Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CurrencyButtonGroup(
                    selected = ChartDisplayCurrency.entries.first { it.code == displayCurrency },
                    onSelect = onCurrencyChange,
                )

                if (slices.isNotEmpty()) {
                    AnimatedContent(
                        targetState = CurrencyFormatter.format(totalConverted, displayCurrency),
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "total",
                    ) { formattedTotal ->
                        Text(
                            text = "Total: $formattedTotal",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextPrimary.copy(alpha = 0.8f),
                        )
                    }
                }
            }

            if (slices.isNotEmpty()) {
                // Chart + Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.lg),
                ) {
                    DonutChart(
                        slices = slices,
                        modifier = Modifier.size(130.dp),
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(spacing.sm),
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
            } else {
                // Empty chart state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.xxl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Lucide.ChartPie,
                        contentDescription = null,
                        tint = colors.TextSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(Modifier.height(spacing.md))
                    Text(
                        "Sin datos para graficar",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = colors.TextSecondary,
                    )
                    Text(
                        "Agrega cuentas con saldo para ver la distribución",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextSecondary.copy(alpha = 0.6f),
                    )
                }
            }
        }
    }
}

// ── Currency ButtonGroup + Dropdown ──

@Composable
private fun CurrencyButtonGroup(
    selected: ChartDisplayCurrency,
    onSelect: (ChartDisplayCurrency) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = CeroFiaoDesign.colors
    val radius = CeroFiaoDesign.radius
    val density = LocalDensity.current
    var anchorHeightPx by remember { mutableStateOf(0) }
    val dropdownState = remember { MutableTransitionState(false) }
    dropdownState.targetState = expanded

    Box(modifier = Modifier.onSizeChanged { anchorHeightPx = it.height }) {
        CeroFiaoButtonGroup(
            items = listOf(
                ButtonGroupItem(
                    key = "currency",
                    text = "${selected.symbol}  ${selected.displayName}",
                    badge = selected.sourceLabel.ifEmpty { null },
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

        if (dropdownState.currentState || dropdownState.targetState) {
            Popup(
                onDismissRequest = { expanded = false },
                alignment = Alignment.TopStart,
                offset = IntOffset(0, anchorHeightPx + with(density) { 4.dp.roundToPx() }),
                properties = PopupProperties(focusable = true),
            ) {
                AnimatedVisibility(
                    visibleState = dropdownState,
                    enter = fadeIn(animationSpec = tween(durationMillis = 150)) +
                        scaleIn(
                            initialScale = 0.5f,
                            transformOrigin = TransformOrigin(0f, 0f),
                            animationSpec = spring(
                                dampingRatio = 0.7f,
                                stiffness = Spring.StiffnessMediumLow,
                            ),
                        ),
                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) +
                        scaleOut(
                            targetScale = 0.5f,
                            transformOrigin = TransformOrigin(0f, 0f),
                            animationSpec = tween(durationMillis = 100),
                        ),
                ) {
                    Surface(
                        shape = RoundedCornerShape(radius.xxl),
                        color = colors.SurfaceVariant,
                        shadowElevation = 8.dp,
                        modifier = Modifier.width(220.dp),
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            ChartDisplayCurrency.entries.forEach { currency ->
                                val isSelected = currency == selected
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onSelect(currency)
                                            expanded = false
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = "${currency.symbol}  ${currency.displayName}",
                                        fontSize = 15.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) colors.Primary else colors.TextPrimary,
                                    )
                                    if (currency.sourceLabel.isNotEmpty()) {
                                        Text(
                                            text = currency.sourceLabel,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isSelected) colors.Primary.copy(alpha = 0.7f) else colors.TextSecondary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CeroFiaoDesign.spacing.sm),
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape),
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = colors.TextSecondary,
            )
        }
        Text(
            text = amount,
            style = MaterialTheme.typography.bodySmall,
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
    val spacing = CeroFiaoDesign.spacing
    val radius = CeroFiaoDesign.radius

    val (badgeBg, badgeText) = when (account.type) {
        AccountType.BANK -> AccountBadgeColors.BankBg to AccountBadgeColors.BankText
        AccountType.CRYPTO_EXCHANGE -> AccountBadgeColors.CryptoBg to AccountBadgeColors.CryptoText
        AccountType.DIGITAL_WALLET -> AccountBadgeColors.WalletBg to AccountBadgeColors.WalletText
        AccountType.CASH -> AccountBadgeColors.CashBg to AccountBadgeColors.CashText
    }

    val typeLabel = when (account.type) {
        AccountType.BANK -> "Banco"
        AccountType.CRYPTO_EXCHANGE -> "Crypto"
        AccountType.DIGITAL_WALLET -> "Billetera"
        AccountType.CASH -> "Efectivo"
    }

    val hasProgress = data.progressRatio != null

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .pressableFeedback(onClick = onClick, variant = FeedbackVariant.ScaleHighlight),
        shape = RoundedCornerShape(radius.xxl),
        color = colors.CardBackground,
    ) {
        Column(modifier = Modifier.padding(spacing.xxl)) {
            // ── Header: type label + name + platform badge ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.xxs),
                    ) {
                        Text(
                            text = "$typeLabel -",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.TextSecondary,
                            letterSpacing = 1.1.sp,
                        )
                        Text(
                            text = account.currencyCode,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = colors.TextSecondary,
                            letterSpacing = 1.1.sp,
                        )
                    }
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.TextPrimary,
                        lineHeight = 28.sp,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(radius.md))
                        .background(badgeBg),
                    contentAlignment = Alignment.Center,
                ) {
                    PlatformIcon(accountType = account.type, tintColor = badgeText)
                }
            }

            Spacer(Modifier.height(if (hasProgress) spacing.huge else spacing.xxxl))

            // ── Balance section ──
            if (hasProgress) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(spacing.xs),
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

                Spacer(Modifier.height(spacing.md))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(CeroFiaoDesign.radius.circle))
                        .background(colors.TextPrimary.copy(alpha = 0.1f)),
                ) {
                    val ratio = data.progressRatio ?: 0f
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(ratio)
                            .height(4.dp)
                            .clip(RoundedCornerShape(CeroFiaoDesign.radius.circle))
                            .background(colors.AccentGreen),
                    )
                }
            } else {
                Text(
                    text = CurrencyFormatter.format(account.balance, account.currencyCode),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.TextPrimary,
                    lineHeight = 32.sp,
                )

                val label = data.lastTransactionLabel
                if (label != null) {
                    Spacer(Modifier.height(spacing.xxs))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.TextSecondary,
                    )
                }
            }
        }
    }
}

// ── Platform Icon ──

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
