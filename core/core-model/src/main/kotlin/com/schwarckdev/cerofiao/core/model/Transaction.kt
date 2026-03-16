package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER,
}

@Serializable
enum class ExchangeRateSource {
    BCV,
    PARALLEL,
    BINANCE_P2P,
    MANUAL,
}

@Serializable
data class Transaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val currencyCode: String,
    val accountId: String,
    val categoryId: String? = null,
    val note: String? = null,
    val date: Long,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val exchangeRateToUsd: Double = 1.0,
    val exchangeRateSource: ExchangeRateSource? = null,
    val amountInUsd: Double = 0.0,
    val transferLinkedId: String? = null,
    val transferToAccountId: String? = null,
    val transferCommission: Double? = null,
    val transferCommissionCurrency: String? = null,
    val receiptImagePath: String? = null,
)
