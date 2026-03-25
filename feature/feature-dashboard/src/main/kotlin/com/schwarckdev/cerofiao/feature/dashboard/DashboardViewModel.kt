package com.schwarckdev.cerofiao.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.BudgetRepository
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetGlobalBalanceUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetTransactionsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RefreshExchangeRatesUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountBalance
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.model.Budget
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.GlobalBalance
import com.schwarckdev.cerofiao.core.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryExpense(
    val categoryName: String,
    val iconName: String,
    val amount: Double,
    val percentage: Float,
)

data class EnrichedTransaction(
    val transaction: Transaction,
    val accountName: String,
    val accountPlatform: AccountPlatform,
    val accountType: AccountType,
    val categoryName: String?,
    val categoryColorHex: String?,
)

data class BudgetWithSpending(
    val budget: Budget,
    val spentAmount: Double,
    val categoryName: String?,
    val categoryColorHex: String?,
)

data class DashboardUiState(
    val globalBalance: GlobalBalance? = null,
    val recentTransactions: List<Transaction> = emptyList(),
    val enrichedTransactions: List<EnrichedTransaction> = emptyList(),
    val bcvRate: ExchangeRate? = null,
    val usdtRate: ExchangeRate? = null,
    val bcvEurRate: ExchangeRate? = null,
    val euriRate: ExchangeRate? = null,
    val isRefreshing: Boolean = false,
    val displayCurrencyCode: String = "USD",
    val monthlyExpenses: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val topCategoryExpenses: List<CategoryExpense> = emptyList(),
    val budgetsWithSpending: List<BudgetWithSpending> = emptyList(),
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getGlobalBalanceUseCase: GetGlobalBalanceUseCase,
    getTransactionsUseCase: GetTransactionsUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    private data class RatesSnapshot(
        val bcv: ExchangeRate? = null,
        val usdt: ExchangeRate? = null,
        val bcvEur: ExchangeRate? = null,
        val euri: ExchangeRate? = null,
    )

    private data class FiveWay(
        val balance: GlobalBalance?,
        val transactions: List<Transaction>,
        val rates: RatesSnapshot,
        val refreshing: Boolean,
        val monthlySummary: Triple<Double, Double, List<Pair<String, Double>>>,
    )

    private val ratesState = MutableStateFlow(RatesSnapshot())
    private val isRefreshing = MutableStateFlow(false)

    private val monthStart = DateUtils.startOfMonth(DateUtils.now())
    private val monthEnd = DateUtils.endOfMonth(DateUtils.now())

    private val monthlySummaryFlow = combine(
        transactionRepository.getTotalExpensesInUsdForPeriod(monthStart, monthEnd),
        transactionRepository.getTotalIncomeInUsdForPeriod(monthStart, monthEnd),
        transactionRepository.getExpensesByCategoryForPeriod(monthStart, monthEnd),
    ) { expenses, income, categoryExpenses ->
        Triple(expenses ?: 0.0, income ?: 0.0, categoryExpenses)
    }

    private val budgetsFlow = budgetRepository.getActiveBudgets()

    private val accountsFlow = accountRepository.getActiveAccounts()

    val uiState: StateFlow<DashboardUiState> = combine(
        combine(
            getGlobalBalanceUseCase(),
            getTransactionsUseCase.recent(5),
            ratesState,
            isRefreshing,
            monthlySummaryFlow,
        ) { balance, transactions, rates, refreshing, monthlySummary ->
            FiveWay(balance, transactions, rates, refreshing, monthlySummary)
        },
        budgetsFlow,
        accountsFlow,
    ) { fiveWay, budgets, accounts ->
        val balance = fiveWay.balance
        val transactions = fiveWay.transactions
        val rates = fiveWay.rates
        val refreshing = fiveWay.refreshing
        val (monthlyExpenses, monthlyIncome, categoryExpenses) = fiveWay.monthlySummary

        val totalExpenses = if (monthlyExpenses > 0.0) monthlyExpenses else 1.0
        val topCategories = categoryExpenses
            .sortedByDescending { it.second }
            .take(5)
            .map { (categoryId, amount) ->
                val category = categoryRepository.getCategoryById(categoryId)
                CategoryExpense(
                    categoryName = category?.name ?: "Sin categoría",
                    iconName = category?.iconName ?: "Category",
                    amount = amount,
                    percentage = (amount / totalExpenses).toFloat(),
                )
            }

        val enriched = transactions.map { tx ->
            val acct = accounts.find { it.id == tx.accountId }
            val cat = categoryRepository.getCategoryById(tx.categoryId ?: "")
            EnrichedTransaction(
                transaction = tx,
                accountName = acct?.name ?: "",
                accountPlatform = acct?.platform ?: AccountPlatform.NONE,
                accountType = acct?.type ?: AccountType.CASH,
                categoryName = cat?.name,
                categoryColorHex = cat?.colorHex,
            )
        }

        val budgetsWithSpending = budgets.map { budget ->
            val cat = budget.categoryId?.let { categoryRepository.getCategoryById(it) }
            val spent = monthlyExpenses * (topCategories.find { it.categoryName == cat?.name }?.percentage?.toDouble() ?: 0.0)
            BudgetWithSpending(
                budget = budget,
                spentAmount = spent,
                categoryName = cat?.name ?: budget.name,
                categoryColorHex = cat?.colorHex,
            )
        }

        DashboardUiState(
            globalBalance = balance,
            recentTransactions = transactions,
            enrichedTransactions = enriched,
            bcvRate = rates.bcv,
            usdtRate = rates.usdt,
            bcvEurRate = rates.bcvEur,
            euriRate = rates.euri,
            isRefreshing = refreshing,
            displayCurrencyCode = balance?.displayCurrencyCode ?: "USD",
            monthlyExpenses = monthlyExpenses,
            monthlyIncome = monthlyIncome,
            topCategoryExpenses = topCategories,
            budgetsWithSpending = budgetsWithSpending,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState(),
    )

    init {
        refreshRates()
    }

    fun refreshRates() {
        viewModelScope.launch {
            isRefreshing.value = true
            try {
                refreshExchangeRatesUseCase()
            } catch (_: Exception) {
                // Offline-first: silently fail
            }
            loadRates()
            isRefreshing.value = false
        }
    }

    private suspend fun loadRates() {
        val bcv = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.BCV)
        val usdt = exchangeRateRepository.getLatestRateBySource("USD", "VES", ExchangeRateSource.USDT)
        val bcvEur = exchangeRateRepository.getLatestRateBySource("EUR", "VES", ExchangeRateSource.BCV)
        val euri = exchangeRateRepository.getLatestRateBySource("EUR", "VES", ExchangeRateSource.EURI)
        ratesState.value = RatesSnapshot(bcv = bcv, usdt = usdt, bcvEur = bcvEur, euri = euri)
    }
}
