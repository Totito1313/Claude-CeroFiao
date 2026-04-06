package com.schwarckdev.cerofiao.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.feature.dashboard.components.AccountsSection
import com.schwarckdev.cerofiao.feature.dashboard.components.BudgetsSection
import com.schwarckdev.cerofiao.feature.dashboard.components.CategoriesSection
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
    onNavigateToTransfer: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToExchangeRates: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToAddAccount: () -> Unit,
    onNavigateToAccountDetail: (String) -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToAddBudget: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Home, title = "CeroFiao")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var balanceVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CeroFiaoDesign.colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 110.dp, top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        GlobalBalanceHero(
            totalBalance = uiState.globalBalance?.totalInDisplayCurrency ?: 0.0,
            displayCurrencyCode = uiState.displayCurrencyCode,
            balanceVisible = balanceVisible,
            onToggleVisibility = { balanceVisible = !balanceVisible },
            onExpense = onAddTransaction,
            onTransfer = onNavigateToTransfer,
            onIncome = onAddTransaction,
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            // Quick Actions
            SectionHeader(
                title = "Acciones rápidas",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
            QuickActionsCard(
                onAddTransaction = onAddTransaction,
                onTransfer = onNavigateToTransfer,
                onCategories = onNavigateToCategories,
                onExchangeRates = onNavigateToExchangeRates,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Exchange Rates
            ExchangeRatesSection(
                bcvRate = uiState.bcvRate,
                euriRate = uiState.euriRate,
                bcvEurRate = uiState.bcvEurRate,
                usdtRate = uiState.usdtRate,
                onViewAll = onNavigateToExchangeRates,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Accounts
            AccountsSection(
                accounts = uiState.globalBalance?.breakdownByAccount ?: emptyList(),
                onViewAll = onNavigateToAccounts,
                onAddAccount = onNavigateToAddAccount,
                onAccountClick = { onNavigateToAccountDetail(it) },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Budgets
            BudgetsSection(
                budgets = uiState.budgetsWithSpending,
                onViewAll = onNavigateToBudgets,
                onAddBudget = onNavigateToAddBudget,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Categories
            CategoriesSection(
                categories = uiState.topCategoryExpenses,
                displayCurrencyCode = uiState.displayCurrencyCode,
                onViewAll = onNavigateToCategories,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Recent Transactions
            RecentTransactionsSection(
                transactions = uiState.enrichedTransactions,
                onViewAll = onViewAllTransactions,
                onTransactionClick = onTransactionClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
