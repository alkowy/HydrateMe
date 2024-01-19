package com.azmarzly.hydration_hub.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azmarzly.core.R
import com.azmarzly.hydration_hub.R.drawable.ic_collapse_arrow
import com.azmarzly.hydration_hub.R.drawable.ic_expand_arrow
import core.ui.theme.HydrateMeTheme

@Composable
fun HydrationHubScreen(
    bottomBarPadding: Dp,
    viewModel: HydrationHubViewModel = hiltViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    HydrationHubScreenContent(
        bottomBarPadding = bottomBarPadding,
        state = state,
        fetchContent = viewModel::fetchContent,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HydrationHubScreenContent(
    bottomBarPadding: Dp,
    state: HydrationHubState,
    fetchContent: () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { fetchContent() }
    )
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .verticalScroll(scrollState)
                .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = bottomBarPadding),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HydrationHubHeader()

            HydrationSectionCard(
                header = "Header",
                body = state.toString()
            )
            HydrationSectionCard(
                header = "Header",
                body = "BodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBody"
            )
            HydrationSectionCard(
                header = "Header",
                body = "BodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBody"
            )
            HydrationSectionCard(
                header = "Header",
                body = "BodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBodyBody"
            )
        }
        PullRefreshIndicator(
            modifier = Modifier
                .align(TopCenter)
                .padding(top = 16.dp),
            refreshing = state.isLoading,
            state = pullRefreshState
        )

    }
}

@Composable
fun HydrationSectionCard(header: String, body: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val localDensity = LocalDensity.current
    val bodyTextStyle = MaterialTheme.typography.body1.copy(
        color = MaterialTheme.colors.onBackground
    )
    val blurHeight: Dp = with(localDensity) { bodyTextStyle.lineHeight.toDp() }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colors.background
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = header,
                style = MaterialTheme.typography.h3.copy(
                    color = MaterialTheme.colors.onBackground
                )
            )

            Box {
                Text(
                    text = body,
                    style = bodyTextStyle,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                )
                if (isExpanded.not()) {
                    Spacer(
                        modifier = Modifier
                            .align(BottomCenter)
                            .fillMaxWidth()
                            .height(blurHeight)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MaterialTheme.colors.background)
                                )
                            )
                    )
                }
            }

            IconButton(
                modifier = Modifier.align(CenterHorizontally),
                onClick = { isExpanded = isExpanded.not() }
            ) {
                Column(
                    horizontalAlignment = CenterHorizontally,
                ) {
                    Text(
                        text = if (isExpanded) stringResource(id = R.string.collapse) else stringResource(id = R.string.expand),
                        style = MaterialTheme.typography.body1.copy(
                            color = MaterialTheme.colors.primary
                        )
                    )
                    Icon(
                        painterResource(id = if (isExpanded) ic_collapse_arrow else ic_expand_arrow),
                        contentDescription = "Toggle expansion",
                        tint = MaterialTheme.colors.primary,
                    )
                }
            }
        }
    }

}

@Composable
fun HydrationHubHeader(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.hydration_hub_header),
        style = MaterialTheme.typography.h3.copy(
            color = MaterialTheme.colors.onBackground
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HydrationHubPreview() {
    HydrateMeTheme {
        HydrationHubScreenContent(bottomBarPadding = 20.dp, state = HydrationHubState(), fetchContent = {})
    }
}