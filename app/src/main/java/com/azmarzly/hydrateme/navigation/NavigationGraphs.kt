package com.azmarzly.hydrateme.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.azmarzly.registration.presentation.ActivityPickScreen
import com.azmarzly.registration.presentation.AgePickScreen
import com.azmarzly.registration.presentation.GenderPickScreen
import com.azmarzly.registration.presentation.InitialRegistrationScreen
import com.azmarzly.registration.presentation.MeasurementsHeightPickScreen
import com.azmarzly.registration.presentation.MeasurementsWeightPickScreen
import com.azmarzly.registration.presentation.RegistrationViewModel
import core.util.RegistrationRoute

fun NavGraphBuilder.registrationGraph(navController: NavController) {
    navigation(startDestination = RegistrationRoute.INITIAL.route, route = RegistrationRoute.REGISTRATION_ROOT.route) {
        composable(RegistrationRoute.INITIAL.route) {
            InitialRegistrationScreen(navController, registrationViewModel(navController))
        }
        composable(RegistrationRoute.GENDER.route) {
            GenderPickScreen(navController, registrationViewModel(navController))
        }
        composable(RegistrationRoute.AGE.route) {
            AgePickScreen(navController, registrationViewModel(navController))
        }
        composable(RegistrationRoute.MEASUREMENTS_HEIGHT.route) {
            MeasurementsHeightPickScreen(navController, registrationViewModel(navController))
        }
        composable(RegistrationRoute.MEASUREMENTS_WEIGHT.route) {
            MeasurementsWeightPickScreen(navController, registrationViewModel(navController))
        }
        composable(RegistrationRoute.ACTIVITY.route) {
            ActivityPickScreen(navController, registrationViewModel(navController))
        }
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
private fun registrationViewModel(navController: NavController): RegistrationViewModel {
    val parentEntry = remember {
        navController.getBackStackEntry(RegistrationRoute.REGISTRATION_ROOT.route)
    }
    return hiltViewModel(parentEntry)
}