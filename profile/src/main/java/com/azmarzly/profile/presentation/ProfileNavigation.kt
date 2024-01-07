package com.azmarzly.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azmarzly.settings.presentation.SettingsNavGraph
import core.util.ProfileRoute

@Composable
fun ProfileNavGraph(
    navController: NavHostController = rememberNavController(),
    bottomBarPadding: Dp,
) {
    NavHost(
        navController = navController,
        startDestination = ProfileRoute.PROFILE.route,
    ) {
        composable(route = ProfileRoute.PROFILE.route) {
            ProfileScreen(
                bottomBarPadding = bottomBarPadding,
            )
        }
        composable(route = ProfileRoute.SETTINGS.route) {
            SettingsNavGraph()
        }
    }
}