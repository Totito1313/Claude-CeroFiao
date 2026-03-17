package com.schwarckdev.cerofiao.core.database.mapper

import com.schwarckdev.cerofiao.core.database.entity.AccountEntity
import com.schwarckdev.cerofiao.core.database.entity.BudgetEntity
import com.schwarckdev.cerofiao.core.database.entity.CategoryEntity
import com.schwarckdev.cerofiao.core.database.entity.CurrencyEntity
import com.schwarckdev.cerofiao.core.database.entity.DebtEntity
import com.schwarckdev.cerofiao.core.database.entity.DebtPaymentEntity
import com.schwarckdev.cerofiao.core.database.entity.ExchangeRateEntity
import com.schwarckdev.cerofiao.core.database.entity.TransactionEntity
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.AccountType
import com.schwarckdev.cerofiao.core.model.Budget
import com.schwarckdev.cerofiao.core.model.BudgetPeriod
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.CategoryType
import com.schwarckdev.cerofiao.core.model.Currency
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtPayment
import com.schwarckdev.cerofiao.core.model.DebtType
import com.schwarckdev.cerofiao.core.model.ExchangeRate
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionType

// Currency
fun CurrencyEntity.toModel() = Currency(
    code = code,
    name = name,
    symbol = symbol,
    decimalPlaces = decimalPlaces,
    isActive = isActive,
    sortOrder = sortOrder,
)

fun Currency.toEntity() = CurrencyEntity(
    code = code,
    name = name,
    symbol = symbol,
    decimalPlaces = decimalPlaces,
    isActive = isActive,
    sortOrder = sortOrder,
)

// Account
fun AccountEntity.toModel() = Account(
    id = id,
    name = name,
    type = AccountType.valueOf(type),
    platform = try { AccountPlatform.valueOf(platform) } catch (_: Exception) { AccountPlatform.NONE },
    currencyCode = currencyCode,
    balance = balance,
    initialBalance = initialBalance,
    iconName = iconName,
    colorHex = colorHex,
    isActive = isActive,
    includeInTotal = includeInTotal,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Account.toEntity(syncId: String? = null, isDeleted: Boolean = false) = AccountEntity(
    id = id,
    name = name,
    type = type.name,
    platform = platform.name,
    currencyCode = currencyCode,
    balance = balance,
    initialBalance = initialBalance,
    iconName = iconName,
    colorHex = colorHex,
    isActive = isActive,
    includeInTotal = includeInTotal,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncId = syncId,
    isDeleted = isDeleted,
)

// Transaction
fun TransactionEntity.toModel() = Transaction(
    id = id,
    type = TransactionType.valueOf(type),
    amount = amount,
    currencyCode = currencyCode,
    accountId = accountId,
    categoryId = categoryId,
    note = note,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt,
    exchangeRateToUsd = exchangeRateToUsd,
    exchangeRateSource = exchangeRateSource?.let {
        try { ExchangeRateSource.valueOf(it) } catch (_: Exception) { null }
    },
    amountInUsd = amountInUsd,
    transferLinkedId = transferLinkedId,
    transferToAccountId = transferToAccountId,
    transferCommission = transferCommission,
    transferCommissionCurrency = transferCommissionCurrency,
    receiptImagePath = receiptImagePath,
)

fun Transaction.toEntity(syncId: String? = null, isDeleted: Boolean = false) = TransactionEntity(
    id = id,
    type = type.name,
    amount = amount,
    currencyCode = currencyCode,
    accountId = accountId,
    categoryId = categoryId,
    note = note,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt,
    exchangeRateToUsd = exchangeRateToUsd,
    exchangeRateSource = exchangeRateSource?.name,
    amountInUsd = amountInUsd,
    transferLinkedId = transferLinkedId,
    transferToAccountId = transferToAccountId,
    transferCommission = transferCommission,
    transferCommissionCurrency = transferCommissionCurrency,
    receiptImagePath = receiptImagePath,
    smsSourceId = null,
    syncId = syncId,
    isDeleted = isDeleted,
)

// Category
fun CategoryEntity.toModel() = Category(
    id = id,
    name = name,
    type = CategoryType.valueOf(type),
    iconName = iconName,
    colorHex = colorHex,
    parentId = parentId,
    isDefault = isDefault,
    sortOrder = sortOrder,
    isActive = isActive,
)

fun Category.toEntity(syncId: String? = null, isDeleted: Boolean = false) = CategoryEntity(
    id = id,
    name = name,
    type = type.name,
    iconName = iconName,
    colorHex = colorHex,
    parentId = parentId,
    isDefault = isDefault,
    sortOrder = sortOrder,
    isActive = isActive,
    syncId = syncId,
    isDeleted = isDeleted,
)

// ExchangeRate
fun ExchangeRateEntity.toModel() = ExchangeRate(
    fromCurrency = fromCurrency,
    toCurrency = toCurrency,
    rate = rate,
    date = date,
    source = try { ExchangeRateSource.valueOf(source) } catch (_: Exception) { ExchangeRateSource.MANUAL },
    fetchedAt = fetchedAt,
)

fun ExchangeRate.toEntity() = ExchangeRateEntity(
    fromCurrency = fromCurrency,
    toCurrency = toCurrency,
    rate = rate,
    date = date,
    source = source.name,
    fetchedAt = fetchedAt,
)

// Debt
fun DebtEntity.toModel() = Debt(
    id = id,
    personName = personName,
    personPhone = personPhone,
    type = DebtType.valueOf(type),
    originalAmount = originalAmount,
    currencyCode = currencyCode,
    remainingAmount = remainingAmount,
    exchangeRateToUsdAtCreation = exchangeRateToUsdAtCreation,
    note = note,
    dueDate = dueDate,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSettled = isSettled,
    settledAt = settledAt,
)

fun Debt.toEntity(syncId: String? = null, isDeleted: Boolean = false) = DebtEntity(
    id = id,
    personName = personName,
    personPhone = personPhone,
    type = type.name,
    originalAmount = originalAmount,
    currencyCode = currencyCode,
    remainingAmount = remainingAmount,
    exchangeRateToUsdAtCreation = exchangeRateToUsdAtCreation,
    note = note,
    dueDate = dueDate,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSettled = isSettled,
    settledAt = settledAt,
    syncId = syncId,
    isDeleted = isDeleted,
)

// Budget
fun BudgetEntity.toModel() = Budget(
    id = id,
    name = name,
    limitAmount = limitAmount,
    anchorCurrencyCode = anchorCurrencyCode,
    period = try { BudgetPeriod.valueOf(period) } catch (_: Exception) { BudgetPeriod.MONTHLY },
    categoryId = categoryId,
    startDate = startDate,
    isRecurring = isRecurring,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Budget.toEntity(syncId: String? = null, isDeleted: Boolean = false) = BudgetEntity(
    id = id,
    name = name,
    limitAmount = limitAmount,
    anchorCurrencyCode = anchorCurrencyCode,
    period = period.name,
    categoryId = categoryId,
    startDate = startDate,
    isRecurring = isRecurring,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncId = syncId,
    isDeleted = isDeleted,
)

// DebtPayment
fun DebtPaymentEntity.toModel() = DebtPayment(
    id = id,
    debtId = debtId,
    transactionId = transactionId,
    amount = amount,
    currencyCode = currencyCode,
    exchangeRateToUsd = exchangeRateToUsd,
    paidAt = paidAt,
    note = note,
)
