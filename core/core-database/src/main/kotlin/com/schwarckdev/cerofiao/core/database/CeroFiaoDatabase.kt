package com.schwarckdev.cerofiao.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.schwarckdev.cerofiao.core.database.entity.RecurringTransactionEntity
import com.schwarckdev.cerofiao.core.database.entity.TransactionEntity
import com.schwarckdev.cerofiao.core.database.entity.TransactionLogEntity
import com.schwarckdev.cerofiao.core.database.entity.TransactionTitleEntity

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
        TransactionLogEntity::class,
        RecurringTransactionEntity::class,
        TransactionTitleEntity::class,
    ],
    version = 3,
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
    abstract fun transactionLogDao(): TransactionLogDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun transactionTitleDao(): TransactionTitleDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `transaction_logs` (
                        `id` TEXT NOT NULL,
                        `transactionId` TEXT NOT NULL,
                        `action` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `snapshotJson` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_logs_transactionId` ON `transaction_logs` (`transactionId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_logs_timestamp` ON `transaction_logs` (`timestamp`)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `recurring_transactions` (
                        `id` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `amount` REAL NOT NULL,
                        `currencyCode` TEXT NOT NULL,
                        `categoryId` TEXT,
                        `accountId` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `recurrence` TEXT NOT NULL,
                        `periodLength` INTEGER NOT NULL,
                        `startDate` INTEGER NOT NULL,
                        `endDate` INTEGER,
                        `nextDueDate` INTEGER NOT NULL,
                        `isActive` INTEGER NOT NULL,
                        `note` TEXT,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        `syncId` TEXT,
                        `isDeleted` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_nextDueDate` ON `recurring_transactions` (`nextDueDate`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_isActive` ON `recurring_transactions` (`isActive`)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `transaction_titles` (
                        `id` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `categoryId` TEXT NOT NULL,
                        `isExactMatch` INTEGER NOT NULL,
                        `syncId` TEXT,
                        `isDeleted` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_titles_title` ON `transaction_titles` (`title`)")
            }
        }
    }
}
