package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.domain.repository.RecurringTransactionRepository
import com.schwarckdev.cerofiao.core.model.RecurrenceType
import com.schwarckdev.cerofiao.core.model.RecurringTransaction
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus
import kotlinx.datetime.atStartOfDayIn
import javax.inject.Inject

class ProcessDueRecurringTransactionsUseCase @Inject constructor(
    private val recurringTransactionRepository: RecurringTransactionRepository,
    private val recordTransactionUseCase: RecordTransactionUseCase,
) {
    suspend operator fun invoke(): Int {
        val now = DateUtils.now()
        val dueTransactions = recurringTransactionRepository.getDueTransactions(now)
        var processed = 0

        for (recurring in dueTransactions) {
            // Create the actual transaction
            recordTransactionUseCase(
                amount = recurring.amount,
                currencyCode = recurring.currencyCode,
                accountId = recurring.accountId,
                categoryId = recurring.categoryId,
                type = recurring.type,
                note = recurring.note,
                date = recurring.nextDueDate,
            )

            // Calculate next due date
            val nextDue = calculateNextDueDate(recurring)

            // Check if past end date
            val endDate = recurring.endDate
            if (endDate != null && nextDue > endDate) {
                recurringTransactionRepository.setActive(recurring.id, false)
            } else {
                recurringTransactionRepository.updateNextDueDate(recurring.id, nextDue)
            }

            processed++
        }

        return processed
    }

    private fun calculateNextDueDate(recurring: RecurringTransaction): Long {
        val tz = TimeZone.currentSystemDefault()
        val currentDate = Instant.fromEpochMilliseconds(recurring.nextDueDate)
            .toLocalDateTime(tz).date

        val period = when (recurring.recurrence) {
            RecurrenceType.DAILY -> DatePeriod(days = recurring.periodLength)
            RecurrenceType.WEEKLY -> DatePeriod(days = recurring.periodLength * 7)
            RecurrenceType.MONTHLY -> DatePeriod(months = recurring.periodLength)
            RecurrenceType.YEARLY -> DatePeriod(years = recurring.periodLength)
        }

        val nextDate = currentDate.plus(period)
        return nextDate.atStartOfDayIn(tz).toEpochMilliseconds()
    }
}
