package com.azmarzly.landing_page

sealed interface LandingPageState {
    object LoggedIn : LandingPageState
    object Loading : LandingPageState
    object NotLoggedIn : LandingPageState
}