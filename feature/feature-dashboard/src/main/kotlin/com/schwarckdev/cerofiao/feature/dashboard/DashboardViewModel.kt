package com.schwarckdev.cerofiao.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetGlobalBalanceUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.GetTransactionsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RefreshExchangeRatesUseCase
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

data class DashboardUiState(
    val globalBalance: GlobalBalance? = null,
    val recentTransactions: List<Transaction> = emptyList(),
    val bcvRate: ExchangeRate? = null,
    val usdtRate: ExchangeRate? = null,
    val bcvEurRate: ExchangeRate? = null,
    val euriRate: ExchangeRate? = null,
    val isRefreshing: Boolean = false,
    val displayCurrencyCode: String = "USD",
    val monthlyExpenses: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val topCategoryExpenses: List<CategoryExpense> = emptyList(),
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getGlobalBalanceUseCase: GetGlobalBalanceUseCase,
    getTransactionsUseCase: GetTransactionsUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private data class RatesSnapshot(
        val bcv: ExchangeRate? = null,
        val usdt: ExchangeRate? = null,
        val bcvEur: ExchangeRate? = null,
        val euri: ExchangeRate? = null,
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

    val uiState: StateFlow<DashboardUiState> = combine(
        getGlobalBalanceUseCase(),
        getTransactionsUseCase.recent(5),
        ratesState,
        isRefreshing,
        monthlySummaryFlow,
    ) { balance, transactions, rates, refreshing, monthlySummary ->
        val (monthlyExpenses, monthlyIncome, categoryExpenses) = monthlySummary
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
        DashboardUiState(
            globalBalance = balance,
            recentTransactions = transactions,
            bcvRate = rates.bcv,
            usdtRate = rates.usdt,
            bcvEurRate = rates.bcvEur,
            euriRate = rates.euri,
            isRefreshing = refreshing,
            displayCurrencyCode = balance?.displayCurrencyCode ?: "USD",
            monthlyExpenses = monthlyExpenses,
            monthlyIncome = monthlyIncome,
            topCategoryExpenses = topCategories,
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
