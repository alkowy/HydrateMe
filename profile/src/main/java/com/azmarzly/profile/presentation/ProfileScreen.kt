package com.azmarzly.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    navigateToSignIn: () -> Unit,
) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val state by profileViewModel.profileState.collectAsStateWithLifecycle()

    ProfileContent(
        login = { email, password -> profileViewModel.login(email, password) }, // remove
        signOut = { profileViewModel.signOut() }, // remove
    )

    LaunchedEffect(state) {
        if (state is ProfileState.LoggedOut) {
            navigateToSignIn()
        }
    }
}

@Composable
fun ProfileContent(
    login: (email: String, password: String) -> Unit,// remove
    signOut: () -> Unit,// remove
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            TextField(value = email, onValueChange = { email = it })
            TextField(value = password, onValueChange = { password = it })

            Button(onClick = { login(email, password) }) {
                Text(text = "LOGIN")
            }

            Button(onClick = signOut) {
                Text(text = "SIGN OUT")
            }
        }

    }
}
