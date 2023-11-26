package com.azmarzly.home.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azmarzly.core.R
import com.azmarzly.home.presentation.HomeState
import com.azmarzly.home.presentation.HomeViewModel
import core.model.ScanData
import core.model.UrineColor
import core.model.UrineScanData
import core.ui.theme.HydrateMeTheme
import core.ui.theme.VeryDarkBlue
import core.ui.theme.caption
import core.util.doNothing
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeScreenState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeScreenContent(
        homeState = homeScreenState,
        fetchCurrentUserData = viewModel::fetchCurrentUser,
        addWater = { water -> viewModel.addHydration(amountOfWaterAdded = water) },
    )
}

@Composable
fun HomeScreenContent(
    homeState: HomeState,
    modifier: Modifier = Modifier,
    fetchCurrentUserData: () -> Unit,
    addWater: (Int) -> Unit,
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 45.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Witaj ${homeState.userData?.name ?: ""}!", // todo extract string resource
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onSurface,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_profile_unselected),
                contentDescription = "",
                modifier = Modifier.clip(CircleShape)
            )

        }
        Spacer(modifier = Modifier.height(45.dp))

        HomeHydrationProgressCard(
            homeState = homeState,
            onAddHydrationAction = addWater,
        )
        Spacer(modifier = Modifier.height(12.dp))

        HomeCompactCalendar(hydrationData = homeState.userData?.hydrationData ?: emptyList())

        Spacer(modifier = Modifier.height(24.dp))

        UrineScanCard(
            UrineScanData(
                LocalDateTime.now(),
                listOf(
                    ScanData(color = UrineColor.OK, message = "Nawodnienie dobre", hour = "12:23"),
                    ScanData(color = UrineColor.BAD, message = "Nawodnienie niewystarczające", hour = "15:23"),
                    ScanData(color = UrineColor.BAD, message = "Nawodnienie niewystarczające", hour = "16:27"),
                    ScanData(color = UrineColor.BAD, message = "Nawodnienie niewystarczające", hour = "18:23")

                )
            )
        )
        //homeState.userData!!.urineScanData.last()) // find todays date


    }
    LaunchedEffect(Unit) {
        Log.d("ANANAS", "HomeScreenContent: launchedeffect Unit $homeState")
//         fetchCurrentUserData() // commented out, called in vm init block. probably to be removed
    }
    LaunchedEffect(homeState) {
        Log.d("ANANAS", "HomeScreenContent: launchedeffect homeState: $homeState")
    }
}

@Composable
fun UrineScanCard(urineScanData: UrineScanData?) {
    Column() {
        Text(
            text = "Skan moczu", // todo extract string resource
            style = MaterialTheme.typography.h3.copy(color = VeryDarkBlue),
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colors.onSurface
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                IconButton(
                    onClick = { doNothing() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_add_circle), contentDescription = "add")
                }
                urineScanData?.let {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        urineScanData.scanInfo.forEach { item ->
                            UrineDataRow(scanData = item)
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun UrineDataRow(scanData: ScanData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = scanData.hour,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.caption
        )
        Divider(
            color = MaterialTheme.colors.caption,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
                .width(1.dp)
        )
        Canvas(modifier = Modifier.size(32.dp)) {
            drawCircle(color = scanData.color.toColor())
        }
        Text(
            text = scanData.message,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.caption,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview
@Composable
fun UrineDataRowPreview() {
    HydrateMeTheme() {
        UrineDataRow(
            scanData = ScanData(color = UrineColor.OK, message = "Nawodnienie dobre", hour = "12:23"),
        )
    }
}

@Preview
@Composable
fun UrineScanPreview() {
    HydrateMeTheme {
        UrineScanCard(
            UrineScanData(
                LocalDateTime.now(),
                listOf(
                    ScanData(color = UrineColor.OK, message = "Nawodnienie dobre", hour = "12:23"),
                    ScanData(
                        color = UrineColor.BAD,
                        message = "Nawodnienie niewystarczającesdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsd",
                        hour = "15:23"
                    )
                )
            )
        )
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    HydrateMeTheme() {
        HomeScreenContent(homeState = HomeState(), fetchCurrentUserData = { doNothing() }, addWater = { doNothing() })
    }
}