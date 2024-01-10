package com.azmarzly.home.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import com.azmarzly.home.presentation.HomeScreenParentViewModel.Companion.homeScreens
import com.exyte.animatednavbar.animation.balltrajectory.BallAnimation
import com.exyte.animatednavbar.animation.balltrajectory.Teleport
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import core.ui.theme.HydrateMeTheme
import core.util.HomeRoute
import core.util.doNothing

data class HomeScreenBottomBarItem(
    val description: String = "description",
    val iconUnselected: Int,
    val iconSelected: Int,
    val route: HomeRoute,
)

const val Duration = 500
const val DoubleDuration = 1000


@Composable
fun DropletButtonNavBar(
    dropletButtons: List<HomeScreenBottomBarItem>,
    shouldShowBottomBar: Boolean,
    ballAnimation: BallAnimation = Teleport(tween(Duration, easing = LinearOutSlowInEasing)),
    barColor: Brush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.1f)),
        startY = 0.0f,
        endY = 400.0f
    ),// = MaterialTheme.colors.background,
    dropletColor: Color = MaterialTheme.colors.primary,
    unselectedIconColor: Color = MaterialTheme.colors.onBackground,
    selectedScreenIndex: Int,
    onNavigateToScreen: (HomeRoute) -> Unit,
    onFloatingActionButtonAction: () -> Unit,
) {

    AnimatedVisibility(
        visible = shouldShowBottomBar,
        enter = slideInVertically {
            it.plus(100)
        },
        exit = slideOutVertically {
            it.minus(100)
        } + fadeOut(tween(250)),
    ) {
        Box {
            AnimatedNavigationBar(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(60.dp),
                selectedIndex = selectedScreenIndex,
                ballColor = dropletColor,
                cornerRadius = shapeCornerRadius(topLeft = 25.dp, topRight = 25.dp, bottomLeft = 25.dp, bottomRight = 25.dp),
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
                            isSelected = selectedScreenIndex == index,
                            onClick = { onNavigateToScreen(it.route) },
                            icon = if (selectedScreenIndex == index) it.iconSelected else it.iconUnselected,
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
                    .offset(y = (-12).dp),
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
            dropletButtons = homeScreens,
            selectedScreenIndex = 0,
            onNavigateToScreen = {},
            onFloatingActionButtonAction = {},
            shouldShowBottomBar = true,
        )
    }
}