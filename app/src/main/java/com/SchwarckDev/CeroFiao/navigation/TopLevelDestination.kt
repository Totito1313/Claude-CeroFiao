package com.SchwarckDev.CeroFiao.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.feature.accounts.AccountListRoute
import com.schwarckdev.cerofiao.feature.dashboard.DashboardRoute
import com.schwarckdev.cerofiao.feature.settings.SettingsRoute

enum class TopLevelDestination(
    val label: String,
    val icon: ImageVector,
    val route: Any,
) {
    DASHBOARD(
        label = "Inicio",
        icon = CeroFiaoIcons.Dashboard,
        route = DashboardRoute,
    ),
    ACCOUNTS(
        label = "Cuentas",
        icon = CeroFiaoIcons.Accounts,
        route = AccountListRoute,
    ),
    SETTINGS(
        label = "Ajustes",
        icon = CeroFiaoIcons.Settings,
        route = SettingsRoute,
    ),
}
