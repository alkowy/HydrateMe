package core.util

interface Route {
    val route: String

    fun nextRegistrationStep(): Route {
        return when (this) {
            RegistrationRoute.REGISTRATION_ROOT -> RegistrationRoute.INITIAL
            RegistrationRoute.INITIAL -> RegistrationRoute.GENDER
            RegistrationRoute.GENDER -> RegistrationRoute.AGE
            RegistrationRoute.AGE -> RegistrationRoute.MEASUREMENTS_HEIGHT
            RegistrationRoute.MEASUREMENTS_HEIGHT -> RegistrationRoute.MEASUREMENTS_WEIGHT
            RegistrationRoute.MEASUREMENTS_WEIGHT -> RegistrationRoute.ACTIVITY
            RegistrationRoute.ACTIVITY -> HomeRoute.HOME_ROOT
            else -> RegistrationRoute.INITIAL
        }
    }

    fun previousRegistrationStep(): RegistrationRoute {
        return when (this) {
            RegistrationRoute.AGE -> RegistrationRoute.GENDER
            RegistrationRoute.MEASUREMENTS_HEIGHT -> RegistrationRoute.AGE
            RegistrationRoute.MEASUREMENTS_WEIGHT -> RegistrationRoute.MEASUREMENTS_HEIGHT
            RegistrationRoute.ACTIVITY -> RegistrationRoute.MEASUREMENTS_WEIGHT
            else -> RegistrationRoute.INITIAL
        }
    }
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