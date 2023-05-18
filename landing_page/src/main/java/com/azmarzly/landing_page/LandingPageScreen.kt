package com.azmarzly.landing_page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.util.Routes

@Composable
fun LandingPageScreen(navController: NavController) {

    val landingPageViewModel: LandingPageViewModel = hiltViewModel()
    val state by landingPageViewModel.state.collectAsStateWithLifecycle()

    LandingPageContent(
        state = state,
        onNavigateToHome = { navController.navigate(Routes.Home.route) },
        onNavigateToSignIn = { navController.navigate(Routes.SignIn.route) },
    )
}

@Composable
fun LandingPageContent(
    state: LandingPageState,
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    when (state) {
        LandingPageState.Loading -> LoadingLandingPageScreen()
        LandingPageState.LoggedIn -> onNavigateToHome()
        LandingPageState.NotLoggedIn -> onNavigateToSignIn()
    }

}