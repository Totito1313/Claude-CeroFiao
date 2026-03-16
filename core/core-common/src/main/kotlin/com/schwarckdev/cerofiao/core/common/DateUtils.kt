package com.schwarckdev.cerofiao.core.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateUtils {

    private val tz = TimeZone.currentSystemDefault()

    fun now(): Long = Clock.System.now().toEpochMilliseconds()

    fun todayIsoDate(): String {
        val today = Clock.System.now().toLocalDateTime(tz).date
        return today.toString()
    }

    fun toIsoDate(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.toLocalDateTime(tz).date.toString()
    }

    fun toLocalDate(epochMillis: Long): LocalDate {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.toLocalDateTime(tz).date
    }

    fun formatDisplayDate(epochMillis: Long): String {
        val date = toLocalDate(epochMillis)
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        val year = date.year
        return "$day/$month/$year"
    }

    fun formatDisplayDateTime(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val dt = instant.toLocalDateTime(tz)
        val day = dt.dayOfMonth.toString().padStart(2, '0')
        val month = dt.monthNumber.toString().padStart(2, '0')
        val hour = dt.hour.toString().padStart(2, '0')
        val minute = dt.minute.toString().padStart(2, '0')
        return "$day/$month/${dt.year} $hour:$minute"
    }

    fun isToday(epochMillis: Long): Boolean {
        return toIsoDate(epochMillis) == todayIsoDate()
    }

    fun startOfDay(epochMillis: Long): Long {
        val date = toLocalDate(epochMillis)
        val startOfDay = date.atStartOfDayIn(tz)
        return startOfDay.toEpochMilliseconds()
    }

    fun startOfMonth(epochMillis: Long): Long {
        val date = toLocalDate(epochMillis)
        val firstOfMonth = LocalDate(date.year, date.monthNumber, 1)
        return firstOfMonth.atStartOfDayIn(tz).toEpochMilliseconds()
    }

    fun endOfMonth(epochMillis: Long): Long {
        val date = toLocalDate(epochMillis)
        val lastDay = when (date.monthNumber) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (date.year % 4 == 0 && (date.year % 100 != 0 || date.year % 400 == 0)) 29 else 28
            else -> 30
        }
        val lastOfMonth = LocalDate(date.year, date.monthNumber, lastDay)
        return lastOfMonth.atStartOfDayIn(tz).toEpochMilliseconds() + 86_399_999L
    }
}
