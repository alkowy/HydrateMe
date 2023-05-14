package com.azmarzly.registration.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import core.util.Routes
import core.util.navigateWithinRegistration

@Composable
fun InitialRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    InitialRegistrationScreenContent(
        onNavigateToGenderInfo = {
            //  navController.navigateWithinRegistration(Routes.RegistrationRoute.GENDER.name)
            navController.navigate(Routes.RegistrationRoute.GENDER.name) {
                popUpTo(Routes.RegistrationRoute.INITIAL.name) { inclusive = true }
            }
        }
    )
}

@Composable
fun InitialRegistrationScreenContent(
    onNavigateToGenderInfo: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "INITIAL REGISTRATION")
        Button(onClick = onNavigateToGenderInfo) {
            Text(text = "Navigate to Gender info screen")
        }
    }
}