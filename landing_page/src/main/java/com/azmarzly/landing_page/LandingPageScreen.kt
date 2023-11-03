package com.azmarzly.landing_page

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.util.HomeRoute
import core.util.SignInRoute
import core.util.doNothing
import core.util.navigateTo

@Composable
fun LandingPageScreen(navController: NavController) {

    val landingPageViewModel: LandingPageViewModel = hiltViewModel()
    val state by landingPageViewModel.state.collectAsStateWithLifecycle()

    LandingPageContent(
        state = state,
        onNavigateToHome = {
            navController.navigateTo(HomeRoute.HOME_ROOT) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        },
        onNavigateToSignIn = {
            navController.navigateTo(SignInRoute) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        },
    )

    LaunchedEffect(state) {
        when (state) {
            LandingPageState.Loading -> doNothing()
            LandingPageState.LoggedIn -> {
                navController.navigateTo(HomeRoute.HOME_ROOT) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }

            LandingPageState.NotLoggedIn -> {
                navController.navigateTo(SignInRoute) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun LandingPageContent(
    state: LandingPageState,
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val logoColor by infiniteTransition.animateColor(
        initialValue = Color.White.copy(alpha = 0.3f),
        targetValue = Color.White,
        animationSpec = infiniteRepeatable(
            tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(com.azmarzly.core.R.drawable.splash_bg),
            contentDescription = "splash_bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Image(
            painter = painterResource(com.azmarzly.core.R.drawable.hydrateme_logo),
            contentDescription = "logo",
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(logoColor)

        )
    }
}