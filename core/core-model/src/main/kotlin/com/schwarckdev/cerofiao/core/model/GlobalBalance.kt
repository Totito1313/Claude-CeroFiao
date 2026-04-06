package com.schwarckdev.cerofiao.core.model

data class GlobalBalance(
    val totalInDisplayCurrency: Double,
    val displayCurrencyCode: String,
    val breakdownByAccount: List<AccountBalance>,
    val breakdownByCurrency: List<CurrencyBalance>,
    /** Total balance in all 5 currencies, pre-computed via RateTable. */
    val total: MultiCurrencyAmount = MultiCurrencyAmount.ZERO,
)

data class AccountBalance(
    val account: Account,
    val balanceInOriginalCurrency: Double,
    val balanceInDisplayCurrency: Double,
    /** Account balance in all 5 currencies, pre-computed via RateTable. */
    val multiCurrency: MultiCurrencyAmount = MultiCurrencyAmount.ZERO,
)

data class CurrencyBalance(
    val currencyCode: String,
    val totalInOriginalCurrency: Double,
    val totalInDisplayCurrency: Double,
    /** Currency group total in all 5 currencies, pre-computed via RateTable. */
    val multiCurrency: MultiCurrencyAmount = MultiCurrencyAmount.ZERO,
)
