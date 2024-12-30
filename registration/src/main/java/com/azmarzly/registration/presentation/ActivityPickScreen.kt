package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.common_components.ActivityCard
import core.model.UserActivity
import core.model.UserActivityState
import core.model.UserDataModel
import core.model.toUserActivityEnum
import core.ui.theme.HydrateMeTheme
import core.util.RegistrationRoute
import core.util.navigateTo

@Composable
fun ActivityPickScreen(
    viewModel: ActivityPickScreenViewModel = hiltViewModel(),
    navController: NavController,
    registrationState: RegistrationState,
    changeCurrentStep: (RegistrationRoute) -> Unit,
    bottomBarState: RegistrationBottomBarState,
    navigateToHome: () -> Unit,
    updateBottomBarState: (Boolean) -> Unit,
    updateUserData: (UserDataModel) -> Unit,
) {

    val userActivitiesState by viewModel.userActivitiesState.collectAsStateWithLifecycle()

    ActivityPickScreenContent(
        userActivitiesState = userActivitiesState,
        onNavigateToGoalScreen = {
            navController.navigateTo(RegistrationRoute.GOAL) {}
        },
        onNavigateToHome = navigateToHome,
        bottomBarState = bottomBarState,
        updateBottomBarState = updateBottomBarState,
        updateUserActivity = { activity ->
            updateUserData(
                registrationState.userModel!!.copy(
                    userActivity = activity.toUserActivityEnum()
                )
            )
        }
    )

    BackHandler {
        changeCurrentStep(RegistrationRoute.GENDER)
        navController.popBackStack()
    }
}

@Composable
fun ActivityPickScreenContent(
    userActivitiesState: List<UserActivityState>,
    onNavigateToGoalScreen: () -> Unit,
    onNavigateToHome: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
    updateUserActivity: (UserActivity) -> Unit,
) {

    var activitySelected: UserActivity? by remember { mutableStateOf(null) }

    RegistrationStepWithBottomBar(
        bottomBarState = bottomBarState.copy(
            currentStep = RegistrationRoute.ACTIVITY,
        ),
        onSkip = onNavigateToHome,
        onNext = {
            updateUserActivity(activitySelected!!)
            onNavigateToGoalScreen()
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(start = 20.dp, end = 20.dp, bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            userActivitiesState.filterNot { it.userActivity is UserActivity.Empty }.forEach { userActivityState ->
                ActivityCard(
                    activityName = userActivityState.name,
                    activityDescription = userActivityState.description,
                    isSelected = activitySelected == userActivityState.userActivity,
                    onClick = { activitySelected = userActivityState.userActivity })
            }
        }
    }

    LaunchedEffect(activitySelected) {
        updateBottomBarState(activitySelected != null)
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityPickScreenPreview() {
    HydrateMeTheme {
        ActivityPickScreenContent(
            onNavigateToGoalScreen = {},
            onNavigateToHome = {},
            bottomBarState = RegistrationBottomBarState(),
            updateBottomBarState = { _ -> },
            updateUserActivity = {},
            userActivitiesState = listOf()
        )
    }
}