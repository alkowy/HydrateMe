package com.azmarzly.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import core.ui.theme.HydrateMeTheme
import core.ui.theme.settingsLabelTextColor

@Composable
fun SettingsSubScreenCard(label: String, value: String, onClickAction: () -> Unit, trailingItem: @Composable (() -> Unit)? = null, enabled: Boolean = true) {
    SettingsSectionCard(modifier = Modifier.clickable(enabled = enabled) { onClickAction() }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = spacedBy(4.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.settingsLabelTextColor
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onBackground
                    )
                )
            }
            trailingItem?.let { it() }
        }
    }
}

@Preview
@Composable
fun SettingsSubScreenCardPreview() {
    HydrateMeTheme {
        SettingsSubScreenCard("label", "value", {})
    }
}