package com.schwarckdev.cerofiao.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.feature.dashboard.components.AccountsSection
import com.schwarckdev.cerofiao.feature.dashboard.components.BudgetsSection
import com.schwarckdev.cerofiao.feature.dashboard.components.CategoriesSection
import com.schwarckdev.cerofiao.feature.dashboard.components.DashboardTopBar
import com.schwarckdev.cerofiao.feature.dashboard.components.ExchangeRatesSection
import com.schwarckdev.cerofiao.feature.dashboard.components.GlobalBalanceHero
import com.schwarckdev.cerofiao.feature.dashboard.components.QuickActionsCard
import com.schwarckdev.cerofiao.feature.dashboard.components.RecentTransactionsSection
import com.schwarckdev.cerofiao.feature.dashboard.components.SectionHeader

@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    onViewAllTransactions: () -> Unit,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var balanceVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F1F3))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 110.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DashboardTopBar(userName = "Alan Schwarck")

        GlobalBalanceHero(
            totalBalance = uiState.globalBalance?.totalInDisplayCurrency ?: 0.0,
            displayCurrencyCode = uiState.displayCurrencyCode,
            balanceVisible = balanceVisible,
            onToggleVisibility = { balanceVisible = !balanceVisible },
            onExpense = onAddTransaction,
            onTransfer = { /* TODO: navigate to transfer */ },
            onIncome = onAddTransaction,
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Quick Actions
            SectionHeader(title = "Acciones rápidas")
            Spacer(Modifier.height(16.dp))
            QuickActionsCard(
                onAddTransaction = onAddTransaction,
                onTransfer = { /* TODO */ },
                onCategories = { /* TODO */ },
                onExchangeRates = { /* TODO */ },
            )

            Spacer(Modifier.height(16.dp))

            // Exchange Rates
            ExchangeRatesSection(
                bcvRate = uiState.bcvRate,
                euriRate = uiState.euriRate,
                bcvEurRate = uiState.bcvEurRate,
                usdtRate = uiState.usdtRate,
                onViewAll = { /* TODO: navigate to exchange rates */ },
            )

            Spacer(Modifier.height(16.dp))

            // Accounts
            AccountsSection(
                accounts = uiState.globalBalance?.breakdownByAccount ?: emptyList(),
                onViewAll = { /* TODO: navigate to accounts */ },
                onAddAccount = { /* TODO */ },
                onAccountClick = { /* TODO */ },
            )

            Spacer(Modifier.height(16.dp))

            // Budgets
            BudgetsSection(
                budgets = uiState.budgetsWithSpending,
                onViewAll = { /* TODO */ },
                onAddBudget = { /* TODO */ },
            )

            Spacer(Modifier.height(16.dp))

            // Categories
            CategoriesSection(
                categories = uiState.topCategoryExpenses,
                displayCurrencyCode = uiState.displayCurrencyCode,
                onViewAll = { /* TODO */ },
            )

            Spacer(Modifier.height(16.dp))

            // Recent Transactions
            RecentTransactionsSection(
                transactions = uiState.enrichedTransactions,
                onViewAll = onViewAllTransactions,
                onTransactionClick = onTransactionClick,
            )
        }
    }
}
