package com.azmarzly.registration.presentation

import android.util.Log
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
fun InitialRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    InitialRegistrationScreenContent(
        onNavigateToGenderInfo = {
            navController.navigateTo(RegistrationRoute.GENDER) {
                popUpTo(RegistrationRoute.INITIAL.route) { inclusive = true }
            }
        }
    )
}


@Composable
fun InitialRegistrationScreenContent(
    onNavigateToGenderInfo: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "INITIAL REGISTRATION")
        Button(onClick = {
            onNavigateToGenderInfo()
            Log.d("ANANAS", "InitialRegistrationScreenContent: clicked")
        }) {
            Text(text = "Navigate to Gender info screen")
        }
    }
}