package com.azmarzly.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import core.model.HydrationData
import core.ui.theme.HydrateMeTheme
import core.ui.theme.VeryDarkBlue
import core.ui.theme.compactCalendarProgressText
import core.ui.theme.emptyProgressColor
import core.ui.theme.verticalFilledProgressColor
import core.util.isSameDayAs
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeCompactCalendar(
    hydrationData: List<HydrationData>,
) {
    val today = LocalDate.now()
    Column {
        Text(
            text = stringResource(R.string.weekly_summary),
            style = MaterialTheme.typography.h3.copy(color = VeryDarkBlue),
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colors.onSurface
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    (6L downTo 0L).map { daysBack ->
                        val hydrationDataForSingleDay = hydrationData.find { it.date.isSameDayAs(today.minusDays(daysBack)) }
                        if (hydrationDataForSingleDay == null) {
                            CompactCalendarDay(
                                day = today.minusDays(daysBack),
                                hydrationProgressInPercentage = 0,
                                hydrationAmount = 0
                            )

                        } else {
                            CompactCalendarDay(
                                day = today.minusDays(daysBack),
                                hydrationProgressInPercentage = hydrationDataForSingleDay.progressInPercentage,
                                hydrationAmount = hydrationDataForSingleDay.progress,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.CompactCalendarDay(
    day: LocalDate,
    hydrationProgressInPercentage: Int,
    hydrationAmount: Int,
) {
    val metGoal = hydrationProgressInPercentage >= 100

    Column(
        modifier = Modifier.Companion.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase(),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.wrapContentWidth(unbounded = true)
        )
        ProgressVerticalBar(
            progressPercentage = hydrationProgressInPercentage,
        )

        Text(
            modifier = Modifier
                .wrapContentWidth(unbounded = true)
                .padding(top = 4.dp),
            text = String.format("%.2f", hydrationAmount.toDouble().div(1000)),
            style = MaterialTheme.typography.caption.copy(
                color = if (metGoal) MaterialTheme.colors.primary else MaterialTheme.colors.compactCalendarProgressText
            )
        )
    }
}

@Composable
private fun ProgressVerticalBar(
    progressPercentage: Int,
) {
    val animatedProgressValue = animateFloatAsState(
        targetValue = progressPercentage.toFloat().div(100),
        animationSpec = tween(
            durationMillis = 1500,
            delayMillis = 250
        ), label = ""
    )
    Box(
        modifier = Modifier
            .aspectRatio(0.45f)
            .clip(RoundedCornerShape(25))
            .background(MaterialTheme.colors.emptyProgressColor),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(animatedProgressValue.value)
                .fillMaxWidth()
                .clip(RoundedCornerShape(25))
                .background(if (progressPercentage >= 100) MaterialTheme.colors.primary else MaterialTheme.colors.verticalFilledProgressColor)
        ) {
            if (progressPercentage >= 100) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp),
                    imageVector = Icons.Default.Check, contentDescription = "Goal met check",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun HomeCompactCalendarPreview() {
    HydrateMeTheme() {
        HomeCompactCalendar(emptyList())
    }
}