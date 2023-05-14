package com.azmarzly.hydrateme.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {

                //LandingPageScreen(
                // onNavigateToHome = navController.navigate(Routes.Home.route),
//                onNavigateToLoginScreen = navController.navigate(Routes.Login.route))
            }
        }
        accountCreationGraph(navController)
    }

}