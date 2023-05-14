package core.util

sealed class Routes(val route: String) {
    object LandingPage : Routes("landing")
    object Home : Routes("home")
    object Registration : Routes("registration")
    enum class RegistrationRoute {
        INITIAL, GENDER, MEASUREMENTS, ACTIVITY;
    }
}