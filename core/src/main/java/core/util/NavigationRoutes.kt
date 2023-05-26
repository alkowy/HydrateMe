package core.util

interface Route {
    val route: String
}

enum class RegistrationRoute : Route {
    REGISTRATION_ROOT, INITIAL, GENDER, AGE, MEASUREMENTS_HEIGHT, MEASUREMENTS_WEIGHT, ACTIVITY;

    override val route: String = this.name
}

enum class HomeRoute : Route {
    HOME_ROOT, HOME, CALENDAR, PROFILE, NEWS;

    override val route: String = this.name
}

object LandingPageRoute : Route {
    override val route: String = "landing_page"
}

object SignInRoute : Route {
    override val route: String = "sign_in"
}

object ScannerRoute : Route {
    override val route: String = "scanner"
}