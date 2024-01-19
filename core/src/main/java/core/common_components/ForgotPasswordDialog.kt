package core.common_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.azmarzly.core.R
import core.input_validators.ValidationState
import core.ui.theme.bodySmall


@Composable
fun ForgotPasswordDialog(
    isError: Boolean,
    errorMessage: String,
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
                        errorText = errorMessage,
                        isError = isError,
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
                        },
                        enabled = resetEmailValidationState == ValidationState.Valid
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