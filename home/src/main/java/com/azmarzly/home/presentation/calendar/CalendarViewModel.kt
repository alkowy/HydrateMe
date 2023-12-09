package com.azmarzly.home.presentation.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.DispatcherIO
import core.domain.use_case.FetchHydrationDataForMonthUseCase
import core.domain.use_case.PeriodicallyFetchUserDataModelUseCase
import core.model.CalendarDay
import core.model.Resource
import core.util.doNothing
import core.util.isSameDayAs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.previousOrSame
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val fetchHydrationDataForMonthUseCase: FetchHydrationDataForMonthUseCase,
    @DispatcherIO private val ioDispatcher: CoroutineDispatcher,
    private val periodicallyFetchUserDataModelUseCase: PeriodicallyFetchUserDataModelUseCase,
) : ViewModel() {

    private var _calendarState = MutableStateFlow(CalendarState(isLoading = true))
    val calendarState = _calendarState.asStateFlow()

    init {
        fetchCurrentMonthData(LocalDate.now())
        periodicallyFetchUserDataAndMapToCalendarState()
    }

    private fun periodicallyFetchUserDataAndMapToCalendarState() {
        viewModelScope.launch(ioDispatcher) {
            periodicallyFetchUserDataModelUseCase.invoke()
                .collectLatest { fetchResult ->
                    Log.d("ANANAS", "periodicallyFetchUserDataAndMapToHomeState in CALENDAR: $fetchResult")
                    when (fetchResult) {
                        is Resource.Success -> {
                            val selectedDateData = fetchResult.data?.hydrationData?.find { it.date.isSameDayAs(_calendarState.value.selectedDate) }
                            selectedDateData?.let { selectedHydrationData ->
                                _calendarState.update { calendarState ->
                                    calendarState.copy(
                                        isLoading = false,
                                        selectedDayData = calendarState.selectedDayData?.copy(
                                            hydrationData = selectedHydrationData
                                        )
                                    )
                                }
                            }
                        }

                        else -> doNothing()
                    }
                }
        }
    }

    private fun fetchCurrentMonthData(date: LocalDate) {
        viewModelScope.launch(ioDispatcher) {
            fetchHydrationDataForMonthUseCase.invoke(date).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _calendarState.update { calendarState ->
                            calendarState.copy(
                                isLoading = false,
                                selectedDayData = resource.data?.find { it.date.isSameDayAs(date) },
                                selectedDate = date,
                                currentMonthData = resource.data ?: emptyList(),
                                selectedWeekData = resource.data?.filter { it.date.with(previousOrSame(DayOfWeek.MONDAY)).equals(date.with(previousOrSame(DayOfWeek.MONDAY))) }
                                    ?: emptyList(),
                                calendarHeaderDate = date

                            )
                        }
                    }

                    is Resource.Loading,
                    is Resource.Error,
                    -> {
                        _calendarState.update { calendarState ->
                            calendarState.copy(
                                isLoading = true,
                                selectedDate = date,
                            )
                        }
                    }

                    else -> {}
                }

            }
        }
    }

    fun updateDaySelected(daySelected: LocalDate) {
        _calendarState.update {
            it.copy(
                selectedDate = daySelected,
                selectedDayData = it.currentMonthData.first { it.date.isSameDayAs(daySelected) },
                selectedWeekData = it.currentMonthData.filter { it.date.with(previousOrSame(DayOfWeek.MONDAY)).equals(daySelected.with(previousOrSame(DayOfWeek.MONDAY))) }
            )
        }
    }

    fun changeMonth(direction: CalendarDirection) {
        val date = when (direction) {
            CalendarDirection.LEFT -> {
                _calendarState.value.calendarHeaderDate.minusMonths(1)
            }

            CalendarDirection.RIGHT -> {
                _calendarState.value.calendarHeaderDate.plusMonths(1)
            }
        }
        viewModelScope.launch(ioDispatcher) {
            fetchHydrationDataForMonthUseCase.invoke(date).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _calendarState.update { calendarState ->
                            calendarState.copy(
                                isLoading = false,
                                currentMonthData = resource.data ?: emptyList(),
                                selectedWeekData = resource.data?.filter { it.date.with(previousOrSame(DayOfWeek.MONDAY)).equals(date.with(previousOrSame(DayOfWeek.MONDAY))) }
                                    ?: emptyList(),
                                calendarHeaderDate = date

                            )
                        }
                    }

                    is Resource.Loading,
                    is Resource.Error,
                    -> {
                        _calendarState.update { calendarState ->
                            calendarState.copy(
                                isLoading = true,
                            )
                        }
                    }

                    else -> {}
                }

            }
        }
    }

}

enum class CalendarDirection {
    LEFT, RIGHT
}

data class CalendarState(
    val isLoading: Boolean,
    val selectedDayData: CalendarDay? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonthData: List<CalendarDay> = emptyList(),
    val selectedWeekData: List<CalendarDay> = emptyList(),
    val calendarHeaderDate: LocalDate = LocalDate.now(),
)