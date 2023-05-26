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
fun AgePickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    AgePickScreenContent(
        onNavigateToGenderInfo = {
            navController.navigateTo(RegistrationRoute.GENDER) {
                popUpTo(RegistrationRoute.GENDER) { inclusive = true }
            }
        },
        onNavigateToMeasurementsHeightPick = {
            navController.navigateTo(RegistrationRoute.MEASUREMENTS_HEIGHT) {}
        }
    )
}

@Composable
fun AgePickScreenContent(
    onNavigateToGenderInfo: () -> Unit,
    onNavigateToMeasurementsHeightPick: () -> Unit,
) {

    BackHandler(onBack = onNavigateToGenderInfo)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Age pick content")
        Button(onClick = onNavigateToGenderInfo) {
            Text(text = "Navigate to Gender info screen")
        }
        Button(onClick = onNavigateToMeasurementsHeightPick) {
            Text(text = "Navigate to to height pick info screen")
        }
    }
}