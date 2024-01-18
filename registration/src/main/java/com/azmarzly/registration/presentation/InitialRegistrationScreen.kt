package com.azmarzly.registration.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.azmarzly.core.R
import androidx.compose.ui.unit.dp
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
    registerUser: (String, String, UserDataModel) -> Unit,
    registrationState: RegistrationState,
    navigateToSignIn: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
) {

    InitialRegistrationScreenContent(
        state = registrationState,
        bottomBarState = bottomBarState,
        registerWithEmailAndPassword = registerUser,
        navigateToSignIn = navigateToSignIn,
        updateBottomBarState = updateBottomBarState,
    )

    LaunchedEffect(registrationState.isRegistrationSuccessful) {
        if (registrationState.isRegistrationSuccessful) {
            navController.navigateTo(RegistrationRoute.PARAMETERS) {
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
    navigateToSignIn: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
) {

    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val buttonEnabled = remember {
        derivedStateOf { name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() }
    }

    RegistrationStepWithBottomBar(
        bottomBarState = bottomBarState,
        onSkip = navigateToSignIn,
        onNext = {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ValidatedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                isSecured = false,
                value = name,
                imeAction = ImeAction.Next,
                onValueChange = {
                    doNothing()
                },
                label = stringResource(id = R.string.name),
                style = MaterialTheme.typography.caption,
                keyboardType = KeyboardType.Text
            )
            ValidatedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                isSecured = false,
                value = email,
                imeAction = ImeAction.Next,
                onValueChange = {
                    doNothing()
                },
                label = stringResource(id = R.string.email_label),
                style = MaterialTheme.typography.caption,
                keyboardType = KeyboardType.Email
            )
            ValidatedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                isSecured = true,
                value = password,
                imeAction = ImeAction.Next,
                onValueChange = {
                    doNothing()
                },
                label = stringResource(id = R.string.password),
                style = MaterialTheme.typography.caption,
                keyboardType = KeyboardType.Password
            )

            if (state.error != null) {
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(horizontal = 32.dp),
                    text = state.error,
                    style = MaterialTheme.typography.caption,
                    color = Color.Red
                )
            }
        }
    }

    LaunchedEffect(buttonEnabled.value) {
        updateBottomBarState(buttonEnabled.value)
    }
}

@Preview
@Composable
fun InitialRegistrationScreenPrev() {
    HydrateMeTheme() {
        InitialRegistrationScreenContent(
            state = RegistrationState(currentStep = RegistrationRoute.INITIAL),
            registerWithEmailAndPassword = { _, _, _ -> Unit },
            navigateToSignIn = {},
            bottomBarState = RegistrationBottomBarState(),
            updateBottomBarState = { _ -> }
        )
    }
}