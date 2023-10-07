package core.util

interface Route {
    val route: String

    fun nextRegistrationStep(): Route {
        return when (this) {
            RegistrationRoute.REGISTRATION_ROOT -> RegistrationRoute.INITIAL
            RegistrationRoute.INITIAL -> RegistrationRoute.PARAMETERS
            RegistrationRoute.PARAMETERS -> RegistrationRoute.GENDER
            RegistrationRoute.GENDER -> RegistrationRoute.ACTIVITY
            RegistrationRoute.ACTIVITY -> RegistrationRoute.GOAL
            RegistrationRoute.GOAL -> HomeRoute.HOME_ROOT
            else -> RegistrationRoute.INITIAL
        }
    }

    fun previousRegistrationStep(): RegistrationRoute {
        return when (this) {
            RegistrationRoute.ACTIVITY -> RegistrationRoute.GENDER
            RegistrationRoute.GENDER -> RegistrationRoute.PARAMETERS
            RegistrationRoute.GOAL -> RegistrationRoute.ACTIVITY
            else -> RegistrationRoute.INITIAL
        }
    }
}

enum class RegistrationRoute : Route {
    REGISTRATION_ROOT, INITIAL, PARAMETERS, GENDER, ACTIVITY, GOAL;

    fun toName() : String =
        when(this){
            REGISTRATION_ROOT -> ""
            INITIAL -> "Podstawowe dane"
            PARAMETERS -> "Twoje parametry"
            GENDER -> "Płeć"
            ACTIVITY -> "Aktywność"
            GOAL -> "Dzienny cel spożycia wody"
        }

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