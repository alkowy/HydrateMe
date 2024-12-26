package com.azmarzly.registration.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azmarzly.core.R
import core.common_components.RoundedButtonWithContent
import core.ui.theme.HydrateMeTheme
import core.util.RegistrationRoute
import core.util.RegistrationRoute.GOAL

@OptIn(ExperimentalTextApi::class)
@Composable
fun RegistrationStepBottomBar(
    modifier: Modifier = Modifier,
    state: RegistrationBottomBarState,
    onSkip: () -> Unit,
    onNext: () -> Unit,
) {

    val primaryColor = MaterialTheme.colors.primary
    val onBackgroundColor = MaterialTheme.colors.onBackground

    Column(
        modifier = modifier
    ) {
        RoundedButtonWithContent(
            onClick = onNext,
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth(fraction = if (state.currentStep == GOAL) 1f else 0.5f)
                .height(48.dp),
            enabled = state.isNextButtonEnabled,
        ) {
            Text(if (state.currentStep == GOAL) stringResource(id = R.string.create_account) else stringResource(id = R.string.next_btn))
        }

        Spacer(modifier = Modifier.height(32.dp))

        val haveAccountText = stringResource(id = R.string.have_account)
        val signInText = stringResource(id = R.string.signin).lowercase()
        val fillLaterText = stringResource(id = R.string.registration_fill_later)
        val annotatedString1 = if (state.currentStep == RegistrationRoute.INITIAL) {
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = onBackgroundColor
                    )
                ) {
                    append("$haveAccountText ")
                }
                withAnnotation("tag", "annotation") {
                    withStyle(
                        style = SpanStyle(
                            color = primaryColor,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(signInText)
                    }
                }
            }
        } else {
            buildAnnotatedString {
                withAnnotation("tag", "annotation") {
                    withStyle(
                        style = SpanStyle(
                            color = primaryColor,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(fillLaterText)
                    }
                }
            }
        }
        ClickableText(
            text = annotatedString1,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = 16.dp)

        ) {
            annotatedString1.getStringAnnotations(it, it).firstOrNull()?.tag?.let { tag ->
                if (tag == "tag") {
                    onSkip()
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationBottomBarPreview() {
    HydrateMeTheme {
        RegistrationStepBottomBar(state = RegistrationBottomBarState(), onSkip = { }, onNext = {})
    }
}
