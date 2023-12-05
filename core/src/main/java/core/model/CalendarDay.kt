package core.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val hydrationData: HydrationData,
    val isInDifferentMonth: Boolean,
)