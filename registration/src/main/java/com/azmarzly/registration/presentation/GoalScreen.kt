package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.azmarzly.core.R
import core.common_components.PlaneValidatedTextField
import core.model.UserDataModel
import core.ui.theme.Grey400
import core.ui.theme.HydrateMeTheme
import core.ui.theme.bodySmall
import core.util.RegistrationRoute
import core.util.RegistrationRoute.GOAL

@Composable
fun GoalScreen(
    navController: NavController,
    registrationState: RegistrationState,
    navigateToHome: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
    updateUserData: (UserDataModel) -> Unit,
    changeCurrentStep: (RegistrationRoute) -> Unit,
    viewModel: GoalScreenViewModel = hiltViewModel(),
) {

    val isGoalValid by viewModel.isInputValid.collectAsStateWithLifecycle()

    GoalScreenContent(
        onNavigateToHome = navigateToHome,
        bottomBarState = bottomBarState,
        updateBottomBarState = updateBottomBarState,
        updateHydrationGoalData = { goal ->
            updateUserData(
                registrationState.userModel!!.copy(
                    hydrationGoalMillis = goal.times(1000).toInt(),
                ),
            )
        },
        validateGoal = viewModel::validateGoal,
        isGoalValid = isGoalValid,
    )

    BackHandler {
        changeCurrentStep(RegistrationRoute.ACTIVITY)
        navController.popBackStack()
    }

}

@Composable
fun GoalScreenContent(
    onNavigateToHome: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
    updateHydrationGoalData: (Double) -> Unit,
    validateGoal: (String) -> Unit,
    isGoalValid: Boolean,
) {

    val goal = rememberSaveable { mutableStateOf("") }

    RegistrationStepWithBottomBar(
        bottomBarState = bottomBarState.copy(currentStep = GOAL),
        onSkip = onNavigateToHome,
        onNext = {
            updateHydrationGoalData(goal.value.toDouble())
            onNavigateToHome()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                text = stringResource(R.string.daily_water_intake_calculated),
//                style = MaterialTheme.typography.bodySmall,
//                color = Grey400,
//                textAlign = TextAlign.Center
//            )

            Spacer(modifier = Modifier.height(1.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PlaneValidatedTextField(
                    value = goal,
                    onValueChange = validateGoal,
                    label = stringResource(id = R.string.goal),
                    style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center),
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    maxCharacters = 4,
                )
                Text(
                    text = stringResource(id = R.string.unit_liter),
                    style = MaterialTheme.typography.caption
                )
            }
        }

        LaunchedEffect(isGoalValid) {
            updateBottomBarState(isGoalValid)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalScreenPreview() {
    HydrateMeTheme {
        GoalScreenContent(
            onNavigateToHome = { },
            bottomBarState = RegistrationBottomBarState(),
            updateBottomBarState = {},
            updateHydrationGoalData = {},
            validateGoal = {},
            isGoalValid = true
        )
    }
}