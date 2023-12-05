package com.azmarzly.home.presentation.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import core.ui.theme.shadowedTextColor
import core.util.isSameDayAs
import java.time.LocalDate

@Composable
fun CalendarDayItem(
    modifier: Modifier = Modifier,
    onDaySelected: (LocalDate) -> Unit,
    date: LocalDate,
    isSelected: Boolean = false,
    isGoalMet: Boolean = false,
    isFromDifferentMonth: Boolean = false,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .clickable(enabled = isFromDifferentMonth.not()) { onDaySelected(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colors.primary else Color.Transparent
                    )
                    .todayBorder(date.isSameDayAs(LocalDate.now()))
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.body1.copy(
                    color = if (isFromDifferentMonth) MaterialTheme.colors.shadowedTextColor else if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            val dotColor = if (isGoalMet) MaterialTheme.colors.primary else MaterialTheme.colors.shadowedTextColor
            Canvas(
                modifier = Modifier
                    .size(4.dp)
            ) {
                drawCircle(color = dotColor)
            }
        }
    }
}

private fun Modifier.todayBorder(isToday: Boolean = false) =
    composed { if (isToday) border(Dp.Hairline, MaterialTheme.colors.primary, RoundedCornerShape(12.dp)) else this }