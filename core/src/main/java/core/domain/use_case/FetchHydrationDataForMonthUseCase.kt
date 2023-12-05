package core.domain.use_case

import core.LocalPreferencesApi
import core.domain.FirestoreRepository
import core.model.CalendarDay
import core.model.HydrationData
import core.model.Resource
import core.util.doNothing
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class FetchHydrationDataForMonthUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val localPreferencesApi: LocalPreferencesApi,
) {

    operator fun invoke(targetDate: LocalDate) = channelFlow<Resource<List<CalendarDay>>> {
        send(Resource.Loading)
        firestoreRepository.fetchUserFromFirestore(localPreferencesApi.getCurrentUserId()).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { userData ->
                        val startDate = targetDate.withDayOfMonth(1)
                        val endDate = startDate.plusMonths(1).minusDays(1)

                        // Include a few days from the previous and next months
                        val daysToIncludeBefore = (startDate.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
                        val daysToIncludeAfter = 6 - (endDate.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7


                        val userDataForMonth = mutableListOf<CalendarDay>()

                        // Create a map for faster lookup
                        val hydrationDataMap = userData.hydrationData.associateBy { it.date }

                        // Include days from the previous month
                        for (i in daysToIncludeBefore downTo 1) {
                            val currentDate = startDate.minusDays(i.toLong())
                            val hydrationDataForDay = hydrationDataMap[currentDate]

                            if (hydrationDataForDay != null) {
                                userDataForMonth.add(
                                    CalendarDay(
                                        date = currentDate,
                                        hydrationData = hydrationDataForDay,
                                        isInDifferentMonth = true
                                    )
                                )
                            } else {
                                userDataForMonth.add(
                                    CalendarDay(
                                        date = currentDate,
                                        hydrationData = HydrationData(
                                            date = currentDate,
                                            goalMillis = 2000,
                                            progress = 0,
                                            progressInPercentage = 0,
                                            hydrationChunksList = emptyList()
                                        ),
                                        isInDifferentMonth = true
                                    )

                                )
                            }
                        }

                        // Include days from the current month
                        var currentDate = startDate
                        while (currentDate <= endDate) {
                            val hydrationDataForDay = hydrationDataMap[currentDate]

                            if (hydrationDataForDay != null) {
                                userDataForMonth.add(
                                    CalendarDay(
                                        date = currentDate,
                                        hydrationData = hydrationDataForDay,
                                        isInDifferentMonth = false
                                    )
                                )
                            } else {
                                userDataForMonth.add(
                                    CalendarDay(
                                        date = currentDate,
                                        hydrationData = HydrationData(
                                            date = currentDate,
                                            goalMillis = 2000,
                                            progress = 0,
                                            progressInPercentage = 0,
                                            hydrationChunksList = emptyList()
                                        ),
                                        isInDifferentMonth = false
                                    )
                                )
                            }

                            currentDate = currentDate.plusDays(1)
                        }

                        // Include days from the next month
                        for (i in 1..daysToIncludeAfter) {
                            val currentDate = endDate.plusDays(i.toLong())
                            val hydrationDataForDay = hydrationDataMap[currentDate]

                            if (hydrationDataForDay != null) {
                                userDataForMonth.add(
                                    CalendarDay(
                                        date = currentDate,
                                        hydrationData = hydrationDataForDay,
                                        isInDifferentMonth = true
                                    )
                                )
                            } else {
                                userDataForMonth.add(
                                    CalendarDay(
                                        date = currentDate,
                                        hydrationData = HydrationData(
                                            date = currentDate,
                                            goalMillis = 2000,
                                            progress = 0,
                                            progressInPercentage = 0,
                                            hydrationChunksList = emptyList()
                                        ),
                                        isInDifferentMonth = true
                                    )
                                )
                            }
                        }

                        send(Resource.Success(userDataForMonth))
                    }
                }

                is Resource.Error -> {
                    send(Resource.Error(resource.errorMessage))
                }

                else -> {
                    doNothing()
                }
            }
        }
    }

}

