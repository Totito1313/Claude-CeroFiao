package com.schwarckdev.cerofiao.feature.accounts

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AccountPieSlice(
    val accountName: String,
    val balanceConverted: Double,
    val color: Color,
)

data class AccountCardData(
    val account: Account,
    val lastTransaction: Transaction? = null,
    val lastTransactionLabel: String? = null,
    val progressRatio: Float? = null,
)

data class AccountListUiState(
    val accounts: List<AccountCardData> = emptyList(),
    val totalBalance: Map<String, Double> = emptyMap(),
    val pieSlices: List<AccountPieSlice> = emptyList(),
    val totalConverted: Double = 0.0,
    val displayCurrency: String = "USD",
)

enum class ChartDisplayCurrency(
    val code: String,
    val symbol: String,
    val displayName: String,
    val sourceLabel: String,
) {
    USD("USD", "$", "Dolar", "BCV"),
    USDT("USDT", "\u20AE", "USDT", "Paralelo"),
    EUR("EUR", "\u20AC", "Euro", "BCV"),
}

@HiltViewModel
class AccountListViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase,
    private val transactionRepository: TransactionRepository,
    private val resolveExchangeRate: ResolveExchangeRateUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _chartCurrency = MutableStateFlow(ChartDisplayCurrency.USD)

    val uiState: StateFlow<AccountListUiState> = flow {
        combine(
            getAccountsUseCase(),
            userPreferencesRepository.userPreferences,
            _chartCurrency,
        ) { accounts, prefs, chartCurrency ->
            val totalBalance = accounts
                .filter { it.includeInTotal }
                .groupBy { it.currencyCode }
                .mapValues { (_, group) -> group.sumOf { it.balance } }

            val cardDataList = accounts.map { account ->
                val lastTx = transactionRepository.getLastTransactionForAccount(account.id)
                val label = lastTx?.let { tx ->
                    val sign = if (tx.type == TransactionType.EXPENSE) "-" else "+"
                    val formatted = CurrencyFormatter.format(tx.amount, tx.currencyCode)
                    val relative = DateUtils.relativeDate(tx.date)
                    "$sign$formatted ($relative)"
                }
                val progress = if (account.initialBalance > 0.0) {
                    (account.balance / account.initialBalance).toFloat().coerceIn(0f, 1f)
                } else null

                AccountCardData(
                    account = account,
                    lastTransaction = lastTx,
                    lastTransactionLabel = label,
                    progressRatio = progress,
                )
            }

            val targetCode = chartCurrency.code
            val pieSlices = accounts.mapIndexedNotNull { index, account ->
                if (account.balance <= 0.0) return@mapIndexedNotNull null
                val rateResult = resolveExchangeRate.resolve(
                    account.currencyCode, targetCode, prefs.preferredRateSource,
                )
                val converted = account.balance * rateResult.rate
                AccountPieSlice(
                    accountName = account.name,
                    balanceConverted = converted,
                    color = ChartColors[index % ChartColors.size],
                )
            }

            val totalConverted = pieSlices.sumOf { it.balanceConverted }

            AccountListUiState(
                accounts = cardDataList,
                totalBalance = totalBalance,
                pieSlices = pieSlices,
                totalConverted = totalConverted,
                displayCurrency = targetCode,
            )
        }.collect { emit(it) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AccountListUiState(),
    )

    fun setChartCurrency(currency: ChartDisplayCurrency) {
        _chartCurrency.update { currency }
    }

    companion object {
        private val ChartColors = listOf(
            Color(0xFF00B8D4), // primary cyan
            Color(0xFF8A2BE2), // purple
            Color(0xFF34C759), // green
            Color(0xFFFF9F0A), // orange
            Color(0xFFFF375F), // pink
            Color(0xFF5E5CE6), // indigo
            Color(0xFFEAB308), // yellow
            Color(0xFF64D2FF), // light blue
        )
    }
}
