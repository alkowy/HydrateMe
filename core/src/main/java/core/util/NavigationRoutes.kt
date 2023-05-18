package core.util

sealed class Routes(val route: String) {
    object LandingPage : Routes("landing")
    object Home : Routes("home")
    object Registration : Routes("registration")
    object Scanner : Routes("scanner")
    object SignIn : Routes("sign_in")
    enum class RegistrationRoute {
        INITIAL, GENDER, AGE, MEASUREMENTS_HEIGHT, MEASUREMENTS_WEIGHT, ACTIVITY;
    }
}