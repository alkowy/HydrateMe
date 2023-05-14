package core.util

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toTimestamp(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}

fun Long?.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this ?: 0, 0, ZoneOffset.UTC)
}