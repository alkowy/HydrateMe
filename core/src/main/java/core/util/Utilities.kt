package core.util

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import core.model.Gender
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.transform

fun doNothing() = Unit

fun Number.toPercentageString() = "${this.toInt()}%"

fun String.toGender(): Gender? {
    return when (this.lowercase()) {
        "male" -> Gender.MALE
        "female" -> Gender.FEMALE
        else -> null
    }
}


fun Modifier.clickableOnce(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    clickDebounceWindow: Long = 350L,
    onClick: () -> Unit,
) = composed {
    val debounceClickState = remember {
        MutableSharedFlow<() -> Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }

    var lastEventTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        debounceClickState.transform {
            // Only emit click events if the clickDebounce
            // millis have passed since the last click event
            val now = System.currentTimeMillis()
            if (now - lastEventTime > clickDebounceWindow) {
                emit(it)
                lastEventTime = now
            }
        }.collect { clickEvent ->
            clickEvent.invoke()
        }
    }

    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { debounceClickState.tryEmit(onClick) },
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }
    )
}