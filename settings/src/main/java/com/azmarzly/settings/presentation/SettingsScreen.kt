package com.azmarzly.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.azmarzly.settings.presentation.personal_data_settings.PersonalDataModel
import core.util.SettingsRoute
import core.util.clickableOnce


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
            personalDataModel = state.personalData,
            navigateToScreen = { navigateToSettingsSection(SettingsRoute.PERSONAL_DATA) }
        )
        AccountPersonalisationCard(
            accountPersonalisationModel = state.accountPersonalisation,
            navigateToScreen = { navigateToSettingsSection(SettingsRoute.ACCOUNT_PERSONALISATION) }
        )
        AccountCard(
            navigateToScreen = { navigateToSettingsSection(SettingsRoute.ACCOUNT) }
        )
        PrivacyPolicyCard(
            navigateToScreen = { navigateToSettingsSection(SettingsRoute.PRIVACY_POLICY) }
        )
    }
}

@Composable
fun PrivacyPolicyCard(
    navigateToScreen: () -> Unit,
) {
    SettingsSectionCard(
        modifier = Modifier.clickableOnce {
            navigateToScreen()
        }
    ) {
        SettingsSectionHeaderRow(title = stringResource(id = R.string.privacy_policy))
    }
}

@Composable
fun AccountCard(
    navigateToScreen: () -> Unit,
) {
    SettingsSectionCard(
        modifier = Modifier.clickableOnce {
            navigateToScreen()
        }
    ) {
        SettingsSectionHeaderRow(title = stringResource(id = R.string.account))
    }
}

@Composable
fun AccountPersonalisationCard(
    accountPersonalisationModel: AccountPersonalisationModel,
    navigateToScreen: () -> Unit,
) {
    SettingsSectionCard(
        modifier = Modifier.clickableOnce {
            navigateToScreen()
        }
    ) {
        Column {
            SettingsSectionHeaderRow(title = stringResource(id = R.string.account_personalisation))
            SettingsSectionBodyRow(type = stringResource(id = R.string.physical_activity), value = accountPersonalisationModel.activity)
            SettingsSectionBodyRow(type = stringResource(id = R.string.daily_water_goal), value = accountPersonalisationModel.hydrationGoal)
        }
    }
}

@Composable
fun PersonalDataCard(
    personalDataModel: PersonalDataModel,
    navigateToScreen: () -> Unit,
) {
    SettingsSectionCard(modifier = Modifier.clickableOnce {
        navigateToScreen()
    }) {
        Column {
            SettingsSectionHeaderRow(title = stringResource(id = R.string.registration_personal_data))
            SettingsSectionBodyRow(type = stringResource(id = R.string.gender), value = personalDataModel.gender.genderText)
            SettingsSectionBodyRow(type = stringResource(id = R.string.birth_date), value = personalDataModel.birthDate)
            SettingsSectionBodyRow(type = stringResource(id = R.string.parameters_weight), value = personalDataModel.weight)
            SettingsSectionBodyRow(type = stringResource(id = R.string.parameters_height), value = personalDataModel.height)
        }
    }
}