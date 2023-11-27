package com.azmarzly.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import core.model.ScanData
import core.model.UrineColor
import core.model.UrineScanData
import core.ui.theme.HydrateMeTheme
import core.ui.theme.VeryDarkBlue
import core.util.doNothing
import java.time.LocalDateTime


@Composable
fun UrineScanCard(urineScanData: UrineScanData?) {
    Column() {
        Text(
            text = "Skan moczu", // todo extract string resource
            style = MaterialTheme.typography.h3.copy(color = VeryDarkBlue),
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colors.onSurface
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                IconButton(
                    onClick = { doNothing() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_add_circle), contentDescription = "add")
                }
                urineScanData?.let {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        urineScanData.scanInfo.forEach { item ->
                            UrineDataRow(scanData = item)
                        }
                    }
                }

            }

        }
    }
}


@Preview
@Composable
fun UrineScanPreview() {
    HydrateMeTheme {
        UrineScanCard(
            UrineScanData(
                LocalDateTime.now(),
                listOf(
                    ScanData(color = UrineColor.OK, message = "Nawodnienie dobre", hour = "12:23"),
                    ScanData(
                        color = UrineColor.BAD,
                        message = "Nawodnienie niewystarczającesdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsdsd",
                        hour = "15:23"
                    )
                )
            )
        )
    }
}