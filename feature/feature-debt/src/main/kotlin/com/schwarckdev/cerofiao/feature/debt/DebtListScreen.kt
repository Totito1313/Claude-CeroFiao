package com.schwarckdev.cerofiao.feature.debt
import com.schwarckdev.cerofiao.core.ui.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtType
import com.schwarckdev.cerofiao.core.ui.EmptyState
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtListScreen(
    onAddDebt: () -> Unit,
    onDebtClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DebtListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens

    // Initialize with THEY_OWE if still on ALL
    LaunchedEffect(Unit) {
        if (uiState.selectedFilter == DebtFilter.ALL) {
            viewModel.setFilter(DebtFilter.THEY_OWE)
        }
    }

    val isTheyOwe = uiState.selectedFilter == DebtFilter.THEY_OWE
    val accentColor = if (isTheyOwe) t.success else t.danger

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).padding(top = 90.dp),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 100.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header: Title + Subtitle
            item(key = "header") {
                Column {
                    Text(
                        text = "CeroFiao",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = t.text,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Gestiona tus deudas y cobros",
                        fontSize = 13.sp,
                        color = t.textMuted,
                    )
                }
            }

            // Segmented Tab Bar
            item(key = "tabs") {
                DebtSegmentedTabs(
                    selectedFilter = uiState.selectedFilter,
                    onFilterChange = { viewModel.setFilter(it) },
                )
            }

            // Summary Card
            item(key = "summary") {
                DebtSummaryGlassCard(
                    isTheyOwe = isTheyOwe,
                    totals = if (isTheyOwe) uiState.theyOweTotals else uiState.iOweTotals,
                    debts = uiState.debts,
                    accentColor = accentColor,
                )
            }

            // Debt Cards or Empty State
            if (uiState.debts.isEmpty() && !uiState.isLoading) {
                item(key = "empty") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        EmptyState(
                            icon = CeroFiaoIcons.HandCoins,
                            title = if (isTheyOwe) "Sin cobros pendientes" else "Sin deudas pendientes",
                            description = if (isTheyOwe) {
                                "Registra lo que te deben"
                            } else {
                                "Registra lo que debes"
                            },
                            actionLabel = if (isTheyOwe) "Agregar cobro" else "Agregar deuda",
                            onAction = onAddDebt,
                        )
                    }
                }
            } else {
                items(
                    items = uiState.debts,
                    key = { it.id },
                ) { debt ->
                    val dismissState = rememberSwipeToDismissBoxState()

                    LaunchedEffect(dismissState.currentValue) {
                        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                            viewModel.deleteDebt(debt.id)
                        }
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(CeroFiaoShapes.CardRadius))
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
                        DebtItemCard(
                            debt = debt,
                            isTheyOwe = isTheyOwe,
                            accentColor = accentColor,
                            onClick = { onDebtClick(debt.id) },
                        )
                    }
                }
            }

            // Add button (dashed border)
            item(key = "add_button") {
                AddDebtButton(
                    isTheyOwe = isTheyOwe,
                    accentColor = accentColor,
                    onClick = onAddDebt,
                )
            }
        }
    }
}

@Composable
private fun DebtSegmentedTabs(
    selectedFilter: DebtFilter,
    onFilterChange: (DebtFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CeroFiaoShapes.ChipRadius),
        color = t.pillBg,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            // "Por Cobrar" tab
            val theyOweSelected = selectedFilter == DebtFilter.THEY_OWE
            val theyOweBg by animateColorAsState(
                targetValue = if (theyOweSelected) t.success.copy(alpha = 0.12f) else Color.Transparent,
                animationSpec = tween(200),
                label = "theyOweBg",
            )
            val theyOweTextColor by animateColorAsState(
                targetValue = if (theyOweSelected) t.success else t.textMuted,
                animationSpec = tween(200),
                label = "theyOweText",
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(CeroFiaoShapes.ChipRadius - 4.dp))
                    .background(theyOweBg)
                    .clickable { onFilterChange(DebtFilter.THEY_OWE) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Por Cobrar",
                    fontSize = 14.sp,
                    fontWeight = if (theyOweSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = theyOweTextColor,
                )
            }

            // "Por Pagar" tab
            val iOweSelected = selectedFilter == DebtFilter.I_OWE
            val iOweBg by animateColorAsState(
                targetValue = if (iOweSelected) t.danger.copy(alpha = 0.12f) else Color.Transparent,
                animationSpec = tween(200),
                label = "iOweBg",
            )
            val iOweTextColor by animateColorAsState(
                targetValue = if (iOweSelected) t.danger else t.textMuted,
                animationSpec = tween(200),
                label = "iOweText",
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(CeroFiaoShapes.ChipRadius - 4.dp))
                    .background(iOweBg)
                    .clickable { onFilterChange(DebtFilter.I_OWE) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Por Pagar",
                    fontSize = 14.sp,
                    fontWeight = if (iOweSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = iOweTextColor,
                )
            }
        }
    }
}

