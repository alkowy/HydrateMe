package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import core.util.HomeRoute
import core.util.RegistrationRoute
import core.util.navigateTo
import core.util.popUpTo

@Composable
fun ParametersPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
    navigateToHome: () -> Unit,
) {
    ParametersPickContent(
        onNavigateToGenderInfo = {
            navController.navigateTo(RegistrationRoute.GENDER) {
                popUpTo(RegistrationRoute.GENDER) { inclusive = true }
            }
        },
        onNavigateToHome = navigateToHome,
    )
}

@Composable
fun ParametersPickContent(
    onNavigateToGenderInfo: () -> Unit,
    onNavigateToHome: () -> Unit,
) {

    BackHandler(onBack = onNavigateToGenderInfo)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Parameters pick content 2")
        Button(onClick = onNavigateToGenderInfo) {
            Text(text = "Navigate to Gender info screen")
        }
        Button(onClick = { onNavigateToHome() }) {
            Text(text = "Pomiń")
        }
    }
}

@Preview
@Composable
fun ParametersPickContentPreview() {
    ParametersPickContent(
        onNavigateToGenderInfo = {},
        onNavigateToHome = {}
    )
}