package com.azmarzly.registration.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            RegistrationTopBar(
                currentStep = registrationState.currentStep,
                showBackButton = registrationState.currentStep !in setOf(RegistrationRoute.INITIAL, RegistrationRoute.PARAMETERS),
                onBackButtonClick = {
                    registrationNavController.popBackStack()
                    registrationViewModel.changeCurrentStep(registrationState.currentStep.previousRegistrationStep())
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .background(MaterialTheme.colors.background)
            )
        },
        containerColor = MaterialTheme.colors.background
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
    val registrationState by registrationViewModel.registrationState.collectAsStateWithLifecycle()
    val bottomRegistrationBarState by registrationViewModel.bottomNavigationBarState.collectAsStateWithLifecycle()

    NavHost(navController = registrationNavController, startDestination = RegistrationRoute.INITIAL.route) {
        composable(RegistrationRoute.INITIAL.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }

        ) {
            InitialRegistrationScreen(
                navController = registrationNavController,
                registerUser = registrationViewModel::registerWithEmailAndPassword,
                registrationState = registrationState,
                navigateToSignIn = {
                    parentNavController.popBackStack()
                    parentNavController.navigateTo(SignInRoute) {
                        parentNavController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                bottomBarState = bottomRegistrationBarState,
                updateBottomBarState = registrationViewModel::updateBottomBarButtonStatus
            )
        }
        composable(RegistrationRoute.PARAMETERS.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }) {
            ParametersPickScreen(
                navController = registrationNavController,
                updateUserData = registrationViewModel::updateUserData,
                registrationState = registrationState,
                navigateToHome = {
                    parentNavController.navigateTo(HomeRoute.HOME_ROOT) {
                        popUpTo(parentNavController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                updateBottomBarState = registrationViewModel::updateBottomBarButtonStatus,
                bottomBarState = bottomRegistrationBarState,
            )
            LaunchedEffect(Unit) {
                registrationViewModel.changeCurrentStep(RegistrationRoute.PARAMETERS)
            }
        }
        composable(RegistrationRoute.GENDER.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }) {
            GenderPickScreen(
                navController = registrationNavController,
                registrationState = registrationState,
                changeCurrentStep = registrationViewModel::changeCurrentStep,
                navigateToHome = {
                    parentNavController.navigateTo(HomeRoute.HOME_ROOT) {
                        popUpTo(parentNavController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                updateBottomBarState = registrationViewModel::updateBottomBarButtonStatus,
                updateUserData = registrationViewModel::updateUserData,
                bottomBarState = bottomRegistrationBarState,
            )
            LaunchedEffect(Unit) {
                registrationViewModel.changeCurrentStep(RegistrationRoute.GENDER)
            }
        }
        composable(RegistrationRoute.ACTIVITY.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }) {
            ActivityPickScreen(
                navController = registrationNavController,
                changeCurrentStep = registrationViewModel::changeCurrentStep,
                bottomBarState = bottomRegistrationBarState,
                navigateToHome = {
                    parentNavController.navigateTo(HomeRoute.HOME_ROOT) {
                        popUpTo(parentNavController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                updateBottomBarState = registrationViewModel::updateBottomBarButtonStatus,
                registrationState = registrationState,
                updateUserData = registrationViewModel::updateUserData
            )
            LaunchedEffect(Unit) {
                registrationViewModel.changeCurrentStep(RegistrationRoute.ACTIVITY)
            }
        }
        composable(RegistrationRoute.GOAL.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }) {
            GoalScreen(
                navController = registrationNavController,
                registrationState = registrationState,
                bottomBarState = bottomRegistrationBarState,
                updateBottomBarState = registrationViewModel::updateBottomBarButtonStatus,
                navigateToHome = {
                    parentNavController.navigateTo(HomeRoute.HOME_ROOT) {
                        popUpTo(parentNavController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                updateUserData = registrationViewModel::updateUserData,
                changeCurrentStep = registrationViewModel::changeCurrentStep
            )
            LaunchedEffect(Unit) {
                registrationViewModel.changeCurrentStep(RegistrationRoute.GOAL)
            }
        }
    }
}