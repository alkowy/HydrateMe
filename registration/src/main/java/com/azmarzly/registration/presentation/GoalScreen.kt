package com.azmarzly.registration.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import core.util.HomeRoute
import core.util.navigateTo

@Composable
fun GoalScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    GoalScreenContent(
        onNavigateToHome = {
            navController.popBackStack()
            navController.navigateTo(HomeRoute.HOME_ROOT) {}
        },
    )
}

@Composable
fun GoalScreenContent(
    onNavigateToHome: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "GOAL SCREEN  5 ")
        Button(onClick = onNavigateToHome) {
            Text(text = "navigate to home - finish")
        }
    }
}