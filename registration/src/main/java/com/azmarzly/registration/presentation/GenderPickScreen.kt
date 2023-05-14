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
fun GenderPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    GenderPickScreenContent(
        onNavigateToMeasurementsInfo = {
            navController.navigate(Routes.RegistrationRoute.MEASUREMENTS.name)
        },

    )
}

@Composable
fun GenderPickScreenContent(
    onNavigateToMeasurementsInfo: () -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "GenderPickScreenContent")
        Button(onClick = onNavigateToMeasurementsInfo) {
            Text(text = "Navigate to measurements info screen")
        }
    }
}