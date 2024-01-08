package com.azmarzly.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import core.ui.theme.shadowedTextColor

@Composable
fun SettingsSectionBodyRow(type: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = type,
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.shadowedTextColor
            )
        )

        Text(
            text = value,
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.onBackground
            )
        )
    }
}