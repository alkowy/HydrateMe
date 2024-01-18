package com.azmarzly.settings.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azmarzly.settings.presentation.account_settings.AccountSettingsScreen
import com.azmarzly.settings.presentation.personal_data_settings.PersonalDataSettingsScreen
import com.azmarzly.settings.presentation.personalisation_settings.AccountPersonalisationSettingsScreen
import core.util.SettingsRoute
import core.util.navigateTo

@Composable
fun SettingsNavGraph(
    navController: NavHostController = rememberNavController(),
    closeSettings: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = SettingsRoute.MAIN.route,
    ) {
        composable(route = SettingsRoute.MAIN.route) {
            SettingsScreen(
                navigateToSettingsSection = { route ->
                    navController.navigateTo(route) {}
                },
                closeSettingsScreen = closeSettings
            )
        }
        composable(route = SettingsRoute.PERSONAL_DATA.route) {
            PersonalDataSettingsScreen(closeScreen = { navController.popBackStack() })
        }
        composable(route = SettingsRoute.ACCOUNT_PERSONALISATION.route) {
            AccountPersonalisationSettingsScreen(closeScreen = { navController.popBackStack() })
        }
        composable(route = SettingsRoute.ACCOUNT.route) {
            AccountSettingsScreen(closeScreen = { navController.popBackStack() })
        }
        composable(route = SettingsRoute.PRIVACY_POLICY.route) {
//            PrivacyPolicySettingsScreen()
        }
    }
}