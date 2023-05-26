package core.util

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder

fun NavController.navigateTo(route: Route, builder: NavOptionsBuilder.() -> Unit) = this.navigate(route.route, builder)
fun NavOptionsBuilder.popUpTo(route: Route, popUpToBuilder: PopUpToBuilder.() -> Unit = {}) = this.popUpTo(route.route, popUpToBuilder)