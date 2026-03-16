package com.schwarckdev.cerofiao.core.model

data class GlobalBalance(
    val totalInDisplayCurrency: Double,
    val displayCurrencyCode: String,
    val breakdownByAccount: List<AccountBalance>,
    val breakdownByCurrency: List<CurrencyBalance>,
)

data class AccountBalance(
    val account: Account,
    val balanceInOriginalCurrency: Double,
    val balanceInDisplayCurrency: Double,
)

data class CurrencyBalance(
    val currencyCode: String,
    val totalInOriginalCurrency: Double,
    val totalInDisplayCurrency: Double,
)
