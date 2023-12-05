package com.azmarzly.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.azmarzly.home.components.HomeScreen
import com.azmarzly.home.presentation.calendar.CalendarScreen
import com.azmarzly.profile.presentation.ProfileScreen
import core.util.HomeRoute

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    navigateToSignIn: () -> Unit,
    bottomBarPadding: Dp,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute.HOME.route,
        route = HomeRoute.HOME_ROOT.route,
    ) {
        composable(route = HomeRoute.HOME.route) {
            HomeScreen(homeViewModel, bottomBarPadding)
        }
        composable(route = HomeRoute.CALENDAR.route) {
            CalendarScreen(bottomBarPadding)
        }
        composable(route = HomeRoute.PROFILE.route) {
            ProfileScreen(
                navigateToSignIn = { navigateToSignIn() }
            )
        }
        composable(route = HomeRoute.NEWS.route) {
            HealthNewsScreen()
        }
    }
}