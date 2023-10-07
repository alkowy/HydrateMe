package com.azmarzly.registration.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import core.util.HomeRoute
import core.util.RegistrationRoute
import core.util.SignInRoute
import core.util.navigateTo

@Composable
fun RegistrationFlow(navController: NavHostController) {
    val registrationViewModel: RegistrationViewModel = hiltViewModel()
    val registrationNavController = rememberNavController()

    val registrationState by registrationViewModel.registrationState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            RegistrationTopBar(currentStep = registrationState.currentStep, registrationState.currentStep != RegistrationRoute.INITIAL)
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            RegistrationNavHost(
                parentNavController = navController,
                registrationNavController = registrationNavController,
                registrationViewModel = registrationViewModel,
            )
        }
    }
}

@Composable
fun RegistrationNavHost(
    parentNavController: NavController,
    registrationNavController: NavHostController,
    registrationViewModel: RegistrationViewModel,
) {
    NavHost(navController = registrationNavController, startDestination = RegistrationRoute.INITIAL.route) {
        composable(RegistrationRoute.INITIAL.route) {
            InitialRegistrationScreen(registrationNavController, registrationViewModel,
                navigateToSignIn = {
                    parentNavController.popBackStack()
                    parentNavController.navigateTo(SignInRoute) {
                        parentNavController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
        composable(RegistrationRoute.PARAMETERS.route) {
            ParametersPickScreen(
                registrationNavController, registrationViewModel,
                navigateToHome = {
                    parentNavController.popBackStack()
                    parentNavController.navigateTo(HomeRoute.HOME_ROOT) {}
                }
            )
        }
        composable(RegistrationRoute.GENDER.route) {
            GenderPickScreen(registrationNavController, registrationViewModel,
                navigateToHome = {
                    parentNavController.popBackStack()
                    parentNavController.navigateTo(HomeRoute.HOME_ROOT) {}
                })
        }
        composable(RegistrationRoute.ACTIVITY.route) {
            ActivityPickScreen(registrationNavController, registrationViewModel)
        }
        composable(RegistrationRoute.GOAL.route) {
            //GoalPickScreen(navController, registrationViewModel(navController))
//            MeasurementsWeightPickScreen(navController, registrationViewModel(navController))
        }
    }
}

//todo can be removed?
//@SuppressLint("UnrememberedGetBackStackEntry")
//@Composable
//private fun registrationViewModel(navController: NavController): RegistrationViewModel {
//    val parentEntry = remember {
//        navController.getBackStackEntry(RegistrationRoute.REGISTRATION_ROOT.route)
//    }
//    return hiltViewModel(parentEntry)
//}