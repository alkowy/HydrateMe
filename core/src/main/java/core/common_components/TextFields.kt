package core.common_components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import core.ui.theme.backgroundContainer
import core.ui.theme.textFieldLabel

@Composable
fun ValidatedTextField(
    isSecured: Boolean,
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    imeAction: ImeAction,
    style: TextStyle,
    keyboardType: KeyboardType,
) {
    if (isSecured) {
        SecuredValidatedTextField(
            modifier = modifier,
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            label = label,
            style = style,
            isError = isError,
            errorText = errorText,
            imeAction = imeAction,
            keyboardType = keyboardType,
        )
    } else {
        PlaneValidatedTextField(
            modifier = modifier,
            value = value,
            onValueChange = { onValueChange(it) },
            label = label,
            style = style,
            isError = isError,
            errorText = errorText,
            imeAction = imeAction,
            keyboardType = keyboardType,
        )
    }
}

@Composable
fun PlaneValidatedTextField(
    modifier: Modifier = Modifier,
    value: MutableState<String>,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    imeAction: ImeAction = ImeAction.Next,
    style: TextStyle,
    keyboardType: KeyboardType = KeyboardType.Text,
    suffix: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {

    TextField(
        modifier = modifier,
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChange(it)
        },
        label = {
            Text(
                text = label,
                style = style.copy(color = MaterialTheme.colors.textFieldLabel)
            )
        },
        isError = isError,
        supportingText = if (isError) {
            { Text(text = errorText, style = style) }
        } else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        maxLines = 1,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colors.backgroundContainer,
            focusedContainerColor = MaterialTheme.colors.backgroundContainer,
            errorContainerColor = MaterialTheme.colors.backgroundContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledSuffixColor = MaterialTheme.colors.textFieldLabel,
            unfocusedSuffixColor = MaterialTheme.colors.textFieldLabel,
            focusedSuffixColor = MaterialTheme.colors.textFieldLabel,
            cursorColor = MaterialTheme.colors.primary
        ),
        textStyle = style,
        suffix = suffix,
        trailingIcon = trailingIcon,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecuredValidatedTextField(
    modifier: Modifier = Modifier,
    value: MutableState<String>,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    style: TextStyle,
    keyboardType: KeyboardType = KeyboardType.Password,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var isInputVisible by remember {
        mutableStateOf(false)
    }

    TextField(
        modifier = modifier,
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChange(it)
        },
        label = {
            Text(
                text = label,
                style = style.copy(color = MaterialTheme.colors.textFieldLabel)
            )
        },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = errorText,
                    style = style
                )
            }
        },
        maxLines = 1,
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            IconButton(onClick = {
                isInputVisible = !isInputVisible
            }) {

                val visibleIconAndText = Pair(
                    first = Icons.Outlined.Visibility,
                    second = "Visible"//stringResource(id = R.string.icon_password_visible)
                )

                val hiddenIconAndText = Pair(
                    first = Icons.Outlined.VisibilityOff,
                    second = "Hidden"//stringResource(id = R.string.icon_password_hidden)
                )

                val passwordVisibilityIconAndText =
                    if (isInputVisible) visibleIconAndText else hiddenIconAndText

                Icon(
                    imageVector = passwordVisibilityIconAndText.first,
                    contentDescription = passwordVisibilityIconAndText.second,
                    tint = MaterialTheme.colors.textFieldLabel
                )
            }
        },
        visualTransformation = if (isInputVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),

        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colors.backgroundContainer,
            focusedContainerColor = MaterialTheme.colors.backgroundContainer,
            errorContainerColor = MaterialTheme.colors.backgroundContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledSuffixColor = MaterialTheme.colors.textFieldLabel,
            unfocusedSuffixColor = MaterialTheme.colors.textFieldLabel,
            focusedSuffixColor = MaterialTheme.colors.textFieldLabel,
        ),
        textStyle = style
    )
}