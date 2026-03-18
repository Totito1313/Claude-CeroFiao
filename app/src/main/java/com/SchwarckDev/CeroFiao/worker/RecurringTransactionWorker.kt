package com.SchwarckDev.CeroFiao.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.schwarckdev.cerofiao.core.domain.usecase.ProcessDueRecurringTransactionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RecurringTransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val processDueRecurringTransactions: ProcessDueRecurringTransactionsUseCase,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            processDueRecurringTransactions()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "recurring_transaction_worker"
    }
}
