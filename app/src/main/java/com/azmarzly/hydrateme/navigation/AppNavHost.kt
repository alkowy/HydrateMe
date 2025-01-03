package com.azmarzly.hydrateme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azmarzly.home.presentation.ParentHomeScreen
import com.azmarzly.landing_page.LandingPageScreen
import com.azmarzly.registration.presentation.RegistrationFlow
import com.azmarzly.sign_in.SignInScreen
import core.util.HomeRoute
import core.util.LandingPageRoute
import core.util.RegistrationRoute
import core.util.Route
import core.util.SignInRoute
import core.util.navigateTo

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Route = LandingPageRoute,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(LandingPageRoute.route) {
            LandingPageScreen(navController)
        }
        composable(RegistrationRoute.REGISTRATION_ROOT.route) {
            RegistrationFlow(navController)
        }
        composable(SignInRoute.route) {
            SignInScreen(navController)
        }
        composable(HomeRoute.HOME_ROOT.route) {
            ParentHomeScreen(
                navigateToSignIn = {
                    navController.navigateTo(SignInRoute) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}