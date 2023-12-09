package com.azmarzly.home.presentation.calendar

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.model.CalendarDay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    bottomBarPadding: Dp,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
) {

    val state by calendarViewModel.calendarState.collectAsStateWithLifecycle()

    CalendarScreenContent(
        bottomBarPadding = bottomBarPadding,
        state = state,
        updateSelectedDay = calendarViewModel::updateDaySelected,
        changeMonth = calendarViewModel::changeMonth,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenContent(
    bottomBarPadding: Dp,
    state: CalendarState,
    updateSelectedDay: (LocalDate) -> Unit,
    changeMonth: (CalendarDirection) -> Unit,
) {

    val screenConfiguration = LocalConfiguration.current
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.surface
            ),
        scaffoldState = scaffoldState,
        sheetPeekHeight = screenConfiguration.screenHeightDp.div(2).dp,
        sheetSwipeEnabled = true,
        sheetDragHandle = null,
        sheetContent = {
            CalendarBottomSheet(bottomBarPadding, state, state.selectedDayData)
        },
        content = { paddingValues ->
            Calendar(
                onDateSelected = updateSelectedDay,
                bottomSheetState = scaffoldState.bottomSheetState,
                calendarState = state,
                changeMonth = changeMonth,
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    onDateSelected: (LocalDate) -> Unit,
    bottomSheetState: SheetState,
    calendarState: CalendarState,
    changeMonth: (CalendarDirection) -> Unit,
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface
            )
    ) {
        CalendarHeader(
            headerDate = calendarState.calendarHeaderDate,
            onPreviousMonth = {
                changeMonth(CalendarDirection.LEFT)
                scope.launch { bottomSheetState.partialExpand() }
            },
            onNextMonth = {
                changeMonth(CalendarDirection.RIGHT)
                scope.launch { bottomSheetState.partialExpand() }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DayOfWeek.values().forEach {
                WeekDayItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    day = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase(),
                )
            }
        }

        val screenConfiguration = LocalConfiguration.current

        val enterTransition = fadeIn() + slideInVertically(animationSpec = tween(400))
        val exitTransition = fadeOut() + slideOutVertically(animationSpec = tween(600))
        val bottomSheetStateValue = remember { derivedStateOf { bottomSheetState.targetValue } }

        AnimatedContent(
            targetState = bottomSheetStateValue.value, label = "",
            transitionSpec = {
                enterTransition.togetherWith(exitTransition)
            }) { sheetState ->
            when (sheetState) {
                SheetValue.Expanded -> {
                    CalendarFlowRow(
                        calendarDaysList = calendarState.selectedWeekData,
                        screenConfiguration = screenConfiguration,
                        updateSelectedDay = onDateSelected,
                        calendarState = calendarState,
                    )
                }

                else -> {
                    Column {
                        CalendarFlowRow(
                            calendarDaysList = calendarState.currentMonthData,
                            screenConfiguration = screenConfiguration,
                            updateSelectedDay = onDateSelected,
                            calendarState = calendarState
                        )
                    }
                }
            }

        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun CalendarFlowRow(
    calendarDaysList: List<CalendarDay>,
    screenConfiguration: Configuration,
    updateSelectedDay: (LocalDate) -> Unit,
    calendarState: CalendarState,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        maxItemsInEachRow = 7
    ) {
        calendarDaysList.forEach { calendarDayData ->
            CalendarDayItem(
                modifier = Modifier
                    .heightIn(
                        max = (screenConfiguration.screenHeightDp
                            .div(2)
                            .div(7)).dp
                    )
                    .weight(1f)
                    .aspectRatio(1f),
                onDaySelected = { updateSelectedDay(calendarDayData.date) },
                date = calendarDayData.date,
                isSelected = calendarState.selectedDate == calendarDayData.date,
                isFromDifferentMonth = calendarDayData.isInDifferentMonth,
                isGoalMet = calendarDayData.hydrationData.progressInPercentage >= 100,
            )
        }
    }
}