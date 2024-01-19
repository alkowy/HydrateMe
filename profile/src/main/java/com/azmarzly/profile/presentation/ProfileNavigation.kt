package com.azmarzly.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azmarzly.settings.presentation.SettingsNavGraph
import core.util.ProfileRoute
import core.util.navigateTo

@Composable
fun ProfileNavGraph(
    navController: NavHostController = rememberNavController(),
    bottomBarPadding: Dp,
    setBottomBarVisibility: (Boolean) -> Unit,
    navigateToSignIn: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = ProfileRoute.PROFILE.route,
    ) {
        composable(route = ProfileRoute.PROFILE.route) {
            LaunchedEffect(Unit) {
                setBottomBarVisibility(true)
            }
            ProfileScreen(
                bottomBarPadding = bottomBarPadding,
                navigateToSettings = {
                    navController.navigateTo(ProfileRoute.SETTINGS) {}
                    setBottomBarVisibility(false)
                },
            )
        }

        composable(route = ProfileRoute.SETTINGS.route) {
            SettingsNavGraph(
                closeSettings = { navController.popBackStack() },
                navigateToSignIn = navigateToSignIn
            )
        }
    }
}
