package com.schwarckdev.cerofiao.feature.transactions
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import com.schwarckdev.cerofiao.core.ui.CeroFiaoDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.TransactionType
import com.schwarckdev.cerofiao.core.ui.EmptyState
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onAddTransaction: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onActivityClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: TransactionListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).statusBarsPadding(),
    ) {
        if (uiState.groupedTransactions.isEmpty() && uiState.transactions.isEmpty()) {
            // Empty state centered
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            ) {
                // Title still at top
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Movimientos",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = t.text,
                )
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    EmptyState(
                        icon = CeroFiaoIcons.Expense,
                        title = "Sin transacciones",
                        description = "Registra tu primer gasto o ingreso",
                        actionLabel = "Agregar",
                        onAction = onAddTransaction,
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                // Title
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Movimientos",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = t.text,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Summary cards: Ingresos + Gastos
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        // Ingresos card
                        SummaryCard(
                            label = "Ingresos",
                            amount = uiState.totalIncomeUsd,
                            accentColor = t.success,
                            icon = { size ->
                                Icon(
                                    imageVector = CeroFiaoIcons.TrendingUp,
                                    contentDescription = null,
                                    tint = t.success,
                                    modifier = Modifier.size(size),
                                )
                            },
                            modifier = Modifier.weight(1f),
                        )
                        // Gastos card
                        SummaryCard(
                            label = "Gastos",
                            amount = uiState.totalExpenseUsd,
                            accentColor = t.danger,
                            icon = { size ->
                                Icon(
                                    imageVector = CeroFiaoIcons.TrendingDown,
                                    contentDescription = null,
                                    tint = t.danger,
                                    modifier = Modifier.size(size),
                                )
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Search bar + filter button
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Search field
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
                            color = t.inputBg,
                            border = BorderStroke(1.dp, t.inputBorder),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = CeroFiaoIcons.Search,
                                    contentDescription = "Buscar",
                                    tint = t.placeholder,
                                    modifier = Modifier.size(18.dp),
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                BasicTextField(
                                    value = uiState.searchQuery,
                                    onValueChange = viewModel::setSearchQuery,
                                    modifier = Modifier.weight(1f),
                                    textStyle = TextStyle(
                                        fontSize = 14.sp,
                                        color = t.text,
                                    ),
                                    singleLine = true,
                                    cursorBrush = SolidColor(t.text),
                                    decorationBox = { innerTextField ->
                                        Box {
                                            if (uiState.searchQuery.isEmpty()) {
                                                Text(
                                                    text = "Buscar transacción...",
                                                    fontSize = 14.sp,
                                                    color = t.placeholder,
                                                )
                                            }
                                            innerTextField()
                                        }
                                    },
                                )
                            }
                        }

                        // Filter button
                        Surface(
                            shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
                            color = t.inputBg,
                            border = BorderStroke(1.dp, t.inputBorder),
                            modifier = Modifier
                                .size(46.dp)
                                .clickable { onActivityClick() },
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = CeroFiaoIcons.Filter,
                                    contentDescription = "Filtros",
                                    tint = t.textSecondary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Type filter chips
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        TypeFilterChip(
                            label = "Todos",
                            selected = uiState.selectedTypeFilter == null,
                            onClick = { viewModel.setTypeFilter(null) },
                        )
                        TypeFilterChip(
                            label = "Gastos",
                            selected = uiState.selectedTypeFilter == TransactionType.EXPENSE,
                            onClick = {
                                viewModel.setTypeFilter(
                                    if (uiState.selectedTypeFilter == TransactionType.EXPENSE) null
                                    else TransactionType.EXPENSE,
                                )
                            },
                        )
                        TypeFilterChip(
                            label = "Ingresos",
                            selected = uiState.selectedTypeFilter == TransactionType.INCOME,
                            onClick = {
                                viewModel.setTypeFilter(
                                    if (uiState.selectedTypeFilter == TransactionType.INCOME) null
                                    else TransactionType.INCOME,
                                )
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Currency filter chips
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        CurrencyFilterChip(
                            label = "Todas",
                            selected = uiState.selectedCurrencyFilter == null,
                            onClick = { viewModel.setCurrencyFilter(null) },
                        )
                        CurrencyFilterChip(
                            label = "USD",
                            selected = uiState.selectedCurrencyFilter == "USD",
                            onClick = {
                                viewModel.setCurrencyFilter(
                                    if (uiState.selectedCurrencyFilter == "USD") null else "USD",
                                )
                            },
                        )
                        CurrencyFilterChip(
                            label = "VES",
                            selected = uiState.selectedCurrencyFilter == "VES",
                            onClick = {
                                viewModel.setCurrencyFilter(
                                    if (uiState.selectedCurrencyFilter == "VES") null else "VES",
                                )
                            },
                        )
                        CurrencyFilterChip(
                            label = "EUR",
                            selected = uiState.selectedCurrencyFilter == "EUR",
                            onClick = {
                                viewModel.setCurrencyFilter(
                                    if (uiState.selectedCurrencyFilter == "EUR") null else "EUR",
                                )
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Date groups
                uiState.groupedTransactions.forEach { group ->
                    // Date header with day net total
                    item(key = "header_${group.dateMillis}") {
                        DateGroupHeader(
                            dateMillis = group.dateMillis,
                            dayNetUsd = group.dayNetUsd,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // GlassCard wrapping transaction rows
                    item(key = "card_${group.dateMillis}") {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            padding = GlassCardPadding.None,
                        ) {
                            Column {
                                group.transactions.forEachIndexed { index, twc ->
                                    SwipeableTransactionRow(
                                        twc = twc,
                                        onClick = { onTransactionClick(twc.transaction.id) },
                                        onDelete = { viewModel.deleteTransaction(twc.transaction.id) },
                                    )
                                    // Divider between items (not after last)
                                    if (index < group.transactions.lastIndex) {
                                        CeroFiaoDivider(modifier = Modifier.padding(horizontal = 16.dp))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Bottom padding for floating nav bar
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

// ─── Summary Card ────────────────────────────────────────────────────────────

@Composable
private fun SummaryCard(
    label: String,
    amount: Double,
    accentColor: Color,
    icon: @Composable (size: androidx.compose.ui.unit.Dp) -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    GlassCard(
        modifier = modifier,
        padding = GlassCardPadding.Medium,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                icon(16.dp)
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = t.textSecondary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = CurrencyFormatter.format(amount, "USD"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
            )
        }
    }
}

// ─── Filter Chips ────────────────────────────────────────────────────────────

private val GreenChipSelectedBg = Color(0x1400FF66)        // rgba(0,255,102,0.08)
private val GreenChipSelectedBorder = Color(0x2600FF66)    // rgba(0,255,102,0.15)
private val GreenChipSelectedText = Color(0xFF00FF66)

private val PurpleChipSelectedBg = Color(0x148A2BE2)       // rgba(138,43,226,0.08)
private val PurpleChipSelectedBorder = Color(0x268A2BE2)   // rgba(138,43,226,0.15)
private val PurpleChipSelectedText = Color(0xFF8A2BE2)

@Composable
private fun TypeFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val bgColor = if (selected) GreenChipSelectedBg else t.pillBg
    val borderColor = if (selected) GreenChipSelectedBorder else Color.Transparent
    val textColor = if (selected) GreenChipSelectedText else t.textSecondary

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(CeroFiaoShapes.ChipRadius))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CeroFiaoShapes.ChipRadius),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun CurrencyFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val bgColor = if (selected) PurpleChipSelectedBg else t.pillBg
    val borderColor = if (selected) PurpleChipSelectedBorder else Color.Transparent
    val textColor = if (selected) PurpleChipSelectedText else t.textSecondary

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(CeroFiaoShapes.ChipRadius))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CeroFiaoShapes.ChipRadius),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

// ─── Date Group Header ───────────────────────────────────────────────────────

@Composable
private fun DateGroupHeader(
    dateMillis: Long,
    dayNetUsd: Double,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val dateLabel = formatDateGroupLabel(dateMillis)
    val netColor = when {
        dayNetUsd > 0 -> t.success
        dayNetUsd < 0 -> t.danger
        else -> t.textMuted
    }
    val netSign = if (dayNetUsd >= 0) "+" else ""

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dateLabel,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = t.textSecondary,
        )
        Text(
            text = "$netSign${CurrencyFormatter.format(dayNetUsd, "USD")}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = netColor,
        )
    }
}

/**
 * Formats a date millis to a human-readable Spanish label.
 * "Hoy", "Ayer", or "lunes, 17 de marzo" for older dates.
 */
private fun formatDateGroupLabel(dateMillis: Long): String {
    if (DateUtils.isToday(dateMillis)) return "Hoy"

    // Check yesterday by comparing start-of-day millis
    val todayStart = DateUtils.startOfDay(DateUtils.now())
    val yesterdayStart = todayStart - 86_400_000L
    if (dateMillis in yesterdayStart until todayStart) return "Ayer"

    // Full Spanish format: "lunes, 17 de marzo"
    val sdf = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
    return sdf.format(Date(dateMillis))
}

// ─── Swipeable Transaction Row ───────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableTransactionRow(
    twc: TransactionWithCategory,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(t.danger.copy(alpha = 0.15f)),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = CeroFiaoIcons.Delete,
                    contentDescription = "Eliminar",
                    tint = t.danger,
                    modifier = Modifier.padding(end = 20.dp),
                )
            }
        },
    ) {
        TransactionRow(
            twc = twc,
            onClick = onClick,
        )
    }
}

// ─── Transaction Row ─────────────────────────────────────────────────────────

@Composable
private fun TransactionRow(
    twc: TransactionWithCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val tx = twc.transaction

    // Parse category color
    val categoryColor = try {
        Color(android.graphics.Color.parseColor(twc.categoryColorHex))
    } catch (_: Exception) {
        t.textSecondary
    }

    // Amount color and sign/icon
    val isIncome = tx.type == TransactionType.INCOME
    val amountColor = if (isIncome) t.success else t.expense
    val amountSign = if (isIncome) "+" else "-"
    val arrowIcon = if (isIncome) CeroFiaoIcons.ArrowIncome else CeroFiaoIcons.ArrowExpense
    val arrowTint = if (isIncome) t.success else t.danger

    // Category icon
    val categoryIconRes = CeroFiaoIcons.getCategoryIconRes(twc.categoryIconName)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Category icon box (44dp, rounded 14dp)
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(14.dp),
                color = t.iconBg,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = categoryIconRes),
                        contentDescription = twc.categoryName,
                        tint = categoryColor,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Description + subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.note ?: twc.categoryName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = t.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = twc.categoryName,
                        fontSize = 11.sp,
                        color = t.textMuted,
                        maxLines = 1,
                    )
                    if (twc.accountName.isNotBlank()) {
                        Text(
                            text = " \u00B7 ",
                            fontSize = 11.sp,
                            color = t.textMuted,
                        )
                        Text(
                            text = twc.accountName,
                            fontSize = 11.sp,
                            color = t.textMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Amount with arrow icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = null,
                    tint = arrowTint,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = "$amountSign${CurrencyFormatter.format(tx.amount, tx.currencyCode)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = amountColor,
                )
            }
        }
    }
}

