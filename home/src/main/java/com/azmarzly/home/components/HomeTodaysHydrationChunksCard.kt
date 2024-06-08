package com.azmarzly.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import core.model.DrinkType
import core.model.HydrationData
import core.ui.theme.shadowedTextColor
import core.util.toHourAndMinutes
import java.time.LocalDateTime

@Composable
fun HomeTodaysHydrationChunks(todayHydrationChunks: List<HydrationData.HydrationChunk>) {
    val scrollState = rememberScrollState()
    AnimatedVisibility(visible = todayHydrationChunks.isEmpty().not()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background
            )
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = CenterVertically,
            ) {
                todayHydrationChunks.forEach {
                    HomeHydrationChunkItem(drinkType = it.amountToDrinkType(), date = it.dateTime)
                }
            }
        }
    }
}

@Composable
fun HomeHydrationChunkItem(drinkType: DrinkType, date: LocalDateTime) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = drinkType.resIdSelected), contentDescription = "",
            tint = MaterialTheme.colors.primary
        )
        Text(
            text = date.toHourAndMinutes(),
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.shadowedTextColor
            )
        )
    }

}