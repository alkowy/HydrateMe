package com.azmarzly.registration.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationStepWithBottomBar(
    bottomBarState: RegistrationBottomBarState,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            RegistrationStepBottomBar(
                state = bottomBarState,
                onSkip = onSkip,
                onNext = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp)
            )
        }
    ) { padding ->
        content(padding)
    }
}