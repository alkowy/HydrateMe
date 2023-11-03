package core.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun LocalDate.toTimestamp(): Long {
    return this.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
}

fun LocalDateTime.toTimestamp(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}

fun Long?.toLocalDate(): LocalDate? {
    return this?.let {
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
    return LocalDateTime.ofEpochSecond(this.div(1000), 0, ZoneOffset.UTC)
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