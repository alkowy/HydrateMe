package com.azmarzly.home.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R.string
import core.ui.theme.HydrateMeTheme

@Composable
fun MinimalDropdownMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(24.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "")
        }

        androidx.compose.material3.MaterialTheme(
            shapes = androidx.compose.material3.MaterialTheme.shapes.copy(
                extraSmall = RoundedCornerShape(16.dp)
            )
        ) {
            DropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colors.onBackground),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                    text = {
                        Text(
                            text = stringResource(string.edit),
                            color = MaterialTheme.colors.primary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colors.primary,
                        )
                    },
                    onClick = {
                        onEdit()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                    text = {
                        Text(
                            text = stringResource(string.delete),
                            color = MaterialTheme.colors.primary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colors.primary,
                        )
                    },
                    onClick = {
                        onDelete()
                        expanded = false
                    }
                )
            }
        }

    }
}