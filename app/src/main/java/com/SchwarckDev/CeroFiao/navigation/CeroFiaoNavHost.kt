package com.SchwarckDev.CeroFiao.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.schwarckdev.cerofiao.feature.budget.addBudgetScreen
import com.schwarckdev.cerofiao.feature.budget.budgetListScreen
import com.schwarckdev.cerofiao.feature.budget.navigateToAddBudget
import com.schwarckdev.cerofiao.feature.budget.navigateToBudgetList
import com.schwarckdev.cerofiao.feature.debt.addDebtScreen
import com.schwarckdev.cerofiao.feature.debt.debtDetailScreen
import com.schwarckdev.cerofiao.feature.debt.debtListScreen
import com.schwarckdev.cerofiao.feature.debt.navigateToAddDebt
import com.schwarckdev.cerofiao.feature.debt.navigateToDebtDetail
import com.schwarckdev.cerofiao.feature.debt.navigateToDebtList
import com.schwarckdev.cerofiao.feature.accounts.AccountListRoute
import com.schwarckdev.cerofiao.feature.accounts.AddAccountRoute
import com.schwarckdev.cerofiao.feature.accounts.accountDetailScreen
import com.schwarckdev.cerofiao.feature.accounts.accountListScreen
import com.schwarckdev.cerofiao.feature.accounts.addAccountScreen
import com.schwarckdev.cerofiao.feature.accounts.navigateToAccountDetail
import com.schwarckdev.cerofiao.feature.accounts.navigateToAddAccount
import com.schwarckdev.cerofiao.feature.categories.categoryListScreen
import com.schwarckdev.cerofiao.feature.categories.navigateToCategories
import com.schwarckdev.cerofiao.feature.dashboard.DashboardRoute
import com.schwarckdev.cerofiao.feature.dashboard.dashboardScreen
import com.schwarckdev.cerofiao.feature.exchangerates.exchangeRateScreen
import com.schwarckdev.cerofiao.feature.exchangerates.navigateToExchangeRates
import com.schwarckdev.cerofiao.feature.onboarding.OnboardingRoute
import com.schwarckdev.cerofiao.feature.onboarding.onboardingScreen
import com.schwarckdev.cerofiao.feature.settings.csvExportScreen
import com.schwarckdev.cerofiao.feature.settings.navigateToCsvExport
import com.schwarckdev.cerofiao.feature.settings.settingsScreen
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionDetail
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionEntry
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionList
import com.schwarckdev.cerofiao.feature.transactions.navigateToRecurringForm
import com.schwarckdev.cerofiao.feature.transactions.navigateToRecurringList
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionActivity
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransfer
import com.schwarckdev.cerofiao.feature.transactions.recurringFormScreen
import com.schwarckdev.cerofiao.feature.transactions.recurringListScreen
import com.schwarckdev.cerofiao.feature.transactions.transactionActivityScreen
import com.schwarckdev.cerofiao.feature.transactions.transactionDetailScreen
import com.schwarckdev.cerofiao.feature.transactions.transactionEntryScreen
import com.schwarckdev.cerofiao.feature.transactions.transactionListScreen
import com.schwarckdev.cerofiao.feature.transactions.transferScreen

@Composable
fun CeroFiaoNavHost(
    navController: NavHostController,
    hasCompletedOnboarding: Boolean,
    modifier: Modifier = Modifier,
) {
    val startDestination: Any = if (hasCompletedOnboarding) DashboardRoute else OnboardingRoute

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        onboardingScreen(
            onOnboardingComplete = {
                navController.navigate(DashboardRoute) {
                    popUpTo(OnboardingRoute) { inclusive = true }
                }
            },
        )

        dashboardScreen(
            onAddTransaction = { navController.navigateToTransactionEntry() },
            onViewAllTransactions = { navController.navigateToTransactionList() },
            onTransactionClick = { transactionId ->
                navController.navigateToTransactionDetail(transactionId)
            },
        )

        transactionListScreen(
            onAddTransaction = { navController.navigateToTransactionEntry() },
            onTransactionClick = { transactionId ->
                navController.navigateToTransactionDetail(transactionId)
            },
            onActivityClick = { navController.navigateToTransactionActivity() },
        )

        transactionDetailScreen(
            onBack = { navController.popBackStack() },
            onEdit = { transactionId ->
                navController.navigateToTransactionEntry(transactionId)
            },
        )

        transactionEntryScreen(
            onBack = { navController.popBackStack() },
            onSaved = { navController.popBackStack() },
        )

        transferScreen(
            onBack = { navController.popBackStack() },
            onSaved = { navController.popBackStack() },
        )

        accountListScreen(
            onAccountClick = { accountId -> navController.navigateToAccountDetail(accountId) },
            onAddAccount = { navController.navigateToAddAccount() },
            onTransfer = { navController.navigateToTransfer() },
        )

        accountDetailScreen(
            onBack = { navController.popBackStack() },
        )

        addAccountScreen(
            onBack = { navController.popBackStack() },
            onAccountCreated = { navController.popBackStack() },
        )

        categoryListScreen(
            onBack = { navController.popBackStack() },
        )

        exchangeRateScreen(
            onBack = { navController.popBackStack() },
        )

        settingsScreen(
            onNavigateToCategories = { navController.navigateToCategories() },
            onNavigateToExchangeRates = { navController.navigateToExchangeRates() },
            onNavigateToBudgets = { navController.navigateToBudgetList() },
            onNavigateToDebts = { navController.navigateToDebtList() },
            onNavigateToCsvExport = { navController.navigateToCsvExport() },
            onNavigateToRecurring = { navController.navigateToRecurringList() },
        )

        csvExportScreen(
            onBack = { navController.popBackStack() },
        )

        transactionActivityScreen(
            onBack = { navController.popBackStack() },
        )

        recurringListScreen(
            onBack = { navController.popBackStack() },
            onAddRecurring = { navController.navigateToRecurringForm() },
        )

        recurringFormScreen(
            onBack = { navController.popBackStack() },
            onSaved = { navController.popBackStack() },
        )

        budgetListScreen(
            onBack = { navController.popBackStack() },
            onAddBudget = { navController.navigateToAddBudget() },
            onEditBudget = { budgetId -> navController.navigateToAddBudget(budgetId) },
        )

        addBudgetScreen(
            onBack = { navController.popBackStack() },
            onSaved = { navController.popBackStack() },
        )

        debtListScreen(
            onBack = { navController.popBackStack() },
            onAddDebt = { navController.navigateToAddDebt() },
            onDebtClick = { debtId -> navController.navigateToDebtDetail(debtId) },
        )

        addDebtScreen(
            onBack = { navController.popBackStack() },
            onSaved = { navController.popBackStack() },
        )

        debtDetailScreen(
            onBack = { navController.popBackStack() },
        )
    }
}
