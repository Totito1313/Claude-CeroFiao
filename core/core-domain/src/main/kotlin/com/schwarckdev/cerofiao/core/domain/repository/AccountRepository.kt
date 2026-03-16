package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getActiveAccounts(): Flow<List<Account>>
    fun getAllAccounts(): Flow<List<Account>>
    fun getAccountsIncludedInTotal(): Flow<List<Account>>
    fun getAccountById(id: String): Flow<Account?>
    suspend fun getAccountByIdOnce(id: String): Account?
    fun getAccountsByCurrency(currencyCode: String): Flow<List<Account>>
    suspend fun createAccount(account: Account)
    suspend fun updateAccount(account: Account)
    suspend fun updateBalance(accountId: String, newBalance: Double)
    suspend fun deleteAccount(accountId: String)
}
