package com.schwarckdev.cerofiao.feature.budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.BudgetRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetCategoriesUseCase
import com.schwarckdev.cerofiao.core.model.Budget
import com.schwarckdev.cerofiao.core.model.BudgetPeriod
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddBudgetUiState(
    val name: String = "",
    val limitAmount: String = "",
    val currencyCode: String = "USD",
    val period: BudgetPeriod = BudgetPeriod.MONTHLY,
    val selectedCategoryId: String? = null,
    val categories: List<Category> = emptyList(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
)

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<AddBudgetRoute>()
    private val editBudgetId: String? = route.budgetId
    private var originalBudget: Budget? = null

    private val formState = MutableStateFlow(FormState())

    val uiState: StateFlow<AddBudgetUiState> = combine(
        getCategoriesUseCase(),
        formState,
    ) { categories, form ->
        val expenseCategories = categories.filter { it.type == CategoryType.EXPENSE }

        AddBudgetUiState(
            name = form.name,
            limitAmount = form.limitAmount,
            currencyCode = form.currencyCode,
            period = form.period,
            selectedCategoryId = form.selectedCategoryId,
            categories = expenseCategories,
            isSaving = form.isSaving,
            isSaved = form.isSaved,
            isEditMode = editBudgetId != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddBudgetUiState(),
    )

    init {
        if (editBudgetId != null) {
            loadBudget(editBudgetId)
        }
    }

    private fun loadBudget(id: String) {
        viewModelScope.launch {
            val budget = budgetRepository.getBudgetById(id).first() ?: return@launch
            originalBudget = budget
            formState.update {
                val amountStr = if (budget.limitAmount == budget.limitAmount.toLong().toDouble()) {
                    budget.limitAmount.toLong().toString()
                } else {
                    budget.limitAmount.toString()
                }
                it.copy(
                    name = budget.name,
                    limitAmount = amountStr,
                    currencyCode = budget.anchorCurrencyCode,
                    period = budget.period,
                    selectedCategoryId = budget.categoryId,
                )
            }
        }
    }

    fun setName(name: String) {
        formState.update { it.copy(name = name) }
    }

    fun setLimitAmount(amount: String) {
        formState.update { it.copy(limitAmount = amount) }
    }

    fun setCurrencyCode(code: String) {
        formState.update { it.copy(currencyCode = code) }
    }

    fun setPeriod(period: BudgetPeriod) {
        formState.update { it.copy(period = period) }
    }

    fun selectCategory(categoryId: String?) {
        formState.update {
            it.copy(selectedCategoryId = if (it.selectedCategoryId == categoryId) null else categoryId)
        }
    }

    fun save() {
        val current = formState.value
        if (current.name.isBlank()) return
        val amount = current.limitAmount.toDoubleOrNull() ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            formState.update { it.copy(isSaving = true) }
            try {
                val now = DateUtils.now()
                if (editBudgetId != null && originalBudget != null) {
                    val updated = originalBudget!!.copy(
                        name = current.name,
                        limitAmount = amount,
                        anchorCurrencyCode = current.currencyCode,
                        period = current.period,
                        categoryId = current.selectedCategoryId,
                        updatedAt = now,
                    )
                    budgetRepository.updateBudget(updated)
                } else {
                    val budget = Budget(
                        id = UuidGenerator.generate(),
                        name = current.name,
                        limitAmount = amount,
                        anchorCurrencyCode = current.currencyCode,
                        period = current.period,
                        categoryId = current.selectedCategoryId,
                        startDate = DateUtils.todayIsoDate(),
                        isRecurring = true,
                        isActive = true,
                        createdAt = now,
                        updatedAt = now,
                    )
                    budgetRepository.insertBudget(budget)
                }
                formState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (_: Exception) {
                formState.update { it.copy(isSaving = false) }
            }
        }
    }

    private data class FormState(
        val name: String = "",
        val limitAmount: String = "",
        val currencyCode: String = "USD",
        val period: BudgetPeriod = BudgetPeriod.MONTHLY,
        val selectedCategoryId: String? = null,
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
    )
}
