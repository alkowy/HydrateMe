package core.util

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigateWithinRegistration(route: String) {
    this.navigate(route) {
        popUpTo(Routes.RegistrationRoute.GENDER.name) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}