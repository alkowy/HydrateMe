package com.azmarzly.settings.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.azmarzly.core.R.drawable
import core.util.clickableOnce

@Composable
fun ClickableText(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Text(
        modifier = modifier.clickableOnce { onClick() },
        text = text,
        style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
    )
}


@Composable
fun SettingsSubScreenHeader(
    headerText: String,
    onCloseScreenClick: () -> Unit,
    isEnabled: Boolean,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onCloseScreenClick,
            enabled = isEnabled,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painterResource(id = drawable.ic_arrow_left), contentDescription = "back",
                tint = MaterialTheme.colors.onSurface
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = headerText,
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.onBackground
            )
        )
    }
}