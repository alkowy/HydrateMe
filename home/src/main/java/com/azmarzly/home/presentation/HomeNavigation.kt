package com.azmarzly.home.presentation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.azmarzly.home.components.HomeScreen
import com.azmarzly.home.components.HomeScreenContent
import com.azmarzly.profile.presentation.ProfileScreen
import core.util.HomeRoute

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    navigateToSignIn: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute.HOME.route,
        route = HomeRoute.HOME_ROOT.route
    ) {
        composable(route = HomeRoute.HOME.route) {
            HomeScreen(homeViewModel)
        }
        composable(route = HomeRoute.CALENDAR.route) {
            Text(text = "calendar")
            CalendarScreen()
        }
        composable(route = HomeRoute.PROFILE.route) {
            Text(text = "PRofile")
            ProfileScreen(
                navigateToSignIn = { navigateToSignIn() }
            )

        }
        composable(route = HomeRoute.NEWS.route) {
            Text(text = "News")
            HealthNewsScreen()
        }
    }

}