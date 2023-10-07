package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import core.util.HomeRoute
import core.util.RegistrationRoute
import core.util.navigateTo
import core.util.popUpTo

@Composable
fun ActivityPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    ActivityPickScreenContent(
        onNavigateToGoalScreen = {
            navController.navigateTo(RegistrationRoute.GOAL) {
                popUpTo(RegistrationRoute.GOAL) { inclusive = true }
            }
        },
        onNavigateToHome = {
            navController.popBackStack()
            navController.navigateTo(HomeRoute.HOME_ROOT) {}
        },
    )
}

@Composable
fun ActivityPickScreenContent(
    onNavigateToGoalScreen: () -> Unit,
    onNavigateToHome: () -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "ActivityPickScreenContent 4 ")
        Button(onClick = onNavigateToGoalScreen) {
            Text(text = "DALEJ - Navigate to goal screen")
        }
    }
}