package com.schwarckdev.cerofiao.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.schwarckdev.cerofiao.core.database.dao.AccountDao
import com.schwarckdev.cerofiao.core.database.dao.BudgetDao
import com.schwarckdev.cerofiao.core.database.dao.CategoryDao
import com.schwarckdev.cerofiao.core.database.dao.CurrencyDao
import com.schwarckdev.cerofiao.core.database.dao.DebtDao
import com.schwarckdev.cerofiao.core.database.dao.ExchangeRateDao
import com.schwarckdev.cerofiao.core.database.dao.SavingsGoalDao
import com.schwarckdev.cerofiao.core.database.dao.TransactionDao
import com.schwarckdev.cerofiao.core.database.entity.AccountEntity
import com.schwarckdev.cerofiao.core.database.entity.BudgetEntity
import com.schwarckdev.cerofiao.core.database.entity.CategoryEntity
import com.schwarckdev.cerofiao.core.database.entity.CurrencyEntity
import com.schwarckdev.cerofiao.core.database.entity.DebtEntity
import com.schwarckdev.cerofiao.core.database.entity.DebtPaymentEntity
import com.schwarckdev.cerofiao.core.database.entity.ExchangeRateEntity
import com.schwarckdev.cerofiao.core.database.entity.SavingsContributionEntity
import com.schwarckdev.cerofiao.core.database.entity.SavingsGoalEntity
import com.schwarckdev.cerofiao.core.database.entity.SyncMetadataEntity
import com.schwarckdev.cerofiao.core.database.entity.TransactionEntity

@Database(
    entities = [
        CurrencyEntity::class,
        AccountEntity::class,
        TransactionEntity::class,
        CategoryEntity::class,
        ExchangeRateEntity::class,
        DebtEntity::class,
        DebtPaymentEntity::class,
        SavingsGoalEntity::class,
        SavingsContributionEntity::class,
        BudgetEntity::class,
        SyncMetadataEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class CeroFiaoDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun debtDao(): DebtDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun budgetDao(): BudgetDao
}
