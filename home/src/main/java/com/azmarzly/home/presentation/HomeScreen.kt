package com.azmarzly.home.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.azmarzly.home.components.AddWaterDialog
import com.azmarzly.home.components.DropletButtonNavBar
import com.azmarzly.home.presentation.HomeScreenParentViewModel.Companion.homeScreens
import core.util.HomeRoute
import core.util.navigateTo
import java.time.LocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ParentHomeScreen(
    navController: NavHostController = rememberNavController(),
    navigateToSignIn: () -> Unit,
) {

    val homeViewModel: HomeViewModel = hiltViewModel()
    var showAddWaterCard by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        bottomBar = {
            DropletButtonNavBar(
                dropletButtons = homeScreens,
                barColor = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colors.onBackground.copy(alpha = 0.14f), MaterialTheme.colors.background),
                    startY = 0.0f,
                    endY = 100.0f
                ),
                onNavigateToHome = {
                    navController.popBackStack()
                    navController.navigateTo(HomeRoute.HOME_ROOT) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToCalendar = {
                    navController.navigateTo(HomeRoute.CALENDAR) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigateTo(HomeRoute.PROFILE) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToNews = {
                    navController.navigateTo(HomeRoute.NEWS) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onFloatingActionButtonAction = { showAddWaterCard = true }
            )
        }
    ) { paddingValues ->
        HomeNavGraph(
            navController = navController,
            navigateToSignIn = navigateToSignIn,
            homeViewModel = homeViewModel,
            bottomBarPadding = paddingValues.calculateBottomPadding()
        )

        if (showAddWaterCard) {
            AddWaterDialog(
                onDismissRequest = { showAddWaterCard = false },
                addHydrationAction = { amount ->
                    homeViewModel.addHydration(LocalDate.now(), amount)
                    showAddWaterCard = false
                },
                validateCustomAmount = homeViewModel::validateNumber,
            )
        }
    }
}