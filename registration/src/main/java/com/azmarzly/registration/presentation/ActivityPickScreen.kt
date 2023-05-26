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

@Composable
fun ActivityPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    ActivityPickScreenContent(
        onNavigateToMeasurementsHeightInfo = {
            navController.navigateTo(RegistrationRoute.MEASUREMENTS_HEIGHT){}
        }
    )
}

@Composable
fun ActivityPickScreenContent(
    onNavigateToMeasurementsHeightInfo: () -> Unit,
    //  onFinaliseRegistration: () -> Unit,

) {
    BackHandler(onBack = onNavigateToMeasurementsHeightInfo)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "ActivityPickScreenContent")
        Button(onClick = onNavigateToMeasurementsHeightInfo) {
            Text(text = "Navigate to MEasurements height info screen")
        }
    }
}