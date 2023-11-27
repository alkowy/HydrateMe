package com.azmarzly.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.model.ScanData
import core.ui.theme.caption


@Composable
fun UrineDataRow(scanData: ScanData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = scanData.hour,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.caption
        )
        Divider(
            color = MaterialTheme.colors.caption,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
                .width(1.dp)
        )
        Canvas(modifier = Modifier.size(32.dp)) {
            drawCircle(color = scanData.color.toColor())
        }
        Text(
            text = scanData.message,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.caption,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}