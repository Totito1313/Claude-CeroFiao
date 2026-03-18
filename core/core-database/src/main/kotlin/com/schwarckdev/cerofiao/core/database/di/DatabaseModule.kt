package com.schwarckdev.cerofiao.core.database.di

import android.content.Context
import androidx.room.Room
import com.schwarckdev.cerofiao.core.database.CeroFiaoDatabase
import com.schwarckdev.cerofiao.core.database.DatabasePrepopulate
import com.schwarckdev.cerofiao.core.database.dao.AccountDao
import com.schwarckdev.cerofiao.core.database.dao.BudgetDao
import com.schwarckdev.cerofiao.core.database.dao.CategoryDao
import com.schwarckdev.cerofiao.core.database.dao.CurrencyDao
import com.schwarckdev.cerofiao.core.database.dao.DebtDao
import com.schwarckdev.cerofiao.core.database.dao.ExchangeRateDao
import com.schwarckdev.cerofiao.core.database.dao.SavingsGoalDao
import com.schwarckdev.cerofiao.core.database.dao.TransactionDao
import com.schwarckdev.cerofiao.core.database.dao.RecurringTransactionDao
import com.schwarckdev.cerofiao.core.database.dao.TransactionLogDao
import com.schwarckdev.cerofiao.core.database.dao.TransactionTitleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CeroFiaoDatabase {
        lateinit var database: CeroFiaoDatabase
        database = Room.databaseBuilder(
            context,
            CeroFiaoDatabase::class.java,
            "cerofiao.db",
        )
            .addMigrations(CeroFiaoDatabase.MIGRATION_1_2, CeroFiaoDatabase.MIGRATION_2_3)
            .addCallback(DatabasePrepopulate { database })
            .build()
        return database
    }

    @Provides
    fun provideCurrencyDao(database: CeroFiaoDatabase): CurrencyDao = database.currencyDao()

    @Provides
    fun provideAccountDao(database: CeroFiaoDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideTransactionDao(database: CeroFiaoDatabase): TransactionDao = database.transactionDao()

    @Provides
    fun provideCategoryDao(database: CeroFiaoDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideExchangeRateDao(database: CeroFiaoDatabase): ExchangeRateDao = database.exchangeRateDao()

    @Provides
    fun provideDebtDao(database: CeroFiaoDatabase): DebtDao = database.debtDao()

    @Provides
    fun provideSavingsGoalDao(database: CeroFiaoDatabase): SavingsGoalDao = database.savingsGoalDao()

    @Provides
    fun provideBudgetDao(database: CeroFiaoDatabase): BudgetDao = database.budgetDao()

    @Provides
    fun provideTransactionLogDao(database: CeroFiaoDatabase): TransactionLogDao = database.transactionLogDao()

    @Provides
    fun provideRecurringTransactionDao(database: CeroFiaoDatabase): RecurringTransactionDao = database.recurringTransactionDao()

    @Provides
    fun provideTransactionTitleDao(database: CeroFiaoDatabase): TransactionTitleDao = database.transactionTitleDao()
}
