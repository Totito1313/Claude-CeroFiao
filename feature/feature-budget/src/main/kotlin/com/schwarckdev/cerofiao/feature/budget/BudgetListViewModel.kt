package com.schwarckdev.cerofiao.feature.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.MoneyCalculator
import com.schwarckdev.cerofiao.core.domain.repository.BudgetRepository
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.model.Budget
import com.schwarckdev.cerofiao.core.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetWithProgress(
    val budget: Budget,
    val category: Category? = null,
    val spentAmount: Double = 0.0,
    val limitAmount: Double = 0.0,
    val currencyCode: String = "USD",
    val progress: Float = 0f,
)

data class BudgetListUiState(
    val budgets: List<BudgetWithProgress> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class BudgetListViewModel @Inject constructor(
    budgetRepository: BudgetRepository,
    categoryRepository: CategoryRepository,
    transactionRepository: TransactionRepository,
    private val resolveExchangeRate: ResolveExchangeRateUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val budgetRepo: BudgetRepository,
) : ViewModel() {

    private val now = DateUtils.now()
    private val startOfMonth = DateUtils.startOfMonth(now)
    private val endOfMonth = DateUtils.endOfMonth(now)

    val uiState: StateFlow<BudgetListUiState> = combine(
        budgetRepository.getActiveBudgets(),
        categoryRepository.getActiveCategories(),
        transactionRepository.getExpensesByCategoryForPeriod(startOfMonth, endOfMonth),
        userPreferencesRepository.userPreferences,
    ) { budgets, categories, categoryExpenses, prefs ->
        val expenseMap = categoryExpenses.toMap()
        val categoryMap = categories.associateBy { it.id }

        val budgetsWithProgress = budgets.map { budget ->
            val category = budget.categoryId?.let { categoryMap[it] }
            val spentInUsd = if (budget.categoryId != null) {
                expenseMap[budget.categoryId] ?: 0.0
            } else {
                categoryExpenses.sumOf { it.second }
            }

            // Convert spent from USD to budget's anchor currency
            val budgetCurrency = budget.anchorCurrencyCode
            val spentInBudgetCurrency = if (budgetCurrency == "USD") {
                spentInUsd
            } else {
                val rate = resolveExchangeRate.fromUsd(budgetCurrency, prefs.preferredRateSource)
                MoneyCalculator.convert(spentInUsd, rate.rate)
            }

            val limit = budget.limitAmount
            val progress = if (limit > 0) (spentInBudgetCurrency / limit).toFloat().coerceIn(0f, 1.5f) else 0f

            BudgetWithProgress(
                budget = budget,
                category = category,
                spentAmount = spentInBudgetCurrency,
                limitAmount = limit,
                currencyCode = budgetCurrency,
                progress = progress,
            )
        }

        BudgetListUiState(
            budgets = budgetsWithProgress,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BudgetListUiState(),
    )

    fun deleteBudget(budgetId: String) {
        viewModelScope.launch {
            budgetRepo.deleteBudget(budgetId)
        }
    }
}
