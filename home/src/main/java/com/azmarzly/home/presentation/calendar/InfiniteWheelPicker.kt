package com.azmarzly.home.presentation.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfiniteWheelTimePicker(
    items: List<Int>,
    initialIndex: Int = 0,
    visibleItems: Int = 3,
    onValueChange: (selectedItem: Int) -> Unit
) {
    val totalItems = items.size
    val initialPosition = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % totalItems) + initialIndex

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialPosition
    )

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val selectedIndex = remember {
        derivedStateOf {
            val firstVisible = listState.firstVisibleItemIndex % items.size
            if (firstVisible < 0) firstVisible + items.size else firstVisible
        }
    }

    LaunchedEffect(selectedIndex.value) {
        onValueChange(items[selectedIndex.value])
    }

    val itemHeight = 40.dp
    val totalHeight = itemHeight * visibleItems

    Box(
        modifier = Modifier
            .height(totalHeight)
            .width(80.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(vertical = (itemHeight * (visibleItems - 1) / 2))
        ) {
            items(Int.MAX_VALUE) { index ->
                val actualIndex = index % items.size
                val item = items[actualIndex]
                Text(
                    text = item.toString().padStart(2, '0'),
                    style = if (actualIndex == selectedIndex.value) {
                        MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.primary)
                    } else {
                        MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                )
                .align(Alignment.Center)
        )
    }
}