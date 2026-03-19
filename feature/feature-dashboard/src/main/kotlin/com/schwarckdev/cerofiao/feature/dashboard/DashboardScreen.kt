package com.schwarckdev.cerofiao.feature.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.CurrencyBalance
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.GlobalBalance
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    onViewAllTransactions: () -> Unit,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens
    var balanceVisible by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize().background(t.bg)) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refreshRates,
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp),
            ) {
                // ── Header ──
                item {
                    DashboardHeader(
                        usdVesRate = uiState.bcvRate?.rate ?: uiState.usdtRate?.rate,
                    )
                }

                // ── Global Balance ──
                item {
                    GlobalBalanceSection(
                        balance = uiState.globalBalance,
                        balanceVisible = balanceVisible,
                        onToggleVisibility = { balanceVisible = !balanceVisible },
                        monthlyIncome = uiState.monthlyIncome,
                        monthlyExpenses = uiState.monthlyExpenses,
                    )
                }

                // ── Quick Actions ──
                item {
                    QuickActionsRow()
                }

                // ── Pocket Cards (Accounts) ──
                val breakdown = uiState.globalBalance?.breakdownByCurrency.orEmpty()
                if (breakdown.isNotEmpty()) {
                    item {
                        PocketCardsSection(
                            breakdown = breakdown,
                            balanceVisible = balanceVisible,
                        )
                    }
                }

                // ── Exchange Rate Banners ──
                if (uiState.bcvRate != null || uiState.usdtRate != null) {
                    item {
                        ExchangeRateBanner(
                            bcvRate = uiState.bcvRate,
                            usdtRate = uiState.usdtRate,
                        )
                    }
                }

                // ── Budgets / Top Categories ──
                if (uiState.topCategoryExpenses.isNotEmpty()) {
                    item {
                        BudgetsSection(
                            categoryExpenses = uiState.topCategoryExpenses,
                        )
                    }
                }

                // ── Recent Transactions ──
                item {
                    SectionHeader(
                        title = "Recientes",
                        actionLabel = "Ver todo",
                        onAction = onViewAllTransactions,
                    )
                }

                if (uiState.recentTransactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp, horizontal = 20.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Sin transacciones aún",
                                fontSize = 14.sp,
                                color = t.textFaint,
                            )
                        }
                    }
                } else {
                    item {
                        GlassCard(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            padding = GlassCardPadding.None,
                        ) {
                            Column {
                                uiState.recentTransactions.forEachIndexed { index, transaction ->
                                    TransactionRow(
                                        transaction = transaction,
                                        onClick = { onTransactionClick(transaction.id) },
                                    )
                                    if (index < uiState.recentTransactions.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            thickness = 1.dp,
                                            color = t.divider,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── FAB ──
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(BrandGradient)
                .clickable(onClick = onAddTransaction),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = CeroFiaoIcons.Add,
                contentDescription = "Agregar transacción",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

// ── Header ──

@Composable
private fun DashboardHeader(usdVesRate: Double?) {
    val t = CeroFiaoTheme.tokens
    val greeting = remember {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "Buenos días"
            hour < 18 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(BrandGradient),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "CF",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = greeting,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = t.textTertiary,
            )
            Text(
                text = "CeroFiao",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = t.text,
            )
        }

        // Rate pill
        if (usdVesRate != null) {
            Surface(
                shape = CircleShape,
                color = t.pillBg,
                border = BorderStroke(1.dp, t.surfaceBorder),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(t.success),
                    )
                    Text(
                        text = "$1 = Bs.${String.format("%.2f", usdVesRate)}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = t.textSecondary,
                    )
                }
            }
        }
    }
}

// ── Global Balance ──

@Composable
private fun GlobalBalanceSection(
    balance: GlobalBalance?,
    balanceVisible: Boolean,
    onToggleVisibility: () -> Unit,
    monthlyIncome: Double,
    monthlyExpenses: Double,
) {
    val t = CeroFiaoTheme.tokens
    val totalDisplay = balance?.totalInDisplayCurrency ?: 0.0
    val currencyCode = balance?.displayCurrencyCode ?: "USD"
    val symbol = if (currencyCode == "VES") "Bs." else "$"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Label + eye toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Balance Total",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = t.textTertiary,
            )
            Icon(
                imageVector = if (balanceVisible) CeroFiaoIcons.Eye else CeroFiaoIcons.EyeOff,
                contentDescription = "Toggle visibility",
                modifier = Modifier
                    .size(13.dp)
                    .clickable(onClick = onToggleVisibility),
                tint = t.textFaint,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Balance amount
        Text(
            text = if (balanceVisible) {
                "$symbol${CurrencyFormatter.format(totalDisplay, currencyCode)}"
            } else {
                "••••••"
            },
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = t.text,
            letterSpacing = (-1.44).sp,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Income / Expense pills
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Income pill
            Surface(
                shape = CircleShape,
                color = t.success.copy(alpha = 0.06f),
                border = BorderStroke(1.dp, t.success.copy(alpha = 0.12f)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = CeroFiaoIcons.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = t.success,
                    )
                    Text(
                        text = if (balanceVisible) "$${monthlyIncome.toLong()}" else "••••",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = t.success,
                    )
                }
            }

            // Expense pill
            Surface(
                shape = CircleShape,
                color = t.danger.copy(alpha = 0.06f),
                border = BorderStroke(1.dp, t.danger.copy(alpha = 0.12f)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = CeroFiaoIcons.TrendingDown,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = t.danger,
                    )
                    Text(
                        text = if (balanceVisible) "$${monthlyExpenses.toLong()}" else "••••",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = t.danger,
                    )
                }
            }
        }
    }
}

