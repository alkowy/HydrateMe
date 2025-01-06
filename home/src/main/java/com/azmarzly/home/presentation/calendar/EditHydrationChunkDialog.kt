package com.azmarzly.home.presentation.calendar

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.azmarzly.core.R.string
import core.common_components.PlaneValidatedTextField
import core.common_components.RoundedButtonWithContent
import core.input_validators.ValidationState
import core.model.CalendarDay
import core.model.HydrationData.HydrationChunk
import core.util.clickableOnce
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHydrationChunkDialog(
    calendarDay: CalendarDay,
    hydrationChunk: HydrationChunk?,
    onSave: (HydrationChunk) -> Unit,
    onDismiss: () -> Unit,
    validateCustomAmount: (String) -> ValidationState,
) {
    val customAmountState = remember {
        mutableStateOf(if (hydrationChunk == null) "" else (hydrationChunk.amount).toString())
    }

    var selectedTime by remember { mutableStateOf(hydrationChunk?.dateTime?.toLocalTime() ?: LocalTime.MIN) }
    var hydrationAmount by remember {
        mutableIntStateOf(hydrationChunk?.amount ?: 0)
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(id = string.edit_hydration_chunk),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                PlaneValidatedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = customAmountState,
                    onValueChange = {
                        hydrationAmount =
                            if (validateCustomAmount(it) == ValidationState.Valid) {
                                customAmountState.value.toInt()
                            } else {
                                0
                            }
                    },
                    label = stringResource(id = string.enter_capacity),
                    style = MaterialTheme.typography.body1,
                    trailingIcon = {
                        Text(
                            text = stringResource(id = string.unit_milliliter),
                            style = MaterialTheme.typography.caption
                        )
                    },
                    keyboardType = KeyboardType.Number,
                    maxCharacters = 3,
                    isError = customAmountState.value.isNotEmpty() && hydrationAmount < 1,
                    errorText = stringResource(string.incorrect_amount)
                )
                Spacer(modifier = Modifier.height(16.dp))

                TimePickerRow(
                    initialHour = hydrationChunk?.dateTime?.hour ?: 0,
                    initialMinute = hydrationChunk?.dateTime?.minute ?: 0,
                    onTimeChanged = { hour, minute ->
                        selectedTime = LocalTime.of(hour, minute)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .clickableOnce { onDismiss() }
                            .weight(0.5f),
                        text = stringResource(string.cancel),
                        style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                    )

                    RoundedButtonWithContent(
                        onClick = {
                            val updatedChunk = hydrationChunk?.copy(
                                amount = hydrationAmount,
                                dateTime = hydrationChunk.dateTime.with(selectedTime)
                            ) ?: HydrationChunk(
                                amount = hydrationAmount,
                                dateTime = selectedTime.atDate(calendarDay.date)
                            )
                            onSave(updatedChunk)
                        },
                        modifier = Modifier
                            .weight(0.5f),
                        enabled = hydrationAmount > 0,
                    ) {
                        Text(
                            text = stringResource(string.confirm),
                            style = MaterialTheme.typography.button,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimePickerRow(
    modifier: Modifier = Modifier,
    initialHour: Int,
    initialMinute: Int,
    onTimeChanged: (hour: Int, minute: Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(initialHour) }
    var selectedMinute by remember { mutableStateOf(initialMinute) }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        InfiniteWheelTimePicker(
            items = (0..23).toList(),
            initialIndex = selectedHour,
            visibleItems = 3,
            onValueChange = {
                selectedHour = it
                onTimeChanged(selectedHour, selectedMinute)
            }
        )

        Text(
            text = ":",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        InfiniteWheelTimePicker(
            items = (0..59).toList(),
            initialIndex = selectedMinute,
            visibleItems = 3,
            onValueChange = {
                selectedMinute = it
                onTimeChanged(selectedHour, selectedMinute)
            }
        )
    }
}