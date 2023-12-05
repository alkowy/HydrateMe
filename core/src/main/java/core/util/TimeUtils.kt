package core.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun LocalDate.toTimestamp(): Long {
    return this.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
}

fun LocalDate.toStringFormatted(): String {
    return "${this.dayOfMonth} ${this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${this.year}"
}

fun LocalDate.toCalendarHeader(): String {
    return "${this.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())} ${this.year}"
}

fun LocalDateTime.toTimestamp(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}

fun Long.toLocalDate(): LocalDate {
    return this.let {
        Instant.ofEpochSecond(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }
}

fun Long?.millisToLocalDate(): LocalDate? {
    return this?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
}

/**
In 'yyyy-MM-dd' format
 */
fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    return LocalDateTime.parse(this, formatter)
}

fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    return LocalDate.parse(this, formatter)
}

fun LocalDateTime.isSameDayAs(date: LocalDateTime): Boolean {
    return this.atZone(ZoneOffset.UTC).toLocalDate().isEqual(date.atZone(ZoneOffset.UTC).toLocalDate())
}

fun LocalDateTime.isSameDayAs(date: LocalDate): Boolean {
    return this.atZone(ZoneOffset.UTC).toLocalDate().isEqual(date)
}

fun LocalDate.isSameDayAs(date: LocalDate): Boolean {
    return this.atStartOfDay(ZoneOffset.UTC).isEqual(date.atStartOfDay(ZoneOffset.UTC))
}

fun LocalDate.isSameMonthAs(month: Month): Boolean {
    return this.month.compareTo(month) == 0
}