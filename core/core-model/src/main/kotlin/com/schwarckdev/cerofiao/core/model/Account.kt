package com.schwarckdev.cerofiao.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class AccountType {
    CASH,
    BANK,
    DIGITAL_WALLET,
    CRYPTO_EXCHANGE,
}

@Serializable
enum class AccountPlatform(val displayName: String, val defaultType: AccountType, val defaultCurrencyCode: String) {
    NONE("Otro", AccountType.CASH, "USD"),
    BANESCO("Banesco", AccountType.BANK, "VES"),
    MERCANTIL("Mercantil", AccountType.BANK, "VES"),
    PROVINCIAL("Provincial", AccountType.BANK, "VES"),
    VENEZUELA("Venezuela", AccountType.BANK, "VES"),
    BNC("BNC", AccountType.BANK, "VES"),
    BANCARIBE("Bancaribe", AccountType.BANK, "VES"),
    BINANCE("Binance", AccountType.CRYPTO_EXCHANGE, "USDT"),
    ZELLE("Zelle", AccountType.DIGITAL_WALLET, "USD"),
    ZINLI("Zinli", AccountType.DIGITAL_WALLET, "USD"),
    WALLY("Wally", AccountType.DIGITAL_WALLET, "USD"),
    PAYPAL("PayPal", AccountType.DIGITAL_WALLET, "USD"),
}

@Serializable
data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val platform: AccountPlatform = AccountPlatform.NONE,
    val currencyCode: String,
    val balance: Double = 0.0,
    val initialBalance: Double = 0.0,
    val iconName: String? = null,
    val colorHex: String? = null,
    val isActive: Boolean = true,
    val includeInTotal: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
