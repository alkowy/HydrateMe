package com.azmarzly.registration.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import core.common_components.PlaneValidatedTextField
import core.model.UserDataModel
import core.ui.theme.HydrateMeTheme
import core.ui.theme.registrationTextColor
import core.util.RegistrationRoute
import core.util.navigateTo
import java.time.LocalDateTime

@Composable
fun ParametersPickScreen(
    navController: NavController,
    updateUserData: (UserDataModel) -> Unit,
    registrationState: RegistrationState,
    navigateToHome: () -> Unit,
    parametersViewModel: RegistrationParametersViewModel = hiltViewModel(),
    updateBottomBarState: (Boolean) -> Unit,
    bottomBarState: RegistrationBottomBarState,
) {

    val parametersState by parametersViewModel.parametersState.collectAsStateWithLifecycle()

    ParametersPickContent(
        state = parametersState,
        setUserParameters = { weight, height, birthDate ->
            updateUserData(
                registrationState.userModel!!.copy(
                    weight = weight,
                    height = height,
                    birthDate = birthDate
                ),
            )
        },
        onNavigateToNextStep = { navController.navigateTo(RegistrationRoute.GENDER) {} },
        onNavigateToHome = navigateToHome,
        validateWeight = parametersViewModel::validateWeight,
        validateHeight = parametersViewModel::validateHeight,
        handleDatePicked = parametersViewModel::handleDatePickerStateChange,
        validateDate = parametersViewModel::validateDate,
        bottomBarState = bottomBarState,
    )

    LaunchedEffect(parametersState.isNextButtonEnabled) {
        updateBottomBarState(
            parametersState.isNextButtonEnabled,
        )
    }

    LaunchedEffect(Unit) {
        updateBottomBarState(
            parametersState.isNextButtonEnabled,
        )
    }
//    LaunchedEffect(registrationState.currentStep) {
//        Log.d("ANANAS", "Parameters pick screen: $registrationState")
//        if (registrationState.currentStep != RegistrationRoute.PARAMETERS && registrationState.currentStep != RegistrationRoute.INITIAL) {
//            navController.navigateTo(registrationState.currentStep) {
//            }
//        }
//    }

//    LaunchedEffect(parametersState.weightValidationState, parametersState.heightValidationState, parametersState.selectedDate) {
//        parametersViewModel.checkIsNextButtonEnabled()
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersPickContent(
    state: RegistrationParametersState,
    onNavigateToHome: () -> Unit,
    onNavigateToNextStep: () -> Unit,
    setUserParameters: (Double?, Double?, LocalDateTime?) -> Unit,
    validateWeight: (String) -> Unit,
    validateHeight: (String) -> Unit,
    handleDatePicked: (Long?) -> Unit,
    validateDate: (String) -> Unit,
    bottomBarState: RegistrationBottomBarState,
) {

    val weight = rememberSaveable { mutableStateOf("") }
    val height = rememberSaveable { mutableStateOf("") }
    val day = rememberSaveable { mutableStateOf("") }
    val month = rememberSaveable { mutableStateOf("") }
    val year = rememberSaveable { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    RegistrationStepWithBottomBar(
        bottomBarState = bottomBarState.copy(
            currentStep = RegistrationRoute.PARAMETERS,
        ),
        onSkip = onNavigateToHome,
        onNext = {
            setUserParameters(state.weight, state.height, state.selectedDate)
            onNavigateToNextStep()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AgeRow(
                day = day,
                month = month,
                year = year,
                onCalendarClick = { showDatePickerDialog = true },
            )

            WeightRow(
                weight = weight,
                validateWeight = validateWeight
            )

            HeightRow(
                height = height,
                validateHeight = validateHeight
            )
        }

        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismiss = { showDatePickerDialog = false },
                onConfirm = {
                    showDatePickerDialog = false
                    handleDatePicked(datePickerState.selectedDateMillis)
                },
                datePickerState = datePickerState
            )
        }

        LaunchedEffect(state.selectedDate) {
            state.selectedDate?.let {
                day.value = it.dayOfMonth.toString()
                month.value = it.month.value.toString()
                year.value = it.year.toString()
            }
        }

        LaunchedEffect(day.value, month.value, year.value) {
            validateDate("${day.value}-${month.value}-${year.value}")
        }
    }
}

@Composable
private fun HeightRow(height: MutableState<String>, validateHeight: (String) -> Unit) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Wzrost",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.registrationTextColor,
        )
        PlaneValidatedTextField(
            value = height,
            onValueChange = validateHeight,
            label = "Wzrost",
            style = MaterialTheme.typography.caption,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )
        Text(
            "cm",
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
private fun WeightRow(weight: MutableState<String>, validateWeight: (String) -> Unit) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Waga",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.registrationTextColor
        )
        PlaneValidatedTextField(
            value = weight,
            onValueChange = validateWeight,
            label = "Waga",
            style = MaterialTheme.typography.caption,
            keyboardType = KeyboardType.Decimal,
            modifier = Modifier.weight(1f)
        )
        Text(
            "kg",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
    }
}

@Composable
private fun AgeRow(
    day: MutableState<String>,
    month: MutableState<String>,
    year: MutableState<String>,
    onCalendarClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = CenterVertically,
    ) {
        Text(
            text = "Wiek",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.registrationTextColor
        )
        PlaneValidatedTextField(
            modifier = Modifier.weight(1f),
            value = day,
            onValueChange = {},
            label = "DD",
            style = MaterialTheme.typography.caption,
            keyboardType = KeyboardType.Number,
        )
        PlaneValidatedTextField(
            modifier = Modifier.weight(1f),
            value = month,
            onValueChange = {},
            label = "MM",
            style = MaterialTheme.typography.caption,
            keyboardType = KeyboardType.Number
        )
        PlaneValidatedTextField(
            modifier = Modifier.weight(1f),
            value = year,
            onValueChange = {},
            label = "YYYY",
            style = MaterialTheme.typography.caption,
            keyboardType = KeyboardType.Number
        )
        IconButton(onClick = onCalendarClick) {
            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "calendar")
        }
    }

    LaunchedEffect(day.value, month.value, year.value) {
        Log.d("ANANAS", "AgeRow: launchedeffect call")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, datePickerState: DatePickerState) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Potwierdź")
            }
        }
    ) {
        DatePicker(state = datePickerState, showModeToggle = false)
    }
}

@Preview(showBackground = true)
@Composable
fun ParametersPickContentPreview() {
    HydrateMeTheme {
        ParametersPickContent(
            state = RegistrationParametersState(),
            onNavigateToHome = {},
            setUserParameters = { _, _, _ -> Unit },
            validateWeight = { _ -> Unit },
            validateHeight = { _ -> Unit },
            handleDatePicked = { _ -> LocalDateTime.now() },
            validateDate = {},
            onNavigateToNextStep = {},
            bottomBarState = RegistrationBottomBarState()
        )
    }
}