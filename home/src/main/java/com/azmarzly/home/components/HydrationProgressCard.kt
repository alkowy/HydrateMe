package com.azmarzly.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import com.azmarzly.home.presentation.HomeState
import core.ui.theme.HydrateMeTheme
import core.ui.theme.VeryDarkBlue
import core.ui.theme.displayTextStyle
import core.ui.theme.emptyProgressColor
import core.ui.theme.shadowedTextColor
import core.util.doNothing
import core.util.toPercentageString


@Composable
fun HydrationProgress(goalProgress: Int) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedGoalProgress(
            modifier = Modifier.padding(horizontal = 105.dp, vertical = 40.dp),
            progress = goalProgress,
            strokeWidth = 64f,
        )
    }
}

@Composable
fun AnimatedGoalProgress(
    modifier: Modifier = Modifier,
    progress: Int,
    durationMillis: Int = 1500,
    delayMillis: Int = 250,
    strokeWidth: Float,
    startColor: Color = MaterialTheme.colors.primary,
    endColor: Color = MaterialTheme.colors.secondary,
    emptyColor: Color = MaterialTheme.colors.emptyProgressColor,
    size: Dp = 140.dp,
) {

    var isGoalReached by remember { mutableStateOf(false) }

    val sweepAngleAnimateNumber = animateFloatAsState(
        targetValue = progress.toFloat(),
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis
        ), label = ""
    )

    val animateColor = animateColorAsState(
        targetValue = if (isGoalReached) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
        animationSpec = tween(
            durationMillis,
            delayMillis
        )
    )

    LaunchedEffect(Unit, progress) {
        isGoalReached = progress >= 100
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = sweepAngleAnimateNumber.value.toPercentageString(),
            style = displayTextStyle.copy(
                color = MaterialTheme.colors.primary
            )
        )
        Canvas(
            modifier = Modifier
                .size(size)
                .rotate(270f),
        ) {
            val sweepAngle = sweepAngleAnimateNumber.value / 100 * 360

            drawArc(
                color = if (isGoalReached) animateColor.value else emptyColor,
                startAngle = 8.5f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                ),
            )

            val brush = Brush.sweepGradient(
                0f to startColor,
                sweepAngleAnimateNumber.value.div(100) to if (isGoalReached) startColor else endColor,
            )
            drawArc(
                brush = brush,
                startAngle = 8.5f,
                sweepAngle = sweepAngle.coerceAtMost(346f),
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                ),
            )


        }
    }
}

@Composable
fun HomeHydrationProgressCard(
    modifier: Modifier = Modifier,
    homeState: HomeState,
    onAddHydrationAction: (Int) -> Unit,
) {
    Column() {
        Text(
            text = "Dzisiejsze nawodnienie", // todo extract string resource
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = modifier
                    .padding(vertical = 40.dp)
                    .fillMaxWidth(),
            ) {
                HydrationProgress(goalProgress = homeState.hydrationProgressPercentage)

                Spacer(modifier = Modifier.height(40.dp))
                val remaining by animateIntAsState(
                    targetValue = homeState.remainingHydrationMillis, label = "remaining",
                    animationSpec = tween(1500)
                )
                val remainingText = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.body1.toSpanStyle()
                    ) {
                        append("Pozostało $remaining ml")
                    }
                    withStyle(
                        style = MaterialTheme.typography.body1.toSpanStyle().copy(
                            color = MaterialTheme.colors.shadowedTextColor
                        )

                    ) {
                        append(" / ${homeState.hydrationGoal} ml")
                    }
                }
                Text(text = remainingText,)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onAddHydrationAction(250) },
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_filled),
                        contentDescription = "add",
                        modifier = Modifier
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "Dodaj szklankę",
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}


@Preview()
@Composable
fun HomeHydrationProgressCardPreview() {
    HydrateMeTheme() {
        HomeHydrationProgressCard(
            homeState = HomeState(),
            onAddHydrationAction = { doNothing() },
        )
    }
}