// ── Quick Actions ──

@Composable
private fun QuickActionsRow() {
    val t = CeroFiaoTheme.tokens
    val actions = listOf(
        CeroFiaoIcons.Transfer to "Transferir",
        CeroFiaoIcons.ExchangeRate to "Cambiar",
        CeroFiaoIcons.Savings to "Ahorrar",
        CeroFiaoIcons.Analytics to "Analytics",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        actions.forEach { (icon, label) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = t.surface,
                    border = BorderStroke(1.dp, t.surfaceBorder),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = t.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = t.textTertiary,
                )
            }
        }
    }
}

// ── Pocket Cards ──

@Composable
private fun PocketCardsSection(
    breakdown: List<CurrencyBalance>,
    balanceVisible: Boolean,
) {
    val t = CeroFiaoTheme.tokens
    val colors = listOf(
        t.success, Color(0xFF8A2BE2), Color(0xFFFF6B00),
        Color(0xFF00D4FF), Color(0xFFF0B90B),
    )

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Bolsillos",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = t.text,
            )
            Text(
                text = "Ver cuentas",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = t.textMuted,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(breakdown) { index, currency ->
                val cardColor = colors[index % colors.size]
                PocketCard(
                    currency = currency,
                    color = cardColor,
                    balanceVisible = balanceVisible,
                )
            }
        }
    }
}

@Composable
private fun PocketCard(
    currency: CurrencyBalance,
    color: Color,
    balanceVisible: Boolean,
) {
    val t = CeroFiaoTheme.tokens
    val symbol = if (currency.currencyCode == "VES") "Bs." else "$"

    Surface(
        modifier = Modifier
            .width(190.dp)
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.04f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.10f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = CeroFiaoIcons.DigitalWallet,
                    contentDescription = null,
                    tint = color.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.07f),
                    border = BorderStroke(1.dp, color.copy(alpha = 0.10f)),
                ) {
                    Text(
                        text = currency.currencyCode,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = color,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
            }

            Column {
                Text(
                    text = currency.currencyCode,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = t.textTertiary,
                )
                Text(
                    text = if (balanceVisible) {
                        "$symbol${CurrencyFormatter.format(currency.totalInOriginalCurrency, currency.currencyCode)}"
                    } else "••••",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = t.text,
                    letterSpacing = (-0.34).sp,
                )
            }
        }
    }
}

// ── Exchange Rate Banner ──

