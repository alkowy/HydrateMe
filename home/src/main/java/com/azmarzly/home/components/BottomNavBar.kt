package com.azmarzly.home.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.BallAnimation
import com.exyte.animatednavbar.animation.balltrajectory.Teleport
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import core.ui.theme.HydrateMeTheme
import core.util.doNothing

data class HomeScreenBottomBarItem(
    val description: String = "description",
    val iconUnselected: Int,
    val iconSelected: Int,
)

const val Duration = 500
const val DoubleDuration = 1000


@Composable
fun DropletButtonNavBar(
    dropletButtons: List<HomeScreenBottomBarItem>,
    ballAnimation: BallAnimation = Teleport(tween(Duration, easing = LinearOutSlowInEasing)),
    barColor: Color = MaterialTheme.colors.background,
    dropletColor: Color = MaterialTheme.colors.primary,
    unselectedIconColor: Color = MaterialTheme.colors.onBackground,
    onNavigateToHome: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNews: () -> Unit,
    onFloatingActionButtonAction: () -> Unit,
) {
    var selectedItem by remember { mutableStateOf(0) }

    Box(modifier = Modifier) {
        AnimatedNavigationBar(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 0.dp)
                .height(60.dp),
            selectedIndex = selectedItem,
            ballColor = dropletColor,
            cornerRadius = shapeCornerRadius(topLeft = 25.dp, topRight = 25.dp, bottomLeft = 0.dp, bottomRight = 0.dp),
            ballAnimation = ballAnimation,
            indentAnimation = Height(
                indentWidth = 56.dp,
                indentHeight = 15.dp,
                animationSpec = tween(
                    DoubleDuration,
                    easing = { OvershootInterpolator().getInterpolation(it) })
            ),
            barColor = barColor
        ) {
            dropletButtons.forEachIndexed { index, it ->
                if (index == dropletButtons.size.div(2)) {
                    InvisButton()
                } else {
                    DropletButton(
                        modifier = Modifier.fillMaxSize(),
                        isSelected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = if (selectedItem == index) it.iconSelected else it.iconUnselected,
                        iconColor = unselectedIconColor,
                        dropletColor = dropletColor,
                        animationSpec = tween(durationMillis = Duration, easing = LinearEasing)
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { onFloatingActionButtonAction() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-32).dp),
            backgroundColor = MaterialTheme.colors.primary,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)

        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_filled),
                contentDescription = "fab",
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }



    LaunchedEffect(selectedItem) {
        when (selectedItem) {
            0 -> onNavigateToHome()
            1 -> onNavigateToCalendar()
            3 -> onNavigateToProfile()
            4 -> onNavigateToNews()
        }
    }
}

@Composable
fun InvisButton() {
    Button(
        onClick = { doNothing() },
        modifier = Modifier.alpha(0f),
        enabled = false,
    ) {}
}

@Preview()
@Composable
fun BottomNavBarPreview() {
    HydrateMeTheme() {
        DropletButtonNavBar(
            dropletButtons = listOf(
                HomeScreenBottomBarItem(
                    description = "Home",
                    iconSelected = R.drawable.ic_home_selected,
                    iconUnselected = R.drawable.ic_home_unselected
                ),
                HomeScreenBottomBarItem(
                    description = "Calendar",
                    iconSelected = R.drawable.ic_calendar_selected,
                    iconUnselected = R.drawable.ic_calendar_unselected
                ),
                HomeScreenBottomBarItem(
                    description = "Profile",
                    iconSelected = R.drawable.ic_profile_selected,
                    iconUnselected = R.drawable.ic_profile_unselected
                ),
                HomeScreenBottomBarItem(
                    description = "News",
                    iconSelected = R.drawable.ic_news_selected,
                    iconUnselected = R.drawable.ic_news_unselected
                ),
                HomeScreenBottomBarItem(
                    description = "Home",
                    iconSelected = R.drawable.ic_home_selected,
                    iconUnselected = R.drawable.ic_home_unselected
                ),
            ),
            onNavigateToHome = { /*TODO*/ },
            onNavigateToCalendar = { /*TODO*/ },
            onNavigateToProfile = { /*TODO*/ },
            onNavigateToNews = {/*TODO*/ },
            onFloatingActionButtonAction = {/*TODO*/ }
        )
    }
}
