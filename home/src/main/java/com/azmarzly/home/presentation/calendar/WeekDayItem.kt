package com.azmarzly.home.presentation.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import core.ui.theme.weekDaysColor

@Composable
fun WeekDayItem(
    day: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.weekDaysColor
            )
        )
    }
}