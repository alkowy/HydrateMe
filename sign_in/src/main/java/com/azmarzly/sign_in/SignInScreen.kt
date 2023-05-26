package com.azmarzly.sign_in

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.model.Resource
import core.model.UserDataModel
import core.util.HomeRoute
import core.util.RegistrationRoute
import core.util.doNothing
import core.util.navigateTo

@Composable
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = hiltViewModel()
    val state by viewModel.authState.collectAsStateWithLifecycle()

    SignInScreenContent(
        state = state,
        onNavigateToHome = { navController.navigateTo(HomeRoute.HOME_ROOT) {} },
        onNavigateToRegistration = { navController.navigateTo(RegistrationRoute.REGISTRATION_ROOT){} },
        onLogin = viewModel::loginWithEmailAndPassword
    )

}

@Composable
fun SignInScreenContent(
    state: Resource<UserDataModel>,
    onNavigateToHome: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    onLogin: (String, String) -> Unit
) {

    Column() {
        Text(text = " sign in screen" + state.toString())
        Button(onClick = { onLogin("test@gmail.com", "12345qwerty") }) {
            Text(text = "Login")
        }
    }


//    LaunchedEffect(state) {
//        when (state) {
//            Resource.EmptyState -> {
//                doNothing()
//            }
//
//            is Resource.Error -> {//show some error dialog
//            }
//
//            Resource.Loading -> { // blur
//            }
//
//            is Resource.Success -> {
////                onNavigateToHome()
//                onNavigateToRegistration() // todo remove, was here for testing
//            }
//        }
//    }
}