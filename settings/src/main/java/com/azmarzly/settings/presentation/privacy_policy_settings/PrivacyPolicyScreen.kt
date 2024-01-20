package com.azmarzly.settings.presentation.privacy_policy_settings

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azmarzly.core.R
import com.azmarzly.settings.presentation.SettingsSubScreenHeader

@Composable
fun PrivacyPolicyScreen(
    closeScreen: () -> Unit,
    viewModel: PrivacyPolicyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PrivacyPolicyScreenContent(
        state = state,
        closeScreen = closeScreen
    )
}

@Composable
fun PrivacyPolicyScreenContent(
    state: PrivacyPolicyState,
    closeScreen: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingsSubScreenHeader(
            headerText = stringResource(id = R.string.privacy_policy),
            onCloseScreenClick = closeScreen,
            isEnabled = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = state.privacyPolicyBody,
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.onBackground
            )
        )
    }
}