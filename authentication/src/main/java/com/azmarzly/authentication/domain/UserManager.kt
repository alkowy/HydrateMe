package com.azmarzly.authentication.domain

import core.model.UserDataModel

interface UserManager {

    fun getLoggedInUser(): UserDataModel
    fun saveLoggedInUserToLocalPreferences(user: UserDataModel?)
    fun isUserLoggedIn(): Boolean
    fun clearUserData()
}