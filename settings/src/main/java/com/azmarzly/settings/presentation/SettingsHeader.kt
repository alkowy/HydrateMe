package com.azmarzly.settings.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.azmarzly.core.R

@Composable
fun SettingsHeader(
    closeSettings: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.settings_header),
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.onBackground
            )
        )
        IconButton(
            onClick = closeSettings,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "close settings"
            )
        }
    }
}