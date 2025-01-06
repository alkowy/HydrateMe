package com.azmarzly.home.presentation.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import core.input_validators.ValidationState
import core.model.CalendarDay
import core.model.HydrationData.HydrationChunk
import core.ui.theme.bodySmall
import core.ui.theme.emptyProgressColor
import core.ui.theme.shadowedTextColor
import core.ui.theme.verticalFilledProgressColor
import core.util.isInFuture
import core.util.isSameDayAs
import core.util.toHourAndMinutes
import core.util.toStringFormatted
import java.time.LocalDate

@Composable
fun CalendarBottomSheet(
    bottomBarPadding: Dp,
    state: CalendarState,
    selectedDayData: CalendarDay?,
    onDeleteHydrationChunk: (HydrationChunk) -> Unit,
    validateCustomAmount: (String) -> ValidationState,
    updateHydrationChunk: (HydrationChunk) -> Unit,
) {
    var hydrationChunkToUpdate by remember { mutableStateOf<HydrationChunk?>(null) }
    var showEditHydrationChunkDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
            .padding(top = 16.dp)
    ) {
        val handleColor = MaterialTheme.colors.onBackground
        Canvas(modifier = Modifier
            .align(Alignment.TopCenter)
            .size(width = 64.dp, height = 6.dp),
            onDraw = {
                drawRoundRect(
                    color = handleColor,
                    cornerRadius = CornerRadius(12f, 12f)
                )
            })
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomBarPadding, top = 20.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BottomSheetHeaderGeneralInfoRow(
                modifier = Modifier,
                progressInPercentage = state.selectedDayData?.hydrationData?.calculateProgressInPercents() ?: 0,
                progress = state.selectedDayData?.hydrationData?.getProgressMillis() ?: 0,
                date = state.selectedDate,
                onEditCalendarDay = { showEditHydrationChunkDialog = true }
            )
            Spacer(modifier = Modifier.height(20.dp))

            selectedDayData?.let { data ->
                CalendarBottomSheetContent(
                    state = state,
                    selectedDayData = data,
                    onDeleteHydrationChunk = onDeleteHydrationChunk,
                    onSelectChunkToUpdate = {
                        showEditHydrationChunkDialog = true
                        hydrationChunkToUpdate = it
                    }
                )

                if (showEditHydrationChunkDialog) {
                    EditHydrationChunkDialog(
                        calendarDay = selectedDayData,
                        hydrationChunk = hydrationChunkToUpdate,
                        onSave = { newChunk ->
                            updateHydrationChunk(newChunk)
                            showEditHydrationChunkDialog = false
                            hydrationChunkToUpdate = null
                        },
                        onDismiss = { showEditHydrationChunkDialog = false },
                        validateCustomAmount = validateCustomAmount,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomSheetHeaderGeneralInfoRow(
    modifier: Modifier = Modifier,
    progressInPercentage: Int,
    progress: Int,
    date: LocalDate,
    onEditCalendarDay: () -> Unit,
) {
    val isGoalMet = progressInPercentage >= 100
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(25))
                .background(MaterialTheme.colors.emptyProgressColor),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(
                        progressInPercentage
                            .toFloat()
                            .div(100)
                    )
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25))
                    .background(if (isGoalMet) MaterialTheme.colors.primary else MaterialTheme.colors.verticalFilledProgressColor)
            ) {
                if (isGoalMet) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Goal met check",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = date.toStringFormatted(),
                style = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onBackground
                )
            )
            Text(
                text = "${progressInPercentage}% ${stringResource(R.string.hydration)}",
                style = MaterialTheme.typography.caption.copy(
                    color = MaterialTheme.colors.shadowedTextColor
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(8.dp)
        ) {
            Text(
                text = "$progress ${stringResource(id = R.string.unit_milliliter)}",
                style = MaterialTheme.typography.h4.copy(
                    color = MaterialTheme.colors.primary
                )
            )
            if (date.isInFuture().not()) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onEditCalendarDay()
                        },
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun CalendarBottomSheetContent(
    state: CalendarState,
    selectedDayData: CalendarDay,
    onDeleteHydrationChunk: (HydrationChunk) -> Unit,
    onSelectChunkToUpdate: (HydrationChunk) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.selectedDayData != null) {
            selectedDayData.hydrationData.hydrationChunksList.forEach {
                CalendarBottomSheetItemRow(
                    chunk = it,
                    onDelete = {
                        onDeleteHydrationChunk(it)
                    },
                    onEdit = { onSelectChunkToUpdate(it) },
                )
            }
        }
    }
}

@Composable
private fun CalendarBottomSheetItemRow(
    modifier: Modifier = Modifier,
    chunk: HydrationChunk,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val drinkType = chunk.amountToDrinkType()

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = drinkType.resIdUnselected),
            contentDescription = "",
            tint = MaterialTheme.colors.primary
        )
        Text(
            text = "${chunk.amount} ${stringResource(id = R.string.unit_milliliter)}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colors.onBackground
            )
        )
        Spacer(modifier = Modifier.weight(1f))

        Text(text = chunk.dateTime.toHourAndMinutes())
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Filled.AccessTime,
            contentDescription = null,
        )
        MinimalDropdownMenu(onEdit = onEdit, onDelete = onDelete)
    }
}