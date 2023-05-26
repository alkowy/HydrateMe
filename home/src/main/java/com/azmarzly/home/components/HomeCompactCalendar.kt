package com.azmarzly.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import core.ui.theme.HydrateMeTheme
import core.ui.theme.emptyProgressColor

@Composable
fun HomeCompactCalendar() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colors.background.copy(alpha = 0.9f)
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
            Text(
                text = "15-21 Maja",
                style = MaterialTheme.typography.h4
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                repeat(7) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CZW",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.wrapContentWidth(unbounded = true)
                        )
                        ProgressVerticalBar()
                        Text(
                            text = "43%",
                            style = MaterialTheme.typography.caption.copy(
                                color = MaterialTheme.colors.primary
                            ),
                            modifier = Modifier.wrapContentWidth(unbounded = true)
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun ProgressVerticalBar() {
    Box(
        modifier = Modifier
            .aspectRatio(0.33f)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colors.emptyProgressColor),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colors.primary)
        )
    }
}

@Composable
fun CompactCalendarItem(day: String, progress: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
//            .fillMaxSize()
            .border(5.dp, color = Color.Red)
    ) {
        Text(text = day)

        VerticalProgressBar(progress = progress)
    }
}

@Composable
fun VerticalProgressBar(
    progress: Int,
    barColor: Color = MaterialTheme.colors.primary,
    backgroundBarColor: Color = Color.Gray // todo replace with some theme color
) {


}

@Preview
@Composable
fun CompactCalendarItemPreview() {
    HydrateMeTheme() {
        CompactCalendarItem("PN", 75)
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun HomeCompactCalendarPreview() {
    HydrateMeTheme() {
        HomeCompactCalendar()
    }
}