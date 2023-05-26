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
fun MeasurementsHeightPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    MeasurementsHeightPickScreenContent(
        onNavigateToAgeInfo = {
            navController.navigateTo(RegistrationRoute.AGE) {
                popUpTo(RegistrationRoute.AGE) { inclusive = true }
            }
        },
        onNavigateToMeasurementsWeightPick = {
            navController.navigateTo(RegistrationRoute.MEASUREMENTS_WEIGHT) {}
        }
    )
}

@Composable
fun MeasurementsHeightPickScreenContent(
    onNavigateToAgeInfo: () -> Unit,
    onNavigateToMeasurementsWeightPick: () -> Unit,
) {

    BackHandler(onBack = onNavigateToAgeInfo)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "MeasurementsHeightPickScreenContent")
        Button(onClick = onNavigateToAgeInfo) {
            Text(text = "Navigate to age info screen")
        }
        Button(onClick = onNavigateToMeasurementsWeightPick) {
            Text(text = "Navigate to to weight pick info screen")
        }
    }
}