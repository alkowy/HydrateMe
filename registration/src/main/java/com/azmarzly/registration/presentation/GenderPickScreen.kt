package com.azmarzly.registration.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import core.util.Routes

@Composable
fun GenderPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    GenderPickScreenContent(
        onNavigateToAgeInfo = {
            navController.navigate(Routes.RegistrationRoute.AGE.name)
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