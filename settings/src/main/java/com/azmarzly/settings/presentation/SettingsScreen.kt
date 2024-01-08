package com.azmarzly.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azmarzly.core.R
import core.util.SettingsRoute


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToSettingsSection: (SettingsRoute) -> Unit,
    closeSettingsScreen: () -> Unit,
) {
    val state by viewModel.settingsState.collectAsStateWithLifecycle()

    SettingsScreenContent(
        state = state,
        navigateToSettingsSection = navigateToSettingsSection,
        closeSettingsScreen = closeSettingsScreen
    )
}

@Composable
fun SettingsScreenContent(
    state: SettingsUiState,
    navigateToSettingsSection: (SettingsRoute) -> Unit,
    closeSettingsScreen: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingsHeader(
            closeSettings = closeSettingsScreen,
        )
        Spacer(modifier = Modifier.height(8.dp))
        PersonalDataCard(
            gender = state.gender,
            birthDate = state.birthDate,
            weight = state.weight,
            height = state.height,
        )
        AccountPersonalisationCard(
            activity = state.activity,
            dailyGoal = state.hydrationGoal,
        )
        AccountCard()
        PrivacyPolicyCard()
    }
}

@Composable
fun PrivacyPolicyCard() {
    SettingsSectionCard {
        SettingsSectionHeaderRow(title = stringResource(id = R.string.privacy_policy))
    }
}

@Composable
fun AccountCard() {
    SettingsSectionCard {
        SettingsSectionHeaderRow(title = stringResource(id = R.string.account))
    }
}

@Composable
fun AccountPersonalisationCard(activity: String, dailyGoal: String) {
    SettingsSectionCard {
        Column {
            SettingsSectionHeaderRow(title = stringResource(id = R.string.account_personalisation))
            SettingsSectionBodyRow(type = stringResource(id = R.string.physical_activity), value = activity)
            SettingsSectionBodyRow(type = stringResource(id = R.string.daily_water_goal), value = dailyGoal)
        }
    }
}

@Composable
fun PersonalDataCard(gender: String, birthDate: String, weight: String, height: String) {
    SettingsSectionCard {
        Column {
            SettingsSectionHeaderRow(title = stringResource(id = R.string.registration_personal_data))
            SettingsSectionBodyRow(type = stringResource(id = R.string.gender), value = gender)
            SettingsSectionBodyRow(type = stringResource(id = R.string.birth_date), value = birthDate)
            SettingsSectionBodyRow(type = stringResource(id = R.string.parameters_weight), value = weight)
            SettingsSectionBodyRow(type = stringResource(id = R.string.parameters_height), value = height)
        }
    }
}