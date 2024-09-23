package com.uogames.balinasoft.test.ui.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object LocalDateTimeExchange {

    val LocalDateTime.MILS_IN_DAY get() = 86_400_000

    fun LocalDateTime.daysInMonth(): Int {
        return when (month.number) {
            1 -> 31
            2 -> if (isHighEnd()) 29 else 28
            3 -> 31
            4 -> 30
            5 -> 31
            6 -> 30
            7 -> 31
            8 -> 31
            9 -> 30
            10 -> 31
            11 -> 30
            12 -> 31
            else -> throw Exception("What is the month $month?")
        }
    }

    fun daysInMonth(year: Int, month: Month): Int {
        return LocalDateTime(year, month, 1, 0, 0, 0, 0).daysInMonth()
    }

    fun LocalDateTime.timeInMonth(): Long {
        return MILS_IN_DAY * daysInMonth().toLong()
    }

    fun LocalDateTime.isHighEnd(): Boolean {
        return year % 4 == 0 && if (year % 100 == 0) year % 4000 == 0 else true
    }

    operator fun LocalDateTime.Companion.invoke(
        year: Int = 0,
        monthNumber: Int = 1,
        dayOfMonth: Int = 1,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanosecond: Int = 0
    ): LocalDateTime {
        return LocalDateTime(
            year,
            monthNumber,
            dayOfMonth,
            hour,
            minute,
            second,
            nanosecond
        )
    }

    fun LocalDateTime.clone(
        year: Int = this.year,
        monthNumber: Int = this.monthNumber,
        dayOfMonth: Int = this.dayOfMonth,
        hour: Int = this.hour,
        minute: Int = this.minute,
        second: Int = this.second,
        nanosecond: Int = this.nanosecond
    ): LocalDateTime =
        LocalDateTime(year, monthNumber, dayOfMonth, hour, minute, second, nanosecond)

    fun LocalDateTime.Companion.fromEpochMilliseconds(
        epochMilliseconds: Long,
        timeZone: TimeZone = TimeZone.UTC
    ): LocalDateTime {
        return Instant.fromEpochMilliseconds(epochMilliseconds).toLocalDateTime(timeZone)
    }

    fun LocalDateTime.Companion.current(timeZone: TimeZone = TimeZone.UTC): LocalDateTime {
        return Clock.System.now().toLocalDateTime(timeZone)
    }

    fun LocalDateTime.toEpochMilliseconds(timeZone: TimeZone = TimeZone.UTC): Long {
        return toInstant(timeZone).toEpochMilliseconds()
    }

    fun LocalDateTime.Companion.currentEpochMilliseconds(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun Long.toLocalDateTime(
        timeZone: TimeZone = TimeZone.UTC
    ): LocalDateTime = LocalDateTime.fromEpochMilliseconds(
        epochMilliseconds = this,
        timeZone = timeZone
    )

    fun LocalDateTime.toShortText(): String {
        return "${dayOfMonth}.${monthNumber}.${year}"
    }

    fun LocalDateTime.toLongText(): String {
        return "${dayOfMonth}.${monthNumber}.${year} $hour:$minute"
    }

}