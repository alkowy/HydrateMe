package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.azmarzly.core.R
import com.azmarzly.core.R.drawable.ic_female
import com.azmarzly.core.R.drawable.ic_male
import core.common_components.GenderPicker
import core.model.Gender
import core.model.UserDataModel
import core.ui.theme.HydrateMeTheme
import core.util.RegistrationRoute
import core.util.RegistrationRoute.GENDER
import core.util.RegistrationRoute.PARAMETERS
import core.util.navigateTo

@Composable
fun GenderPickScreen(
    navController: NavController,
    registrationState: RegistrationState,
    updateUserData: (UserDataModel) -> Unit,
    navigateToHome: () -> Unit,
    changeCurrentStep: (RegistrationRoute) -> Unit,
    updateBottomBarState: (Boolean) -> Unit,
    bottomBarState: RegistrationBottomBarState,
) {

    GenderPickScreenContent(
        onNavigateToHome = navigateToHome,
        setUserGender = { gender ->
            updateUserData(
                registrationState.userModel!!.copy(
                    gender = gender
                ),
            )
        },
        onNavigateToNextStep = {
            navController.navigateTo(RegistrationRoute.ACTIVITY) {}
        },
        bottomBarState = bottomBarState,
        updateBottomBarState = updateBottomBarState,
    )

    BackHandler {
        changeCurrentStep(PARAMETERS)
        navController.popBackStack()
    }
}

@Composable
fun GenderPickScreenContent(
    onNavigateToHome: () -> Unit,
    setUserGender: (Gender) -> Unit,
    onNavigateToNextStep: () -> Unit,
    bottomBarState: RegistrationBottomBarState,
    updateBottomBarState: (Boolean) -> Unit,
) {

    var genderSelected: Gender? by remember { mutableStateOf(null) }

    RegistrationStepWithBottomBar(
        bottomBarState = bottomBarState.copy(
            currentStep = GENDER
        ),
        onSkip = onNavigateToHome,
        onNext = {
            setUserGender(genderSelected!!)
            onNavigateToNextStep()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GenderPicker(
                    gender = Gender.MALE,
                    imageId = ic_male,
                    onClick = { genderSelected = it },
                    isSelected = genderSelected == Gender.MALE

                )
                GenderPicker(
                    gender = Gender.FEMALE,
                    imageId = ic_female,
                    onClick = { genderSelected = it },
                    isSelected = genderSelected == Gender.FEMALE
                )
            }
        }

    }
    LaunchedEffect(genderSelected) {
        updateBottomBarState(genderSelected != null)
    }

}

@Preview(showBackground = true)
@Composable
fun GenderPickerScreenPreview() {
    HydrateMeTheme {
        GenderPickScreenContent(
            onNavigateToHome = {},
            setUserGender = {},
            onNavigateToNextStep = {},
            bottomBarState = RegistrationBottomBarState(),
            updateBottomBarState = {}
        )
    }
}