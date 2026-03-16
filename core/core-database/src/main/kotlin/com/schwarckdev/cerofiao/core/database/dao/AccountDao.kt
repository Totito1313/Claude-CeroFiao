package com.schwarckdev.cerofiao.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.schwarckdev.cerofiao.core.database.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account WHERE isDeleted = 0 AND isActive = 1 ORDER BY createdAt ASC")
    fun getActiveAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account WHERE isDeleted = 0 ORDER BY createdAt ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account WHERE isDeleted = 0 AND includeInTotal = 1 AND isActive = 1")
    fun getAccountsIncludedInTotal(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account WHERE id = :id AND isDeleted = 0")
    fun getAccountById(id: String): Flow<AccountEntity?>

    @Query("SELECT * FROM account WHERE id = :id AND isDeleted = 0")
    suspend fun getAccountByIdOnce(id: String): AccountEntity?

    @Query("SELECT * FROM account WHERE currencyCode = :currencyCode AND isDeleted = 0 AND isActive = 1")
    fun getAccountsByCurrency(currencyCode: String): Flow<List<AccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Update
    suspend fun update(account: AccountEntity)

    @Query("UPDATE account SET balance = :newBalance, updatedAt = :updatedAt WHERE id = :accountId")
    suspend fun updateBalance(accountId: String, newBalance: Double, updatedAt: Long)

    @Query("UPDATE account SET isDeleted = 1, updatedAt = :updatedAt WHERE id = :accountId")
    suspend fun softDelete(accountId: String, updatedAt: Long)
}
