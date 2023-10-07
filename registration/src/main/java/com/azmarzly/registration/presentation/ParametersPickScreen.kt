package com.azmarzly.registration.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.util.RegistrationRoute
import core.util.navigateTo
import java.time.LocalDateTime

@Composable
fun ParametersPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
    navigateToHome: () -> Unit,
) {

    val state by registrationViewModel.registrationState.collectAsStateWithLifecycle()

    ParametersPickContent(
        setUserParameters = { weight, height, birthDate ->
            registrationViewModel.updateUserDataAndMoveToStep(
                userModel = state.userModel!!.copy(
                    weight = weight,
                    height = height,
                    birthDate = birthDate
                ),
                nextStep = RegistrationRoute.GENDER,
            )
        },
        onNavigateToHome = navigateToHome,
    )

    BackHandler {

    }
    LaunchedEffect(state.currentStep) {
        Log.d("ANANAS", "Parameters pick screen: $state")
        if (state.currentStep != RegistrationRoute.PARAMETERS && state.currentStep != RegistrationRoute.INITIAL) {
            navController.navigateTo(state.currentStep) {
            }
        }
    }
}

@Composable
fun ParametersPickContent(
    onNavigateToHome: () -> Unit,
    setUserParameters: (Double, Double, LocalDateTime) -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Parameters pick content 2")
        //todo extract from fields
        Button(onClick = { setUserParameters(2.2, 2.2, LocalDateTime.now()) }) {
            Text(text = "DALEJ - to gender")
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
        onNavigateToHome = {},
        setUserParameters = { _, _, _ -> Unit }
    )
}