package com.azmarzly.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import core.model.DrinkType
import core.ui.theme.backgroundContainer
import core.ui.theme.bodySmall

@Composable
fun RowScope.AddWaterItem(
    drinkType: DrinkType,
    onSelected: (DrinkType) -> Unit,
    isSelected: Boolean = false,
) {
    Box(
        modifier = Modifier
            .height(125.dp)
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.backgroundContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onSelected(drinkType) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = if (isSelected) drinkType.resIdSelected else drinkType.resIdUnselected),
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.onBackground
            )
            Text(
                text = drinkType.amountOfWater.toString() + " ml",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.onBackground
                )
            )

        }
    }
}