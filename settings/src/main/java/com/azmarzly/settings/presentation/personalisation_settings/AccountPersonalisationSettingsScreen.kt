package com.azmarzly.settings.presentation.personalisation_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import core.common_components.ActivityCard
import core.common_components.RoundedButtonWithContent
import core.model.UserActivity
import core.model.UserActivityState
import core.ui.theme.shadowedTextColor

@Composable
fun AccountPersonalisationSettingsScreen(
    viewModel: AccountPersonalisationSettingsViewModel = hiltViewModel(),
    closeScreen: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AccountPersonalisationSettingsScreenContent(
        state = state,
        closeScreen = closeScreen,
        saveChanges = viewModel::saveChanges,
        updateUserActivity = viewModel::updateUserActivity,
        updateHydrationGoal = viewModel::updateHydrationGoal,
        validateGoalInput = viewModel::onHydrationGoalChanged,
    )
}

@Composable
fun AccountPersonalisationSettingsScreenContent(
    state: AccountPersonalisationState,
    closeScreen: () -> Unit,
    saveChanges: () -> Unit,
    updateUserActivity: (UserActivity) -> Unit,
    updateHydrationGoal: (String) -> Unit,
    validateGoalInput: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    var showUserActivityPicker by remember { mutableStateOf(false) }
    var showHydrationGoalPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        if (showUserActivityPicker) {
            UserActivityPickerDialog(
                onDismiss = { showUserActivityPicker = false },
                onConfirm = { activity ->
                    updateUserActivity(activity)
                    showUserActivityPicker = false
                },
                defaultActivity = state.userActivity,
                userActivities = state.userActivitiesState
            )
        }

        if (showHydrationGoalPicker) {
            ParametersPickDialog(
                parameterTitle = stringResource(id = R.string.daily_water_goal),
                parameterSubtitle = null,
                label = stringResource(id = R.string.daily_water_goal),
                unit = stringResource(id = R.string.unit_liter),
                onDismiss = {
                    showHydrationGoalPicker = false
                },
                validateInput = validateGoalInput,
                isInputValid = state.isHydrationGoalInputValid,
                confirm = { goal ->
                    updateHydrationGoal(goal)
                    showHydrationGoalPicker = false
                },
                maxCharacters = 5,
            )
        }

        SettingsSubScreenHeader(
            headerText = stringResource(id = R.string.account_personalisation),
            closeScreen = closeScreen
        )
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSubScreenCard(
            label = stringResource(id = R.string.physical_activity),
            value = state.userActivity.name,
            onClickAction = { showUserActivityPicker = true },
            trailingItem = {
                ClickableText(
                    text = stringResource(id = R.string.choose),
                    onClick = { showUserActivityPicker = true }
                )
            }
        )

        SettingsSubScreenCard(
            label = stringResource(id = R.string.daily_water_goal),
            value = state.hydrationGoalUi,
            onClickAction = { showHydrationGoalPicker = true },
            trailingItem = {
                Text(
                    text = stringResource(id = R.string.unit_liter),
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
fun UserActivityPickerDialog(onDismiss: () -> Unit, onConfirm: (UserActivity) -> Unit, defaultActivity: UserActivity?, userActivities: List<UserActivityState>) {
    SettingsPickerDialogWithContent(
        headerTitle = stringResource(id = R.string.physical_activity),
        headerSubTitle = stringResource(id = R.string.choose_one),
        onDismiss = onDismiss,
        content = {
            var activitySelected: UserActivity? by remember { mutableStateOf(defaultActivity) }

            userActivities.filterNot { it.userActivity is UserActivity.Empty }.forEach {
                ActivityCard(
                    activityName = it.name,
                    activityDescription = it.description,
                    onClick = { activitySelected = it.userActivity },
                    isSelected = activitySelected == it.userActivity
                )
            }

            RoundedButtonWithContent(
                onClick = { activitySelected?.let { onConfirm(it) } },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = activitySelected != null,
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