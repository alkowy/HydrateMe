package core.util

import core.model.Gender

fun doNothing() = Unit

fun Number.toPercentageString() = "${this.toInt()}%"

fun String.toGender(): Gender? {
    return when (this.lowercase()) {
        "male" -> Gender.MALE
        "female" -> Gender.FEMALE
        else -> null
    }
}