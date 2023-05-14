package core.model

data class ScanData(
    val color: UrineColor,
    val message: String,
)
enum class UrineColor{
    BAD, OK, GOOD, SOMETHING; //need some scale to use
}