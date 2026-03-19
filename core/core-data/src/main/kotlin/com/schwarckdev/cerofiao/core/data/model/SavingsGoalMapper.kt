package com.schwarckdev.cerofiao.core.data.model

import com.schwarckdev.cerofiao.core.database.entity.SavingsContributionEntity
import com.schwarckdev.cerofiao.core.database.entity.SavingsGoalEntity
import com.schwarckdev.cerofiao.core.model.SavingsContribution
import com.schwarckdev.cerofiao.core.model.SavingsGoal

fun SavingsGoalEntity.toModel() = SavingsGoal(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currencyCode = currencyCode,
    currentAmountInUsd = currentAmountInUsd,
    iconName = iconName,
    colorHex = colorHex,
    deadline = deadline,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isCompleted = isCompleted,
    completedAt = completedAt,
)

fun SavingsGoal.toEntity() = SavingsGoalEntity(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currencyCode = currencyCode,
    currentAmountInUsd = currentAmountInUsd,
    iconName = iconName,
    colorHex = colorHex,
    deadline = deadline,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isCompleted = isCompleted,
    completedAt = completedAt,
    syncId = null,
    isDeleted = false,
)

fun SavingsContributionEntity.toModel() = SavingsContribution(
    id = id,
    goalId = goalId,
    transactionId = transactionId,
    amount = amount,
    currencyCode = currencyCode,
    exchangeRateToUsd = exchangeRateToUsd,
    contributedAt = contributedAt,
)

fun SavingsContribution.toEntity() = SavingsContributionEntity(
    id = id,
    goalId = goalId,
    transactionId = transactionId,
    amount = amount,
    currencyCode = currencyCode,
    exchangeRateToUsd = exchangeRateToUsd,
    contributedAt = contributedAt,
    syncId = null,
    isDeleted = false,
)
