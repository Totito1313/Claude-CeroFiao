package com.schwarckdev.cerofiao.core.data.di

import com.schwarckdev.cerofiao.core.data.repository.AccountRepositoryImpl
import com.schwarckdev.cerofiao.core.data.repository.BudgetRepositoryImpl
import com.schwarckdev.cerofiao.core.data.repository.CategoryRepositoryImpl
import com.schwarckdev.cerofiao.core.data.repository.DebtRepositoryImpl
import com.schwarckdev.cerofiao.core.data.repository.ExchangeRateRepositoryImpl
import com.schwarckdev.cerofiao.core.data.repository.TransactionLogRepositoryImpl
import com.schwarckdev.cerofiao.core.data.repository.TransactionRepositoryImpl
import com.schwarckdev.cerofiao.core.data.repository.UserPreferencesRepositoryImpl
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.BudgetRepository
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.DebtRepository
import com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionLogRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindDebtRepository(impl: DebtRepositoryImpl): DebtRepository

    @Binds
    @Singleton
    abstract fun bindTransactionLogRepository(impl: TransactionLogRepositoryImpl): TransactionLogRepository
}
