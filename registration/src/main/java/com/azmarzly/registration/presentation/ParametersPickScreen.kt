package com.azmarzly.registration.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.common_components.PlaneValidatedTextField
import core.model.UserDataModel
import core.util.RegistrationRoute
import core.util.navigateTo
import java.time.LocalDateTime

@Composable
fun ParametersPickScreen(
    navController: NavController,
    updateUserDataAndMoveToStep: (UserDataModel, RegistrationRoute) -> Unit,
    registrationState: RegistrationState,
    navigateToHome: () -> Unit,
    parametersViewModel: RegistrationParametersViewModel = hiltViewModel(),
) {

    val paramatersState by parametersViewModel.paramatersState.collectAsStateWithLifecycle()

    ParametersPickContent(
        state = paramatersState,
        setUserParameters = { weight, height, birthDate ->
            updateUserDataAndMoveToStep(
                registrationState.userModel!!.copy(
                    weight = weight,
                    height = height,
                    birthDate = birthDate
                ),
                RegistrationRoute.GENDER,
            )
        },
        onNavigateToHome = navigateToHome,
        validateWeight = parametersViewModel::validateWeight,
        validateHeight = parametersViewModel::validateHeight,
    )

    LaunchedEffect(registrationState.currentStep) {
        Log.d("ANANAS", "Parameters pick screen: $registrationState")
        if (registrationState.currentStep != RegistrationRoute.PARAMETERS && registrationState.currentStep != RegistrationRoute.INITIAL) {
            navController.navigateTo(registrationState.currentStep) {
            }
        }
    }
}

@Composable
fun ParametersPickContent(
    state: RegistrationParametersState,
    onNavigateToHome: () -> Unit,
    setUserParameters: (Double?, Double?, LocalDateTime?) -> Unit,
    validateWeight: (String) -> Unit,
    validateHeight: (String) -> Unit,
) {

    val weight = rememberSaveable { mutableStateOf("") }
    val height = rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            Text(text = "Weight")
            PlaneValidatedTextField(
                value = weight,
                onValueChange = validateWeight,
                label = "Waga",
                style = MaterialTheme.typography.caption,
                keyboardType = KeyboardType.Decimal
            )

        }
        Row {
            Text(text = "Height")
            PlaneValidatedTextField(
                value = height,
                onValueChange = validateHeight,
                label = "Wzrost",
                style = MaterialTheme.typography.caption,
                keyboardType = KeyboardType.Number
            )
        }
        Text(text = "State is: ${state.toString()}")
        //todo extract from fields
        Button(onClick = { setUserParameters(weight.value.toDoubleOrNull(), height.value.toDoubleOrNull(), LocalDateTime.now()) }) {
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
        state = RegistrationParametersState(),
        onNavigateToHome = {},
        setUserParameters = { _, _, _ -> Unit },
        validateWeight = { _ -> Unit },
        validateHeight = { _ -> Unit },
    )
}