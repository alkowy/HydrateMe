package core.model

sealed interface UserActivity {
    val name: String
    val description: String

    data class VeryLowActivity(
        override val name: String = "Very low",
        override val description: String = "Siedząca praca, lekka prace domowe, spacer tylko do autobusu",
    ) : UserActivity

    data class LowActivity(
        override val name: String = "Low",
        override val description: String = "Siedząca lub stojąca praca z przemieszczaniem się w ciągu dnia, cięższe prace domowe",
    ) : UserActivity

    data class AverageActivity(
        override val name: String = "Average",
        override val description: String = "Praca fizyczna, duża ilość ruchu w ciagu dnia",
    ) : UserActivity

    data class HighActivity(
        override val name: String = "High",
        override val description: String = "Wielogodzinna ciężka praca fizyczna, bardzo duża ruchu w ciagu dnia",
    ) : UserActivity

    data class Empty(
        override val name: String = "Empty",
        override val description: String = "Empty",
    ) : UserActivity
}

fun UserActivity.toUserActivityEnum(): UserActivityEnum {
    return when (this) {
        is UserActivity.VeryLowActivity -> UserActivityEnum.VERY_LOW
        is UserActivity.LowActivity -> UserActivityEnum.LOW
        is UserActivity.AverageActivity -> UserActivityEnum.AVERAGE
        is UserActivity.HighActivity -> UserActivityEnum.HIGH
        is UserActivity.Empty -> UserActivityEnum.EMPTY
    }
}

fun UserActivityEnum.toUserActivity(): UserActivity {
    return when (this) {
        UserActivityEnum.EMPTY -> UserActivity.Empty()
        UserActivityEnum.VERY_LOW -> UserActivity.VeryLowActivity()
        UserActivityEnum.LOW -> UserActivity.LowActivity()
        UserActivityEnum.AVERAGE -> UserActivity.AverageActivity()
        UserActivityEnum.HIGH -> UserActivity.HighActivity()
    }
}

fun String.toUserActivityEnumFromString(): UserActivityEnum {
    return when (this) {
        UserActivity.VeryLowActivity().name -> UserActivity.VeryLowActivity().toUserActivityEnum()
        UserActivity.LowActivity().name -> UserActivity.LowActivity().toUserActivityEnum()
        UserActivity.AverageActivity().name -> UserActivity.AverageActivity().toUserActivityEnum()
        UserActivity.HighActivity().name -> UserActivity.HighActivity().toUserActivityEnum()
        else -> UserActivityEnum.EMPTY
    }
}