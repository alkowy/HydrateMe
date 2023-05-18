package com.azmarzly.hydrateme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azmarzly.landing_page.LandingPageScreen
import com.azmarzly.scanner.presentation.ScannerScreen
import com.azmarzly.sign_in.SignInScreen
import core.util.Routes

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LandingPage.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LandingPage.route) {
            LandingPageScreen(navController)
        }
        registrationGraph(navController)

        composable(Routes.Scanner.route) {
            ScannerScreen()
        }
        composable(Routes.SignIn.route) {
            SignInScreen(navController)
        }
        composable(Routes.Home.route) {
//            HomeScreen(navController)
        }
    }

}