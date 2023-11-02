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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import core.common_components.PlaneValidatedTextField
import core.ui.theme.Grey400
import core.ui.theme.HydrateMeTheme
import core.ui.theme.bodySmall
import core.util.RegistrationRoute
import core.util.RegistrationRoute.GOAL

@Composable
fun GoalScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
    navigateToHome: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
) {
    GoalScreenContent(
        onNavigateToHome = navigateToHome,
        bottomBarState = bottomBarState,
        updateBottomBarState = updateBottomBarState
    )
    BackHandler {
        registrationViewModel.changeCurrentStep(RegistrationRoute.ACTIVITY)
        navController.popBackStack()
    }

}

@Composable
fun GoalScreenContent(
    onNavigateToHome: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
) {

    val goal = rememberSaveable { mutableStateOf("") }

    RegistrationStepWithBottomBar(
        bottomBarState = bottomBarState.copy(currentStep = GOAL),
        onSkip = onNavigateToHome,
        onNext = onNavigateToHome
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Wyliczony na podstawie uzupełnionych przez Ciebie danych",
                style = MaterialTheme.typography.bodySmall,
                color = Grey400,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PlaneValidatedTextField(
                    value = goal,
                    onValueChange = {},
                    label = "Cel",
                    style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center),
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth(0.3f)
                )
                Text(
                    text = "L",
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalScreenPreview() {
    HydrateMeTheme {
        GoalScreenContent(onNavigateToHome = { }, bottomBarState = RegistrationBottomBarState(), updateBottomBarState = {})
    }
}