package com.azmarzly.home.presentation.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.DispatcherIO
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.FetchHydrationDataForMonthUseCase
import core.domain.use_case.PeriodicallyFetchUserDataModelUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.model.CalendarDay
import core.model.HydrationData.HydrationChunk
import core.model.Resource
import core.util.doNothing
import core.util.isSameDayAs
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.previousOrSame
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val fetchHydrationDataForMonthUseCase: FetchHydrationDataForMonthUseCase,
    @DispatcherIO private val ioDispatcher: CoroutineDispatcher,
    private val periodicallyFetchUserDataModelUseCase: PeriodicallyFetchUserDataModelUseCase,
    @Named("WholeNumberValidator") private val numberValidator: InputValidator,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
) : ViewModel() {

    private var _calendarState = MutableStateFlow(CalendarState(isLoading = true))
    val calendarState = _calendarState.asStateFlow()

    init {
        fetchMonthDataForDate(LocalDate.now())
        periodicallyFetchUserDataAndMapToCalendarState()
    }

    fun onConfirmEditHydrationChunk(chunk: HydrationChunk) {
        val currentSelectedDayData = _calendarState.value.selectedDayData
        Log.d("ANANAS", "CalendarViewModel.kt - onConfirmEditHydrationChunk: $currentSelectedDayData")
        if (currentSelectedDayData != null) {
            if (currentSelectedDayData.hydrationData.hydrationChunksList.any { it.uuid == chunk.uuid }) {
                editExistingHydrationChunk(currentSelectedDayData, chunk)
            } else {
                addNewHydrationChunk(currentSelectedDayData, chunk)
            }
        }
    }

    private fun editExistingHydrationChunk(
        currentSelectedDayData: CalendarDay,
        updatedChunk: HydrationChunk
    ) {
        Log.d("ANANAS", "CalendarViewModel.kt - editExistingHydrationChunk: $currentSelectedDayData \n $updatedChunk")
        val updatedChunks = currentSelectedDayData.hydrationData.hydrationChunksList.map { chunk ->
            if (chunk.uuid == updatedChunk.uuid) updatedChunk else chunk
        }.sortedBy { it.dateTime }

        val updatedHydrationData = currentSelectedDayData.hydrationData.copy(
            hydrationChunksList = updatedChunks
        )

        val updatedSelectedDayData = currentSelectedDayData.copy(hydrationData = updatedHydrationData)
        _calendarState.update { it.copy(selectedDayData = updatedSelectedDayData) }

        updateDataInFirebase(updatedSelectedDayData)
    }

    private fun addNewHydrationChunk(
        currentSelectedDayData: CalendarDay,
        newChunk: HydrationChunk
    ) {
        Log.d("ANANAS", "CalendarViewModel.kt - addNewHydrationChunk: $currentSelectedDayData \n $newChunk")
        val updatedChunks = currentSelectedDayData.hydrationData.hydrationChunksList + newChunk

        val updatedHydrationData = currentSelectedDayData.hydrationData.copy(
            hydrationChunksList = updatedChunks.sortedBy { it.dateTime }
        )

        val updatedSelectedDayData = currentSelectedDayData.copy(hydrationData = updatedHydrationData)
        _calendarState.update { it.copy(selectedDayData = updatedSelectedDayData) }

        updateDataInFirebase(updatedSelectedDayData)
    }

    fun validateNumber(amount: String): ValidationState {
        return numberValidator.isValid(amount)
    }

    fun deleteHydrationChunk(chunk: HydrationChunk) {
        val currentSelectedDayData = _calendarState.value.selectedDayData

        if (currentSelectedDayData != null) {
            val updatedChunks =
                currentSelectedDayData.hydrationData.hydrationChunksList.filter { it != chunk }

            val updatedHydrationData = currentSelectedDayData.hydrationData.copy(
                hydrationChunksList = updatedChunks
            )

            val updatedSelectedDayData = currentSelectedDayData.copy(hydrationData = updatedHydrationData)
            _calendarState.update {
                it.copy(selectedDayData = updatedSelectedDayData)
            }

            updateDataInFirebase(updatedSelectedDayData)
        }
    }

    private fun updateDataInFirebase(updatedSelectedDayData: CalendarDay) {
        viewModelScope.launch(ioDispatcher) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                if (result is Resource.Success) {
                    val currentUser = result.data

                    val updatedHydrationData = currentUser?.hydrationData
                        ?.filter { it.date.isEqual(updatedSelectedDayData.date).not() }
                        ?.toMutableList()
                        ?.apply {
                            add(updatedSelectedDayData.hydrationData)
                        } ?: emptyList()

                    val updatedUser = currentUser?.copy(hydrationData = updatedHydrationData)

                    if (updatedUser != null) {
                        updateFirestoreUserUseCase(updatedUser.toFirestoreUserDataModel())
                    }
                }
            }
        }
    }

    private fun periodicallyFetchUserDataAndMapToCalendarState() {
        viewModelScope.launch(ioDispatcher) {
            periodicallyFetchUserDataModelUseCase.invoke()
                .collectLatest { fetchResult ->
                    fetchMonthDataForDate(_calendarState.value.selectedDate)
                    when (fetchResult) {
                        is Resource.Success -> {
                            val selectedDateData =
                                fetchResult.data?.hydrationData?.find { it.date.isSameDayAs(_calendarState.value.selectedDate) }
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

    private fun fetchMonthDataForDate(date: LocalDate) {
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
                                selectedWeekData = resource.data?.filter {
                                    it.date.with(previousOrSame(DayOfWeek.MONDAY))
                                        .equals(date.with(previousOrSame(DayOfWeek.MONDAY)))
                                }
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
                selectedWeekData = it.currentMonthData.filter {
                    it.date.with(previousOrSame(DayOfWeek.MONDAY)).equals(daySelected.with(previousOrSame(DayOfWeek.MONDAY)))
                }
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
                                selectedWeekData = resource.data?.filter {
                                    it.date.with(previousOrSame(DayOfWeek.MONDAY))
                                        .equals(date.with(previousOrSame(DayOfWeek.MONDAY)))
                                }
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