package com.SchwarckDev.CeroFiao.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.feature.dashboard.DashboardRoute
import com.schwarckdev.cerofiao.feature.debt.DebtListRoute
import com.schwarckdev.cerofiao.feature.transactions.TransactionListRoute

/**
 * Figma V4 navigation: 4 tabs — Inicio, Movimientos, CeroFiao, Más
 */
enum class TopLevelDestination(
    val label: String,
    val icon: ImageVector,
    val route: Any,
) {
    DASHBOARD(
        label = "Inicio",
        icon = CeroFiaoIcons.NavHome,
        route = DashboardRoute,
    ),
    TRANSACTIONS(
        label = "Movimientos",
        icon = CeroFiaoIcons.NavTransactions,
        route = TransactionListRoute,
    ),
    CEROFIAO(
        label = "CeroFiao",
        icon = CeroFiaoIcons.Debt,
        route = DebtListRoute,
    ),
    MORE(
        label = "Más",
        icon = CeroFiaoIcons.More,
        route = MoreRoute,
    ),
}
