package com.azmarzly.registration.presentation

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.azmarzly.registration.R
import core.model.Gender
import core.model.UserDataModel
import core.ui.theme.HydrateMeTheme
import core.ui.theme.backgroundContainer
import core.util.RegistrationRoute
import core.util.RegistrationRoute.GENDER
import core.util.RegistrationRoute.PARAMETERS
import core.util.navigateTo
import java.util.Locale

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
                    imageId = R.drawable.ic_male,
                    onClick = { genderSelected = it },
                    isSelected = genderSelected == Gender.MALE

                )
                GenderPicker(
                    gender = Gender.FEMALE,
                    imageId = R.drawable.ic_female,
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

@Composable
fun GenderPicker(
    modifier: Modifier = Modifier,
    gender: Gender,
    @DrawableRes imageId: Int,
    onClick: (Gender) -> Unit,
    isSelected: Boolean = false,
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .size(165.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.backgroundContainer
        )
    ) {
        Column(
            modifier = modifier
                .clickable { onClick(gender) }
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(painter = painterResource(id = imageId), contentDescription = gender.name)
            Text(
                text = gender.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                style = MaterialTheme.typography.h4,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground
            )
        }
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