package com.azmarzly.registration.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.common_components.ValidatedTextField
import core.model.UserDataModel
import core.ui.theme.HydrateMeTheme
import core.util.RegistrationRoute
import core.util.doNothing
import core.util.navigateTo

@Composable
fun InitialRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {

    val state by registrationViewModel.registrationState.collectAsStateWithLifecycle()

    InitialRegistrationScreenContent(
        state = state,
        registerWithEmailAndPassword = { email, password, userDataModel ->
            registrationViewModel.registerWithEmailAndPassword(email, password, userDataModel)
        }
    )

    LaunchedEffect(state) {
        Log.d("ANANAS", "InitialRegistrationScreen 111111: $state")
    }
    LaunchedEffect(state.currentStep) {
        Log.d("ANANAS", "InitialRegistrationScreen: $state")
        if (state.currentStep != RegistrationRoute.INITIAL) {
            navController.navigateTo(state.currentStep) {
                popUpTo(RegistrationRoute.INITIAL.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }

        }
    }
}


@Composable
fun InitialRegistrationScreenContent(
    state: RegistrationState,
    registerWithEmailAndPassword: (email: String, password: String, userDataModel: UserDataModel) -> Unit,
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "INITIAL REGISTRATION")

        ValidatedTextField(
            modifier = Modifier,
            isSecured = false,
            value = name,
            imeAction = ImeAction.Next,
            onValueChange = {
                doNothing()
            },
            label = "Imię",
            style = MaterialTheme.typography.caption
        )
        ValidatedTextField(
            modifier = Modifier,
            isSecured = false,
            value = email,
            imeAction = ImeAction.Next,
            onValueChange = {
                doNothing()
            },
            label = "E-mail",
            style = MaterialTheme.typography.caption
        )
        ValidatedTextField(
            modifier = Modifier,
            isSecured = true,
            value = password,
            imeAction = ImeAction.Next,
            onValueChange = {
                doNothing()
            },
            label = "Hasło",
            style = MaterialTheme.typography.caption
        )

        Button(onClick = {
            registerWithEmailAndPassword(
                email.value,
                password.value,
                UserDataModel(
                    name = name.value,
                    email = email.value,
                )
            )
        }

        ) {
            Text(text = "Dalej")
        }

        LaunchedEffect(state.currentStep) {

        }

        Button(onClick = {
            //  onNavigateToGenderInfo()
            Log.d("ANANAS", "InitialRegistrationScreenContent: clicked")
        }) {
            Text(text = "Navigate to Gender info screen")
        }
    }
}

@Preview
@Composable
fun InitialRegistrationScreenPrev() {
    HydrateMeTheme() {
        InitialRegistrationScreenContent(
            state = RegistrationState(currentStep = RegistrationRoute.INITIAL),
            registerWithEmailAndPassword = { _, _, _ -> Unit }
        )
    }
}