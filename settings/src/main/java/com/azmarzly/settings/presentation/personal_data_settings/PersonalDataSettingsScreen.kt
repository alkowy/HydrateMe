package com.azmarzly.settings.presentation.personal_data_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azmarzly.core.R
import com.azmarzly.settings.presentation.ClickableText
import com.azmarzly.settings.presentation.ParametersPickDialog
import com.azmarzly.settings.presentation.SettingsPickerDialogWithContent
import com.azmarzly.settings.presentation.SettingsSubScreenCard
import com.azmarzly.settings.presentation.SettingsSubScreenHeader
import core.common_components.DatePickerDialog
import core.common_components.GenderPicker
import core.common_components.RoundedButtonWithContent
import core.model.Gender
import core.model.GenderState
import core.ui.theme.shadowedTextColor
import java.time.LocalDate

@Composable
fun PersonalDataSettingsScreen(
    viewModel: PersonalDataSettingsViewModel = hiltViewModel(),
    closeScreen: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PersonalDataSettingsScreenContent(
        state = state,
        closeScreen = closeScreen,
        saveChanges = viewModel::saveChanges,
        updateGender = viewModel::updateGender,
        updateWeight = viewModel::updateWeight,
        validateWeight = viewModel::onWeightChanged,
        handleDatePicked = viewModel::handleDatePicked,
        updateHeight = viewModel::updateHeight,
        validateHeight = viewModel::onHeightChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataSettingsScreenContent(
    state: PersonalDataModel,
    closeScreen: () -> Unit,
    saveChanges: () -> Unit,
    updateGender: (Gender?) -> Unit,
    updateWeight: (String) -> Unit,
    validateWeight: (String) -> Unit,
    updateHeight: (String) -> Unit,
    validateHeight: (String) -> Unit,
    handleDatePicked: (Long?) -> Unit,
) {
    val currentYear = LocalDate.now().year
    val scrollState = rememberScrollState()
    var showGenderPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showWeightPicker by remember { mutableStateOf(false) }
    var showHeightPicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        yearRange = 1900..currentYear,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        if (showGenderPicker) {
            GenderPickerDialog(
                onDismiss = { showGenderPicker = false },
                confirm = { gender ->
                    updateGender(gender)
                    showGenderPicker = false
                },
                genders = state.genders,
            )
        }
        if (showDatePicker) {
            DatePickerDialog(
                onDismiss = { showDatePicker = false },
                onConfirm = {
                    showDatePicker = false
                    handleDatePicked(datePickerState.selectedDateMillis)
                },
                datePickerState = datePickerState
            )
        }

        if (showWeightPicker) {
            ParametersPickDialog(
                parameterTitle = stringResource(id = R.string.parameters_weight),
                parameterSubtitle = null,
                label = stringResource(id = R.string.parameters_weight),
                unit = stringResource(id = R.string.unit_kg),
                onDismiss = {
                    showWeightPicker = false
                },
                validateInput = validateWeight,
                isInputValid = state.isWeightInputValid,
                confirm = { weight ->
                    updateWeight(weight)
                    showWeightPicker = false
                },
                maxCharacters = 5,
            )
        }
        if (showHeightPicker) {
            ParametersPickDialog(
                parameterTitle = stringResource(id = R.string.parameters_height),
                parameterSubtitle = null,
                label = stringResource(id = R.string.parameters_height),
                unit = stringResource(id = R.string.unit_cm),
                onDismiss = {
                    showHeightPicker = false
                },
                validateInput = validateHeight,
                isInputValid = state.isHeightInputValid,
                confirm = { height ->
                    updateHeight(height)
                    showHeightPicker = false
                },
                maxCharacters = 5,
            )
        }

        SettingsSubScreenHeader(
            headerText = stringResource(id = R.string.registration_personal_data),
            closeScreen = closeScreen
        )
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSubScreenCard(
            label = stringResource(id = R.string.gender),
            value = state.gender,
            onClickAction = { showGenderPicker = true },
            trailingItem = {
                ClickableText(
                    text = stringResource(id = R.string.choose),
                    onClick = { showGenderPicker = true }
                )
            }
        )
        SettingsSubScreenCard(
            label = stringResource(id = R.string.birth_date),
            value = state.birthDate,
            onClickAction = { showDatePicker = true },
            trailingItem = {
                ClickableText(
                    text = stringResource(id = R.string.choose),
                    onClick = { showDatePicker = true }
                )
            }
        )
        SettingsSubScreenCard(
            label = stringResource(id = R.string.parameters_weight),
            value = state.weight,
            onClickAction = { showWeightPicker = true },
            trailingItem = {
                Text(
                    text = stringResource(id = R.string.unit_kg),
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.shadowedTextColor
                    )
                )
            }
        )
        SettingsSubScreenCard(
            label = stringResource(id = R.string.parameters_height),
            value = state.height,
            onClickAction = { showHeightPicker = true },
            trailingItem = {
                Text(
                    text = stringResource(id = R.string.unit_cm),
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.shadowedTextColor
                    )
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        RoundedButtonWithContent(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                saveChanges()
                closeScreen()
            },
        ) {
            Text(
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.button.copy(
                    color = MaterialTheme.colors.onPrimary
                )
            )
        }

    }
}

@Composable
fun GenderPickerDialog(onDismiss: () -> Unit, confirm: (Gender?) -> Unit, genders: List<GenderState>) {
    SettingsPickerDialogWithContent(
        headerTitle = stringResource(id = R.string.gender),
        headerSubTitle = stringResource(id = R.string.choose_one),
        onDismiss = onDismiss,
        content = {
            var genderSelected: Gender? by remember { mutableStateOf(null) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                genders.forEach { genderState ->
                    GenderPicker(
                        genderName = genderState.name,
                        imageId = genderState.genderIcon,
                        onClick = { genderSelected = genderState.gender },
                        isSelected = genderSelected == genderState.gender

                    )
                }
            }
            RoundedButtonWithContent(
                onClick = { confirm(genderSelected) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = genderSelected != null,
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    )
}