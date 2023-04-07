package core.model

sealed interface UserActivity {
    data class VeryLowActivity(
        val name: String = "Very low",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    )

    data class LowActivity(
        val name: String = "Low",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    )

    data class AverageActivity(
        val name: String = "Average",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    )

    data class HighActivity(
        val name: String = "High",
        val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
    )
}