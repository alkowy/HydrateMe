package com.azmarzly.registration.presentation

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.model.Gender
import core.util.HomeRoute
import core.util.RegistrationRoute
import core.util.navigateTo

@Composable
fun GenderPickScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {

    val state by registrationViewModel.registrationState.collectAsStateWithLifecycle()

    GenderPickScreenContent(
        onNavigateToAgeInfo = {
            navController.navigateTo(RegistrationRoute.AGE) {}
        },
        onNavigateToHome = {
            navController.popBackStack()
            navController.navigateTo(HomeRoute.HOME_ROOT) {}
        },
        setUserGender = { gender ->
            registrationViewModel.updateUserDataAndMoveToStep(
                userModel = state.userModel!!.copy(
                    gender = gender
                ),
                nextStep = RegistrationRoute.AGE
            )
        }
    )

    LaunchedEffect(state.currentStep) {
        Log.d("ANANAS", "GenderPickScreen: $state")
        if (state.currentStep != RegistrationRoute.GENDER && state.currentStep != RegistrationRoute.INITIAL) {
            navController.navigateTo(state.currentStep) {}
        }
    }
}

@Composable
fun GenderPickScreenContent(
    onNavigateToAgeInfo: () -> Unit,
    onNavigateToHome: () -> Unit,
    setUserGender: (Gender) -> Unit,
) {

    var genderSelected: Gender? by remember { mutableStateOf(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "GenderPickScreenContent")

        GenderPicker(
            gender = Gender.MALE,
            imageId = com.azmarzly.core.R.drawable.ic_add,
            onClick = { genderSelected = it },
            isSelected = genderSelected == Gender.MALE

        )
        GenderPicker(
            gender = Gender.FEMALE,
            imageId = com.azmarzly.core.R.drawable.ic_add,
            onClick = { genderSelected = it },
            isSelected = genderSelected == Gender.FEMALE
        )

        Button(
            modifier = Modifier,
            onClick = { setUserGender(genderSelected!!) },
            enabled = genderSelected != null
        ) {
            Text(text = "Dalej")
        }

        Button(onClick = { onNavigateToHome() }) {
            Text(text = "Pomiń")
        }

        Button(onClick = onNavigateToAgeInfo) {
            Text(text = "Navigate to age pick info screen")
        }
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

    Column(
        modifier = modifier
            .clickable { onClick(gender) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "checkedGender"
            )
        }
        Image(painter = painterResource(id = imageId), contentDescription = gender.name)
        Text(text = gender.name.uppercase())
    }
}