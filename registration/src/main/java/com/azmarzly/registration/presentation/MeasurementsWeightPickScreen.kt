package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import core.util.RegistrationRoute
import core.util.navigateTo
import core.util.popUpTo

@Composable
fun MeasurementsWeightPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    MeasurementsWeightPickScreenContent(
        onNavigateToHeightInfo = {
            navController.navigateTo(RegistrationRoute.MEASUREMENTS_HEIGHT) {
                popUpTo(RegistrationRoute.MEASUREMENTS_HEIGHT) { inclusive = true }
            }
        },
        onNavigateToActivityInfo = {
            navController.navigateTo(RegistrationRoute.ACTIVITY) {}
        }
    )
}

@Composable
fun MeasurementsWeightPickScreenContent(
    onNavigateToHeightInfo: () -> Unit,
    onNavigateToActivityInfo: () -> Unit,
) {
    BackHandler(onBack = onNavigateToHeightInfo)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "MeasurementsWeightPickScreenContent")
        Button(onClick = onNavigateToHeightInfo) {
            Text(text = "Navigate to Height info screen")
        }
        Button(onClick = onNavigateToActivityInfo) {
            Text(text = "Navigate to activity info screen")
        }
    }
}