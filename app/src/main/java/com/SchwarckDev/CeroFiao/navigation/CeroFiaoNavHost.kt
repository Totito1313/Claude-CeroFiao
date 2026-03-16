package com.SchwarckDev.CeroFiao.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
import com.schwarckdev.cerofiao.feature.settings.settingsScreen
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionEntry
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionList
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransfer
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
        )

        transactionListScreen(
            onAddTransaction = { navController.navigateToTransactionEntry() },
            onTransactionClick = { /* TODO: transaction detail */ },
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
            onAccountClick = { /* TODO: account detail */ },
            onAddAccount = { navController.navigateToAddAccount() },
            onTransfer = { navController.navigateToTransfer() },
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
        )
    }
}
