package com.azmarzly.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun SettingsPickerDialogWithContent(
    headerTitle: String,
    headerSubTitle: String?,
    onDismiss: () -> Unit,
    content: @Composable() (ColumnScope.() -> Unit),
) {
    Dialog(
        onDismissRequest = onDismiss,
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
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingsDialogHeader(
                    title = headerTitle,
                    subTitle = headerSubTitle,
                    onDismiss = onDismiss,
                )
                content()
            }
        }
    }
}

@Composable
private fun SettingsDialogHeader(modifier: Modifier = Modifier, title: String, subTitle: String?, onDismiss: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.h4.copy(
                    color = MaterialTheme.colors.onBackground
                )
            )
            subTitle?.let { subTitle ->
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.body2.copy(
                        color = MaterialTheme.colors.onBackground
                    )
                )
            }
        }

        IconButton(
            onClick = onDismiss,
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "close settings"
            )
        }
    }
}