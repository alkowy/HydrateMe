package com.azmarzly.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import core.common_components.PlaneValidatedTextField
import core.common_components.RoundedButtonWithContent

@Composable
fun ParametersPickDialog(
    parameterTitle: String,
    parameterSubtitle: String?,
    label: String,
    unit: String?,
    onDismiss: () -> Unit,
    confirm: (String) -> Unit,
    isInputValid: Boolean = false,
    validateInput: (String) -> Unit,
    maxCharacters: Int? = null,
    keyboardType: KeyboardType,
) {
    SettingsPickerDialogWithContent(
        headerTitle = parameterTitle,
        headerSubTitle = parameterSubtitle,
        onDismiss = onDismiss,
        content = {
            val value = rememberSaveable { mutableStateOf("") }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlaneValidatedTextField(
                    value = value,
                    onValueChange = validateInput,
                    label = label,
                    style = MaterialTheme.typography.body1,
                    keyboardType = keyboardType,
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        unit?.let {
                            Text(
                                text = unit,
                                style = MaterialTheme.typography.body1.copy(
                                    color = MaterialTheme.colors.onBackground
                                ),
                                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                            )
                        }
                    },
                    maxCharacters = maxCharacters,
                )

            }

            RoundedButtonWithContent(
                modifier = Modifier.fillMaxWidth(),
                enabled = isInputValid && value.value.isNotEmpty(),
                onClick = { confirm(value.value) }) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    )
}