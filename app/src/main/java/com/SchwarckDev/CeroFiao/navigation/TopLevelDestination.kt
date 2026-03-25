package com.SchwarckDev.CeroFiao.navigation

import com.schwarckdev.cerofiao.feature.dashboard.DashboardRoute
import com.schwarckdev.cerofiao.feature.debt.DebtListRoute
import com.schwarckdev.cerofiao.feature.transactions.TransactionListRoute

enum class TopLevelDestination(val route: Any) {
    DASHBOARD(DashboardRoute),
    TRANSACTIONS(TransactionListRoute),
    CEROFIAO(DebtListRoute),
    MORE(MoreRoute),
}
