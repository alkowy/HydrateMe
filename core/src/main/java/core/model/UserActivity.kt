package core.model

sealed interface UserActivity {
    data class VeryLowActivity(
        val name: String = "Very low",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    ) : UserActivity

    data class LowActivity(
        val name: String = "Low",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    ) : UserActivity

    data class AverageActivity(
        val name: String = "Average",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    ) : UserActivity

    data class HighActivity(
        val name: String = "High",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    ) : UserActivity

    object Empty : UserActivity

    fun toUserActivityEnum(): UserActivityEnum {
        return when (this) {
            is VeryLowActivity -> UserActivityEnum.VERY_LOW
            is LowActivity -> UserActivityEnum.LOW
            is AverageActivity -> UserActivityEnum.AVERAGE
            is HighActivity -> UserActivityEnum.HIGH
            is Empty -> UserActivityEnum.EMPTY
        }
    }
}