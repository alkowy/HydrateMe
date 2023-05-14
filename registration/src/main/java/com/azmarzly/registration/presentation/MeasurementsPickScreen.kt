package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
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
fun MeasurementsPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    MeasurementsPickScreenContent(
        onNavigateToGenderInfo = {
            navController.navigate(Routes.RegistrationRoute.GENDER.name) {
                popUpTo(Routes.RegistrationRoute.GENDER.name) { inclusive = true }
            }
        },
        onNavigateToActivityInfo = {
            navController.navigate(Routes.RegistrationRoute.ACTIVITY.name)

        }
    )
}

@Composable
fun MeasurementsPickScreenContent(
    onNavigateToGenderInfo: () -> Unit,
    onNavigateToActivityInfo: () -> Unit,
) {

    BackHandler(onBack = onNavigateToGenderInfo)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "MeasurementsPickScreenContent")
        Button(onClick = onNavigateToGenderInfo) {
            Text(text = "Navigate to Gender info screen")
        }
        Button(onClick = onNavigateToActivityInfo) {
            Text(text = "Navigate to activity info screen")
        }
    }
}