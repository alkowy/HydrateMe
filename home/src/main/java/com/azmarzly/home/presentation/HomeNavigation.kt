package com.azmarzly.home.presentation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.azmarzly.home.components.HomeScreenContent
import com.azmarzly.profile.presentation.ProfileScreen
import core.util.HomeRoute
import core.util.Route
import core.util.SignInRoute
import core.util.navigateTo

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute.HOME.route,
        route = HomeRoute.HOME_ROOT.route
    ) {
        composable(route = HomeRoute.HOME.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val homeScreenState by viewModel.homeState.collectAsStateWithLifecycle()

            HomeScreenContent(
                homeState = homeScreenState,
                fetchCurrentUserData = viewModel::fetchCurrentUser,
                addWater = viewModel::addWater,
            )
        }
        composable(route = HomeRoute.CALENDAR.route) {
            Text(text = "calendar")
            CalendarScreen()
        }
        composable(route = HomeRoute.PROFILE.route) {
            Text(text = "PRofile")
            ProfileScreen()
        }
        composable(route = HomeRoute.NEWS.route) {
            Text(text = "News")
            HealthNewsScreen()
        }
    }

}