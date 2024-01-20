package com.azmarzly.settings.presentation.account_settings

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azmarzly.core.R
import com.azmarzly.settings.presentation.ClickableText
import com.azmarzly.settings.presentation.ParametersPickDialog
import com.azmarzly.settings.presentation.SettingsSubScreenCard
import com.azmarzly.settings.presentation.SettingsSubScreenHeader
import com.azmarzly.settings.presentation.account_settings.AccountSettingsState.ToastState.Error
import com.azmarzly.settings.presentation.account_settings.AccountSettingsState.ToastState.Success
import core.common_components.ResetPasswordDialog
import core.common_components.RoundedButtonWithContent
import core.util.doNothing

@Composable
fun AccountSettingsScreen(
    viewModel: AccountSettingsViewModel = hiltViewModel(),
    closeScreen: () -> Unit,
    navigateToSignIn: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AccountSettingsScreenContent(
        state = state,
        closeScreen = closeScreen,
        saveChanges = viewModel::saveChanges,
        updateUserName = viewModel::updateUserName,
        onResetPassword = viewModel::resetPassword,
        validateName = viewModel::validateName,
        toggleResetPasswordDialogVisibility = viewModel::toggleResetPasswordDialogVisibility,
        onSignOut = {
            viewModel.signOut()
            navigateToSignIn()
        }
    )
}

@Composable
fun AccountSettingsScreenContent(
    state: AccountSettingsState,
    closeScreen: () -> Unit,
    saveChanges: () -> Unit,
    updateUserName: (String) -> Unit,
    validateName: (String) -> Unit,
    onResetPassword: () -> Unit,
    toggleResetPasswordDialogVisibility: (Boolean) -> Unit,
    onSignOut: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var showUserNamePickDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(state.toastState) {
        when (state.toastState) {
            is Success -> {
                Toast.makeText(context, state.emailSentMessage, Toast.LENGTH_SHORT).show()
            }

            is Error -> {
                Toast.makeText(context, state.emailSentErrorMessage, Toast.LENGTH_SHORT).show()
            }

            else -> {
                doNothing()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        if (showUserNamePickDialog) {
            ParametersPickDialog(
                parameterTitle = stringResource(id = R.string.name),
                parameterSubtitle = null,
                label = stringResource(id = R.string.name),
                unit = null,
                onDismiss = {
                    showUserNamePickDialog = false
                },
                validateInput = { validateName(it) },
                isInputValid = state.isNewNameValid,
                confirm = { name ->
                    updateUserName(name)
                    showUserNamePickDialog = false
                },
                keyboardType = KeyboardType.Text,
                )
        }

        if (state.shouldShowResetPasswordDialog) {
            ResetPasswordDialog(
                onDismiss = { toggleResetPasswordDialogVisibility(false) },
                email = state.email,
                onConfirm = onResetPassword
            )
        }

        SettingsSubScreenHeader(
            headerText = stringResource(id = R.string.account),
            isEnabled = true,
            onCloseScreenClick = {
                closeScreen()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSubScreenCard(
            label = stringResource(id = R.string.name),
            value = state.name,
            onClickAction = { showUserNamePickDialog = true },
            trailingItem = {
                ClickableText(
                    text = stringResource(id = R.string.edit),
                    onClick = { showUserNamePickDialog = true }
                )
            }
        )

        SettingsSubScreenCard(
            label = stringResource(id = R.string.email_label),
            value = state.email,
            onClickAction = { },
            enabled = false,
        )

        Spacer(modifier = Modifier.height(4.dp))
        ClickableText(
            text = stringResource(id = R.string.reset_password),
            onClick = { toggleResetPasswordDialogVisibility(true) }
        )
        ClickableText(
            text = stringResource(id = R.string.sign_out),
            onClick = onSignOut
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