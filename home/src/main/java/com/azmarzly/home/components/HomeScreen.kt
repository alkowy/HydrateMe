package com.azmarzly.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.azmarzly.core.R
import com.azmarzly.home.presentation.HomeState
import com.azmarzly.home.presentation.HomeViewModel
import core.ui.theme.HydrateMeTheme
import core.util.clickableOnce
import core.util.doNothing

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    bottomBarPadding: Dp,
    navigateToProfile: () -> Unit
) {
    val homeScreenState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeScreenContent(
        homeState = homeScreenState,
        addWater = { water -> viewModel.addHydration(amountOfWaterAdded = water) },
        bottomBarPadding = bottomBarPadding,
        navigateToProfile = navigateToProfile,
    )
}

@Composable
fun HomeScreenContent(
    homeState: HomeState,
    addWater: (Int) -> Unit,
    bottomBarPadding: Dp,
    navigateToProfile: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 45.dp, bottom = bottomBarPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "${stringResource(R.string.hello_header)} ${homeState.userData?.name ?: ""}!",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(homeState.userData?.profileImageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_profile_selected),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .clickableOnce { navigateToProfile() }
            )

        }
        Spacer(modifier = Modifier.height(45.dp))

        HomeHydrationProgressCard(
            homeState = homeState,
            onAddHydrationAction = addWater,
        )

        HomeTodaysHydrationChunks(todayHydrationChunks = homeState.todayHydrationChunks)

        Spacer(modifier = Modifier.height(12.dp))

        HomeCompactCalendar(hydrationData = homeState.userData?.hydrationData ?: emptyList())

    }
}

@Preview
@Composable
fun HomeContentPreview() {
    HydrateMeTheme() {
        HomeScreenContent(homeState = HomeState(), addWater = { doNothing() }, 0.dp, {})
    }
}