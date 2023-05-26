package com.azmarzly.authentication.domain

import core.model.UserDataModel

interface UserManager {

    fun getLoggedInUserFromLocalPreferences(): UserDataModel
    fun saveLoggedInUserToLocalPreferences(user: UserDataModel?)
    fun isUserLoggedIn(): Boolean
    fun clearUserDataInLocalPreferences()
    fun getCurrentUserId(): String?
    fun saveUserId(id: String)
}