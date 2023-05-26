package com.azmarzly.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalendarScreen() {

    CalendarContent()
}

@Composable
fun CalendarContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {

        DatePickerSample()
        repeat(40) {
            Text(text = "CALENDAR $it")
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Pre-select a date for January 4, 2020
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = 1578096000000)
        DatePicker(state = datePickerState, modifier = Modifier.padding(16.dp))

        Text("Selected date timestamp: ${datePickerState.selectedDateMillis ?: "no selection"}")
    }
}