@Composable
private fun DebtSummaryGlassCard(
    isTheyOwe: Boolean,
    totals: List<CurrencyTotal>,
    debts: List<Debt>,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val pendingCount = debts.count { !it.isSettled }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        padding = GlassCardPadding.Large,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header row: icon + label
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.12f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = CeroFiaoIcons.HandCoins,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = accentColor,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isTheyOwe) "TOTAL POR COBRAR" else "TOTAL POR PAGAR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = t.textMuted,
                    letterSpacing = 1.2.sp,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Large amount
            if (totals.isEmpty()) {
                Text(
                    text = "$0.00",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = t.text,
                )
            } else {
                totals.forEachIndexed { index, total ->
                    Text(
                        text = CurrencyFormatter.format(total.amount, total.currencyCode),
                        fontSize = if (index == 0) 40.sp else 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (index == 0) t.text else t.textSecondary,
                    )
                    if (index < totals.lastIndex) {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Pending count
            Text(
                text = "$pendingCount ${if (isTheyOwe) "cobros" else "deudas"} pendientes",
                fontSize = 12.sp,
                color = t.textMuted,
            )
        }
    }
}

@Composable
private fun DebtItemCard(
    debt: Debt,
    isTheyOwe: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val progress = if (debt.originalAmount > 0) {
        ((debt.originalAmount - debt.remainingAmount) / debt.originalAmount).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "progress",
    )
    val isCompleted = debt.isSettled
    val hasPayments = debt.remainingAmount < debt.originalAmount && !isCompleted

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        padding = GlassCardPadding.Medium,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar circle with initials
                val initials = debt.personName
                    .split(" ")
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
                val avatarGradient = if (isTheyOwe) {
                    Brush.linearGradient(
                        colors = listOf(accentColor, accentColor.copy(alpha = 0.6f)),
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(accentColor, accentColor.copy(alpha = 0.6f)),
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(avatarGradient),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = initials,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name + description
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = debt.personName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = t.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    val note = debt.note
                    Text(
                        text = if (!note.isNullOrBlank()) note else {
                            if (isTheyOwe) "Te debe" else "Le debes"
                        },
                        fontSize = 11.sp,
                        color = t.textMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Amount + remaining status
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyFormatter.format(debt.remainingAmount, debt.currencyCode),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = t.text,
                    )
                    if (debt.remainingAmount != debt.originalAmount) {
                        Text(
                            text = "de ${CurrencyFormatter.format(debt.originalAmount, debt.currencyCode)}",
                            fontSize = 11.sp,
                            color = t.textMuted,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(t.progressBg),
            ) {
                if (animatedProgress > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        accentColor,
                                        accentColor.copy(alpha = 0.7f),
                                    ),
                                ),
                            )
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(4.dp),
                                ambientColor = accentColor.copy(alpha = 0.3f),
                                spotColor = accentColor.copy(alpha = 0.3f),
                            ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress percentage + status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${(progress * 100).toInt()}% completado",
                    fontSize = 11.sp,
                    color = t.textMuted,
                )

                // Status badge
                when {
                    isCompleted -> {
                        StatusBadge(
                            text = "Completado",
                            bgColor = t.success.copy(alpha = 0.12f),
                            textColor = t.success,
                        )
                    }
                    hasPayments -> {
                        StatusBadge(
                            text = "En progreso",
                            bgColor = Color(0xFF9B8CE8).copy(alpha = 0.15f),
                            textColor = Color(0xFFB8A9F0),
                        )
                    }
                    else -> {
                        StatusBadge(
                            text = "Sin abono",
                            bgColor = Color(0xFFFF9800).copy(alpha = 0.12f),
                            textColor = Color(0xFFFF9800),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Main action button (gradient)
                val buttonGradient = Brush.horizontalGradient(
                    colors = listOf(accentColor, accentColor.copy(alpha = 0.75f)),
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(CeroFiaoShapes.ButtonRadius))
                        .background(buttonGradient)
                        .clickable { onClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (isCompleted) {
                            "Ver detalle"
                        } else if (isTheyOwe) {
                            "Cobrar Ahora"
                        } else {
                            "Pagar Ahora"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }

                // Bell notification button (only for receivables)
                if (isTheyOwe && !isCompleted) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = t.pillBg,
                    ) {
                        IconButton(
                            onClick = { onClick() },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(
                                imageVector = CeroFiaoIcons.Bell,
                                contentDescription = "Recordar",
                                modifier = Modifier.size(16.dp),
                                tint = t.textSecondary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }
}

@Composable
private fun AddDebtButton(
    isTheyOwe: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(CeroFiaoShapes.CardRadius))
            .border(
                border = BorderStroke(
                    width = 1.5.dp,
                    color = accentColor.copy(alpha = 0.3f),
                ),
                shape = RoundedCornerShape(CeroFiaoShapes.CardRadius),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = CeroFiaoIcons.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = accentColor.copy(alpha = 0.6f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isTheyOwe) "Agregar cobro" else "Agregar deuda",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = accentColor.copy(alpha = 0.6f),
            )
        }
    }
}

