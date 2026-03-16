package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.database.dao.AccountDao
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
) : AccountRepository {

    override fun getActiveAccounts(): Flow<List<Account>> =
        accountDao.getActiveAccounts().map { list -> list.map { it.toModel() } }

    override fun getAllAccounts(): Flow<List<Account>> =
        accountDao.getAllAccounts().map { list -> list.map { it.toModel() } }

    override fun getAccountsIncludedInTotal(): Flow<List<Account>> =
        accountDao.getAccountsIncludedInTotal().map { list -> list.map { it.toModel() } }

    override fun getAccountById(id: String): Flow<Account?> =
        accountDao.getAccountById(id).map { it?.toModel() }

    override suspend fun getAccountByIdOnce(id: String): Account? =
        accountDao.getAccountByIdOnce(id)?.toModel()

    override fun getAccountsByCurrency(currencyCode: String): Flow<List<Account>> =
        accountDao.getAccountsByCurrency(currencyCode).map { list -> list.map { it.toModel() } }

    override suspend fun createAccount(account: Account) {
        accountDao.insert(account.toEntity())
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.update(account.toEntity())
    }

    override suspend fun updateBalance(accountId: String, newBalance: Double) {
        accountDao.updateBalance(accountId, newBalance, DateUtils.now())
    }

    override suspend fun deleteAccount(accountId: String) {
        accountDao.softDelete(accountId, DateUtils.now())
    }
}
