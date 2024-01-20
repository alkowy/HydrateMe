package core.util

import androidx.compose.ui.text.capitalize
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun LocalDate.toTimestamp(): Long {
    return this.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
}

//fun LocalDate.toStringFormatted(): String {
//    return "${this.dayOfMonth} ${this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${this.year}"
//}
// yyyy-M-d
fun LocalDate.toStringFormatted(): String {
    return "${this.year}/${this.monthValue}/${this.dayOfMonth}"
}

fun LocalDate.toCalendarHeader(): String {
    return "${this.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} ${this.year}"
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

fun String.toLocalDate(): LocalDate? {
    val formatter = DateTimeFormatter.ofPattern("yyyy/M/d")
    return try {
        LocalDate.parse(this.replace("-", "/"), formatter)
    } catch (e: Exception) {
        null
    }
}

fun LocalDate.isSameDayAs(date: LocalDate): Boolean {
    return this.atStartOfDay(ZoneOffset.UTC).isEqual(date.atStartOfDay(ZoneOffset.UTC))
}

fun LocalDateTime.toHourAndMinutes(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    return this.format(formatter)
}