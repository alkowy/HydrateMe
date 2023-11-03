package com.azmarzly.registration.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import core.model.UserActivity
import core.model.UserDataModel
import core.model.toUserActivityEnum
import core.ui.theme.Grey400
import core.ui.theme.HydrateMeTheme
import core.ui.theme.backgroundContainer
import core.ui.theme.bodySmall
import core.ui.theme.registrationTextColor
import core.util.RegistrationRoute
import core.util.navigateTo

@Composable
fun ActivityPickScreen(
    navController: NavController,
    registrationState: RegistrationState,
    changeCurrentStep: (RegistrationRoute) -> Unit,
    bottomBarState: RegistrationBottomBarState,
    navigateToHome: () -> Unit,
    updateBottomBarState: (Boolean) -> Unit,
    updateUserData: (UserDataModel) -> Unit,
) {
    ActivityPickScreenContent(
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
                .background(MaterialTheme.colors.background)
                .padding(start = 20.dp, end = 20.dp, bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            ActivityCard(
                activity = UserActivity.VeryLowActivity(),
                onClick = { activitySelected = it },
                isSelected = activitySelected == UserActivity.VeryLowActivity()
            )
            ActivityCard(
                activity = UserActivity.LowActivity(),
                onClick = { activitySelected = it },
                isSelected = activitySelected == UserActivity.LowActivity()
            )
            ActivityCard(
                activity = UserActivity.AverageActivity(),
                onClick = { activitySelected = it },
                isSelected = activitySelected == UserActivity.AverageActivity()
            )
            ActivityCard(
                activity = UserActivity.HighActivity(),
                onClick = { activitySelected = it },
                isSelected = activitySelected == UserActivity.HighActivity()
            )
        }
    }

    LaunchedEffect(activitySelected) {
        updateBottomBarState(activitySelected != null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCard(
    activity: UserActivity,
    isSelected: Boolean = false,
    onClick: (UserActivity) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.backgroundContainer
        ),
        onClick = { onClick(activity) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = activity.name,
                style = MaterialTheme.typography.h4,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.registrationTextColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodySmall,
                color = Grey400
            )
        }
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
            updateUserActivity = {}
        )
    }
}