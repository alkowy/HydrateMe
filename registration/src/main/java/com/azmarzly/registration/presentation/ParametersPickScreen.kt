package com.azmarzly.registration.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
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
import core.common_components.RoundedButtonWithContent
import core.input_validators.ValidationState
import core.model.UserDataModel
import core.ui.theme.HydrateMeTheme
import core.ui.theme.shadowedTextColor
import core.util.RegistrationRoute
import core.util.navigateTo
import java.time.LocalDate
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
        handleDatePicked = parametersViewModel::handleDatePickedStateChanged,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersPickContent(
    state: RegistrationParametersState,
    onNavigateToHome: () -> Unit,
    onNavigateToNextStep: () -> Unit,
    setUserParameters: (Double?, Double?, LocalDate?) -> Unit,
    validateWeight: (String) -> Unit,
    validateHeight: (String) -> Unit,
    handleDatePicked: (Long?) -> Unit,
    validateDate: (String) -> Unit,
    bottomBarState: RegistrationBottomBarState,
) {

    val currentYear = LocalDate.now().year
    val weight = rememberSaveable { mutableStateOf("") }
    val height = rememberSaveable { mutableStateOf("") }
    val day = rememberSaveable { mutableStateOf("") }
    val month = rememberSaveable { mutableStateOf("") }
    val year = rememberSaveable { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        yearRange = 1900..currentYear,
    )

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
                state = state,
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
            Log.d("ANANAS", "ParametersPickContent: showDatePickerDialog $showDatePickerDialog")
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
            Log.d("ANANAS", "ParametersPickContent:         LaunchedEffect(state.selectedDate) {\n")
            state.selectedDate?.let {
                day.value = it.dayOfMonth.toString()
                month.value = it.month.value.toString()
                year.value = it.year.toString()
            }
        }

        LaunchedEffect(day.value, month.value, year.value) {
            Log.d("ANANAS", "ParametersPickContent:        LaunchedEffect(day.value, month.value, year.value) {\n ")
            validateDate("${year.value}-${month.value}-${day.value}")
        }
    }
}

@Composable
private fun HeightRow(height: MutableState<String>, validateHeight: (String) -> Unit) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlaneValidatedTextField(
            value = height,
            onValueChange = validateHeight,
            label = "Wzrost",
            style = MaterialTheme.typography.body1,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            trailingIcon = {
                Text(
                    "cm",
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.shadowedTextColor
                    )
                )
            }
        )

    }
}

@Composable
private fun WeightRow(weight: MutableState<String>, validateWeight: (String) -> Unit) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlaneValidatedTextField(
            value = weight,
            onValueChange = validateWeight,
            label = "Waga",
            style = MaterialTheme.typography.body1,
            keyboardType = KeyboardType.Decimal,
            modifier = Modifier.weight(1f),
            trailingIcon = {
                Text(
                    "kg",
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.shadowedTextColor
                    ),
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }
        )

    }
}

@Composable
private fun AgeRow(
    day: MutableState<String>,
    month: MutableState<String>,
    year: MutableState<String>,
    onCalendarClick: () -> Unit,
    state: RegistrationParametersState,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = CenterVertically,
    ) {
        PlaneValidatedTextField(
            modifier = Modifier.weight(1f),
            value = day,
            onValueChange = {},
            label = "DD",
            style = MaterialTheme.typography.body1,
            keyboardType = KeyboardType.Number,
            isError = state.dateValidationState == ValidationState.Invalid,
        )
        PlaneValidatedTextField(
            modifier = Modifier.weight(1f),
            value = month,
            onValueChange = {},
            label = "MM",
            style = MaterialTheme.typography.body1,
            keyboardType = KeyboardType.Number,
            isError = state.dateValidationState == ValidationState.Invalid
        )
        PlaneValidatedTextField(
            modifier = Modifier.weight(1f),
            value = year,
            onValueChange = {},
            label = "YYYY",
            style = MaterialTheme.typography.body1,
            keyboardType = KeyboardType.Number,
            isError = state.dateValidationState == ValidationState.Invalid
        )
        IconButton(onClick = onCalendarClick) {
            Icon(
                imageVector = Icons.Default.CalendarMonth, contentDescription = "calendar",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, datePickerState: DatePickerState) {
    Log.d("ANANAS", "DatePickerDialog: ")
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            RoundedButtonWithContent(onClick = onConfirm) {
                Text(text = "Potwierdź")
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colors.background,
            subheadContentColor = MaterialTheme.colors.onBackground,
            )
    ) {
        DatePicker(
            state = datePickerState, showModeToggle = false, title = null,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colors.background,
                selectedDayContainerColor = MaterialTheme.colors.primary,
                selectedDayContentColor = MaterialTheme.colors.onPrimary,
                todayDateBorderColor = MaterialTheme.colors.primary,
                dayContentColor = MaterialTheme.colors.onBackground,
                todayContentColor = MaterialTheme.colors.onBackground,
                dayInSelectionRangeContentColor = MaterialTheme.colors.onBackground,
                yearContentColor = MaterialTheme.colors.onBackground,
                titleContentColor = MaterialTheme.colors.onBackground,
                headlineContentColor = MaterialTheme.colors.onBackground,
                currentYearContentColor = MaterialTheme.colors.onBackground,
                selectedYearContainerColor = MaterialTheme.colors.primary,
                selectedYearContentColor = MaterialTheme.colors.onPrimary,
                subheadContentColor = MaterialTheme.colors.onBackground,
                )
        )
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ParametersPickContentPreviewDark() {
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