package core.util

import androidx.annotation.StringRes
import com.azmarzly.core.R

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

    fun toResourceStringId(): Int =
        when (this) {
            REGISTRATION_ROOT -> -1
            INITIAL -> R.string.registration_personal_data
            PARAMETERS -> R.string.registration_parameters
            GENDER -> R.string.gender
            ACTIVITY -> R.string.physical_activity
            GOAL -> R.string.daily_water_goal
        }

    override val route: String = this.name
}

enum class HomeRoute : Route {
    HOME_ROOT, HOME, CALENDAR, PROFILE, NEWS;

    override val route: String = this.name
}

enum class ProfileRoute : Route {
    PROFILE, SETTINGS;

    override val route: String = this.name
}

enum class SettingsRoute : Route {
    MAIN, PERSONAL_DATA, ACCOUNT_PERSONALISATION, ACCOUNT, PRIVACY_POLICY;

    override val route: String = this.name

}

object LandingPageRoute : Route {
    override val route: String = "landing_page"
}

object SignInRoute : Route {
    override val route: String = "sign_in"
}