package com.azmarzly.home.presentation.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import core.model.CalendarDay
import core.ui.theme.bodySmall
import core.ui.theme.emptyProgressColor
import core.ui.theme.shadowedTextColor
import core.ui.theme.verticalFilledProgressColor
import core.util.toHourAndMinutes
import core.util.toStringFormatted
import java.time.LocalDate

@Composable
fun CalendarBottomSheet(bottomBarPadding: Dp, state: CalendarState, selectedDayData: CalendarDay?) {
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
                progressInPercentage = state.selectedDayData?.hydrationData?.progressInPercentage ?: 0,
                progress = state.selectedDayData?.hydrationData?.progress ?: 0,
                date = state.selectedDate
            )
            Spacer(modifier = Modifier.height(20.dp))

            selectedDayData?.let { data ->
                CalendarBottomSheetContent(state, data)
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
        Text(
            text = "$progress ${stringResource(id = R.string.unit_milliliter)}",
            style = MaterialTheme.typography.h4.copy(
                color = MaterialTheme.colors.primary
            )
        )
    }
}

@Composable
private fun CalendarBottomSheetContent(state: CalendarState, selectedDayData: CalendarDay) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.selectedDayData != null) {
            selectedDayData.hydrationData.hydrationChunksList.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val drinkType = it.amountToDrinkType()
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = drinkType.resIdUnselected),
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "${it.amount} ${stringResource(id = R.string.unit_milliliter)}", style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colors.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = it.dateTime.toHourAndMinutes())
                }
            }
        }
    }
}