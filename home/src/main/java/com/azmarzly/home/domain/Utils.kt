package com.azmarzly.home.domain

import core.util.HomeRoute

fun String?.toBottomBarItemIndex(): Int {
    return when (this) {
        HomeRoute.HOME.route -> 0
        HomeRoute.CALENDAR.route -> 1
        HomeRoute.PROFILE.route -> 3
        else -> 4
    }
}