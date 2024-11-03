package com.azmarzly.notifications

interface UserInactivityCheckerApi {
    fun checkUserInactivity(): InactivityType
}