@Composable
private fun ExchangeRateBanner(
    bcvRate: ExchangeRate?,
    usdtRate: ExchangeRate?,
) {
    val t = CeroFiaoTheme.tokens

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (bcvRate != null) {
            GlassCard(
                modifier = Modifier.weight(1f),
                padding = GlassCardPadding.Small,
            ) {
                Column {
                    Text(
                        text = "BCV",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = t.textFaint,
                    )
                    Text(
                        text = "Bs.${String.format("%.2f", bcvRate.rate)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = t.text,
                    )
                    Text(
                        text = "Bs/USD",
                        fontSize = 10.sp,
                        color = t.textMuted,
                    )
                }
            }
        }
        if (usdtRate != null) {
            GlassCard(
                modifier = Modifier.weight(1f),
                padding = GlassCardPadding.Small,
            ) {
                Column {
                    Text(
                        text = "USDT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = t.textFaint,
                    )
                    Text(
                        text = "Bs.${String.format("%.2f", usdtRate.rate)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = t.text,
                    )
                    Text(
                        text = "Bs/USD",
                        fontSize = 10.sp,
                        color = t.textMuted,
                    )
                }
            }
        }
    }
}

// ── Budgets Section ──

@Composable
private fun BudgetsSection(categoryExpenses: List<CategoryExpense>) {
    val t = CeroFiaoTheme.tokens

    GlassCard(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        padding = GlassCardPadding.None,
    ) {
        Column {
            SectionHeader(
                title = "Presupuestos",
                actionLabel = "Ver todo",
                onAction = {},
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                categoryExpenses.forEach { expense ->
                    BudgetRow(expense = expense)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun BudgetRow(expense: CategoryExpense) {
    val t = CeroFiaoTheme.tokens
    val pct = expense.percentage.coerceIn(0f, 1f)
    val barColor = when {
        pct > 0.8f -> t.danger
        pct > 0.5f -> Color(0xFFFF6B00)  // orange warning — intentional brand color
        else -> t.success
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(CeroFiaoIcons.getCategoryIconRes(expense.iconName)),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = t.textSecondary,
                )
                Text(
                    text = expense.categoryName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = t.text,
                )
            }
            Text(
                text = "$${expense.amount.toLong()}",
                fontSize = 12.sp,
                color = t.textTertiary,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { pct },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = t.progressBg,
        )
    }
}

// ── Transaction Row ──

@Composable
private fun TransactionRow(
    transaction: Transaction,
    onClick: () -> Unit,
) {
    val t = CeroFiaoTheme.tokens
    val isIncome = transaction.type == TransactionType.INCOME
    val amountColor = if (isIncome) t.success else t.expense
    val sign = if (isIncome) "+" else "-"
    val symbol = if (transaction.currencyCode == "VES") "Bs." else "$"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(14.dp),
            color = t.iconBg,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = when (transaction.type) {
                        TransactionType.INCOME -> CeroFiaoIcons.ArrowIncome
                        TransactionType.EXPENSE -> CeroFiaoIcons.ArrowExpense
                        TransactionType.TRANSFER -> CeroFiaoIcons.Transfer
                    },
                    contentDescription = null,
                    modifier = Modifier.size(17.dp),
                    tint = t.textSecondary,
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.note ?: transaction.type.name.lowercase()
                    .replaceFirstChar { it.uppercase() },
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = t.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = transaction.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 11.sp,
                    color = t.textMuted,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = if (isIncome) CeroFiaoIcons.ArrowIncome else CeroFiaoIcons.ArrowExpense,
                contentDescription = null,
                modifier = Modifier.size(11.dp),
                tint = if (isIncome) t.success else t.danger,
            )
            Text(
                text = "$sign$symbol${CurrencyFormatter.format(transaction.amount, transaction.currencyCode)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = amountColor,
            )
        }
    }
}

// ── Section Header ──

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
) {
    val t = CeroFiaoTheme.tokens

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = t.text,
        )
        Row(
            modifier = Modifier.clickable(onClick = onAction),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = actionLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = t.success,
            )
            Icon(
                imageVector = CeroFiaoIcons.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = t.success,
            )
        }
    }
}
