package com.azmarzly.home.presentation.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.azmarzly.core.R
import core.util.toCalendarHeader
import java.time.LocalDate

@Composable
fun CalendarHeader(
    headerDate: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onPreviousMonth() }) {
            Icon(
                painterResource(id = R.drawable.ic_arrow_left), contentDescription = "arrow_left",
                tint = MaterialTheme.colors.onBackground
            )
        }
        Text(
            text = headerDate.toCalendarHeader(),
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.onBackground
            )
        )

        IconButton(onClick = { onNextMonth() }) {
            Icon(
                painterResource(id = R.drawable.ic_arrow_right), contentDescription = "arrow_right",
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}