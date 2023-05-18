package com.azmarzly.hydrateme.navigation

import android.annotation.SuppressLint
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
import core.util.Routes

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.registrationGraph(navController: NavController) {
    navigation(startDestination = Routes.RegistrationRoute.INITIAL.name, route = Routes.Registration.route) {
        composable(Routes.RegistrationRoute.INITIAL.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.Registration.route)
            }
            val registrationViewModel: RegistrationViewModel = hiltViewModel(parentEntry)
            InitialRegistrationScreen(navController, registrationViewModel)
        }
        composable(Routes.RegistrationRoute.GENDER.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.Registration.route)
            }
            val registrationViewModel: RegistrationViewModel = hiltViewModel(parentEntry)
            GenderPickScreen(navController = navController, registrationViewModel = registrationViewModel)

        }
        composable(Routes.RegistrationRoute.AGE.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.Registration.route)
            }
            val registrationViewModel: RegistrationViewModel = hiltViewModel(parentEntry)
            AgePickScreen(navController = navController, registrationViewModel = registrationViewModel)

        }
        composable(Routes.RegistrationRoute.MEASUREMENTS_HEIGHT.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.Registration.route)
            }
            val registrationViewModel: RegistrationViewModel = hiltViewModel(parentEntry)
            MeasurementsHeightPickScreen(navController = navController, registrationViewModel = registrationViewModel)

        }
        composable(Routes.RegistrationRoute.MEASUREMENTS_WEIGHT.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.Registration.route)
            }
            val registrationViewModel: RegistrationViewModel = hiltViewModel(parentEntry)
            MeasurementsWeightPickScreen(navController = navController, registrationViewModel = registrationViewModel)

        }
        composable(Routes.RegistrationRoute.ACTIVITY.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Routes.Registration.route)
            }
            val registrationViewModel: RegistrationViewModel = hiltViewModel(parentEntry)
            ActivityPickScreen(navController = navController, registrationViewModel = registrationViewModel)

        }
    }
}

