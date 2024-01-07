package com.azmarzly.settings.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import core.util.SettingsRoute

@Composable
fun SettingsNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = SettingsRoute.MAIN.route,
    ) {
        composable(route = SettingsRoute.MAIN.route) {

        }
        composable(route = SettingsRoute.PERSONAL_DATA.route) {

        }
        composable(route = SettingsRoute.PERSONALISATION.route) {

        }
        composable(route = SettingsRoute.ACCOUNT.route) {

        }
        composable(route = SettingsRoute.PRIVACY_POLICY.route) {

        }
    }
}