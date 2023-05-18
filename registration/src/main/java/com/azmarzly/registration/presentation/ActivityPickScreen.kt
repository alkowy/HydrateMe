package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import core.util.Routes

@Composable
fun ActivityPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    ActivityPickScreenContent(
        onNavigateToMeasurementsHeightInfo = {
            navController.navigate(Routes.RegistrationRoute.MEASUREMENTS_HEIGHT.name)
        }
    )
}

@Composable
fun ActivityPickScreenContent(
    onNavigateToMeasurementsHeightInfo: () -> Unit,
    //  onFinaliseRegistration: () -> Unit,

    ) {
    BackHandler(onBack = onNavigateToMeasurementsHeightInfo )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "ActivityPickScreenContent")
        Button(onClick = onNavigateToMeasurementsHeightInfo) {
            Text(text = "Navigate to MEasurements height info screen")
        }
    }
}