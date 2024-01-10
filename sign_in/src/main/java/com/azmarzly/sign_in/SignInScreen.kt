package com.azmarzly.sign_in

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.azmarzly.core.R
import core.common_components.PlaneValidatedTextField
import core.common_components.RoundedButtonWithContent
import core.common_components.ValidatedTextField
import core.input_validators.ValidationState
import core.input_validators.ValidationState.Valid
import core.ui.theme.HydrateMeTheme
import core.ui.theme.bodySmall
import core.ui.theme.buttonLabelLinkTextStyle
import core.util.HomeRoute
import core.util.RegistrationRoute
import core.util.navigateTo

@Composable
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = hiltViewModel()
    val state by viewModel.authState.collectAsStateWithLifecycle()
    val emailValidationState by viewModel.emailValidationState.collectAsStateWithLifecycle()
    val passwordValidationState by viewModel.passwordValidationState.collectAsStateWithLifecycle()
    val resetEmailValidationState by viewModel.resetPasswordEmailValidationState.collectAsStateWithLifecycle()
    var forgotPasswordDialogVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    SignInScreenContent(
        state = state,
        emailValidationState = emailValidationState,
        passwordValidationState = passwordValidationState,
        onNavigateToRegistration = {
            navController.navigateTo(RegistrationRoute.REGISTRATION_ROOT) {
                launchSingleTop = true
            }
        },
        onLogin = viewModel::loginWithEmailAndPassword,
        onForgotPassword = { forgotPasswordDialogVisible = true },
        validateEmail = viewModel::validateEmail,
        validatePassword = viewModel::validatePassword,
    )

    ForgotPasswordDialog(
        isDialogVisible = forgotPasswordDialogVisible,
        onDialogDismiss = { forgotPasswordDialogVisible = false },
        onSendResetPassword = viewModel::requestPasswordReset,
        validateResetPasswordEmail = viewModel::validateResetPasswordEmail,
        resetEmailValidationState = resetEmailValidationState,
    )

    LaunchedEffect(Unit) {
        if (viewModel.isLoggedIn()) {
            navController.popBackStack()
            navController.navigateTo(HomeRoute.HOME_ROOT) {}
        }
    }
    LaunchedEffect(state) {
        if (state is SignInState.Success) {
            navController.popBackStack()
            navController.navigateTo(HomeRoute.HOME_ROOT) {}
        }
        if (state is SignInState.Error) {
            val signInError = context.getString(R.string.sign_in_error)
            Toast.makeText(context, signInError, Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(
    isDialogVisible: Boolean,
    onDialogDismiss: () -> Unit,
    onSendResetPassword: (String) -> Unit,
    validateResetPasswordEmail: (String) -> Unit,
    resetEmailValidationState: ValidationState,
) {
    val email = rememberSaveable { mutableStateOf("") }
    val initialFocus = remember { FocusRequester() }

    AnimatedVisibility(
        visible = isDialogVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            onDismissRequest = { onDialogDismiss() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .imePadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colors.background,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PlaneValidatedTextField(
                        value = email,
                        onValueChange = { validateResetPasswordEmail(it) },
                        label = stringResource(id = R.string.email_label),
                        style = MaterialTheme.typography.h3,
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester = initialFocus)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(id = R.string.reset_password_body),
                        style = MaterialTheme.typography.bodySmall.copy(
                            MaterialTheme.colors.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RoundedButtonWithContent(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSendResetPassword(email.value)
                            onDialogDismiss()
                        },
                        enabled = resetEmailValidationState == Valid
                    ) {
                        Text(text = stringResource(id = R.string.send))
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            initialFocus.requestFocus()
        }
    }
}

@Composable
fun SignInScreenContent(
    state: SignInState,
    emailValidationState: ValidationState,
    passwordValidationState: ValidationState,
    onNavigateToRegistration: () -> Unit,
    onLogin: (String, String) -> Unit,
    onForgotPassword: () -> Unit,
    validateEmail: (String) -> Unit,
    validatePassword: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.splash_bg),
            contentDescription = "signIn_bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
//                .offset(y = (-300).dp),
        )
        Image(
            painter = painterResource(R.drawable.hydrateme_logo),
            contentDescription = "logo",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = 132.dp,
                    start = 82.dp, end = 82.dp
                )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
            backgroundColor = MaterialTheme.colors.background
        ) {
            val email = rememberSaveable { mutableStateOf("") }
            val password = rememberSaveable { mutableStateOf("") }
            var enabledSignInButton by remember { mutableStateOf(true) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 64.dp)
            ) {
                Text(
                    text = stringResource(R.string.signin),
                    style = MaterialTheme.typography.h3
                )

                Spacer(modifier = Modifier.height(20.dp))

                ValidatedTextField(
                    isSecured = false,
                    value = email,
                    onValueChange = {
                        validateEmail(it)
                    },
                    label = "E-mail",
                    style = MaterialTheme.typography.caption,
                    isError = emailValidationState == ValidationState.Invalid,
                    errorText = stringResource(R.string.incorrect_email),
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ValidatedTextField(
                    isSecured = true,
                    value = password,
                    onValueChange = {
                        validatePassword(it)
                    },
                    label = "Password",
                    style = MaterialTheme.typography.caption.copy(),
                    isError = passwordValidationState == ValidationState.Invalid,
                    errorText = stringResource(R.string.incorrect_password),
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            onForgotPassword()
                        },
                    text = stringResource(R.string.forgot_password),
                    style = buttonLabelLinkTextStyle.copy(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))

                RoundedButtonWithContent(
                    onClick = { onLogin(email.value, password.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = enabledSignInButton,
                ) {
                    if (state is SignInState.Loading) {
                        LinearProgressIndicator(
                            color = MaterialTheme.colors.onPrimary,
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.signin),
                            style = MaterialTheme.typography.button,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = MaterialTheme.colors.onPrimary
                        )

                    }

                }

                Spacer(modifier = Modifier.height(110.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.dont_have_account),
                        style = MaterialTheme.typography.caption
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        modifier = Modifier
                            .clickable {
                                onNavigateToRegistration()
                            },
                        text = stringResource(R.string.sign_up),
                        style = buttonLabelLinkTextStyle.copy(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }

                LaunchedEffect(emailValidationState, passwordValidationState) {
                    enabledSignInButton = (emailValidationState is Valid && passwordValidationState is Valid)
                }
            }
        }
    }
}

@Preview
@Composable
fun SignInScreencontentPrev() {
    HydrateMeTheme() {
        SignInScreenContent(
            state = SignInState.Loading,
            onNavigateToRegistration = { /*TODO*/ },
            onLogin = { _, _ -> Unit },
            onForgotPassword = { Unit },
            validatePassword = { _ -> Unit },
            validateEmail = { _ -> Unit },
            emailValidationState = ValidationState.Empty,
            passwordValidationState = ValidationState.Empty,
        )
    }
}