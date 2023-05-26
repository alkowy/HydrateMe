package com.azmarzly.registration.presentation

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
fun GenderPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    GenderPickScreenContent(
        onNavigateToAgeInfo = {
            navController.navigateTo(RegistrationRoute.AGE) {

            }
        },
    )
}

@Composable
fun GenderPickScreenContent(
    onNavigateToAgeInfo: () -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "GenderPickScreenContent")
        Button(onClick = onNavigateToAgeInfo) {
            Text(text = "Navigate to age pick info screen")
        }
    }
}