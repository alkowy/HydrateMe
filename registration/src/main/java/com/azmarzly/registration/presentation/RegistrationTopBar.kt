package com.azmarzly.registration.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azmarzly.registration.R
import core.ui.theme.HydrateMeTheme
import core.ui.theme.registrationStepDot
import core.ui.theme.registrationTextColor
import core.util.RegistrationRoute

@Composable
fun RegistrationTopBar(
    currentStep: RegistrationRoute,
    showBackButton: Boolean,
    onBackButtonClick: () -> Unit,
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 16.dp, bottom = 16.dp)
                .sizeIn(minHeight = 46.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = onBackButtonClick
                ) {
                    Icon(painterResource(id = R.drawable.ic_back_arrow), contentDescription = "back")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement
                    .spacedBy(
                        space = 14.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RegistrationRoute.values().filter { it != RegistrationRoute.REGISTRATION_ROOT }.forEach {
                    RegistrationStepDot(isPreviousOrCurrent = it.ordinal <= currentStep.ordinal, isCurrent = it.ordinal == currentStep.ordinal)
                }
            }
        }

        Text(
            modifier = Modifier.align(CenterHorizontally),
            style = MaterialTheme.typography.h3,
            color = colors.registrationTextColor,
            text = currentStep.toName()
        )
    }
}

@Composable
fun RegistrationStepDot(
    isPreviousOrCurrent: Boolean,
    isCurrent: Boolean,
) {
    val circleColor = if (isPreviousOrCurrent) colors.primary else colors.registrationStepDot
    val size = if (isCurrent) 10.dp else 6.dp
    Canvas(
        modifier = Modifier
            .size(size)
            .animateContentSize()
    ) {
        drawCircle(color = circleColor)
    }

}

@Preview
@Composable
fun RegistrationTopBarPreview() {
    HydrateMeTheme {
        RegistrationTopBar(
            currentStep = RegistrationRoute.GENDER,
            showBackButton = true,
            onBackButtonClick = {},
        )
    }
}

@Preview
@Composable
fun RegistrationTopBarPreviewWithoutBackButton() {
    HydrateMeTheme {
        RegistrationTopBar(
            currentStep = RegistrationRoute.GENDER,
            showBackButton = false,
            onBackButtonClick = {},
        )
    }
}