package core

import core.model.UserDataModel

interface LocalPreferencesApi {

    fun setCurrentUser(userData: UserDataModel?)
    fun getCurrentUser(): UserDataModel
    fun clearUserData()
    fun getCurrentUserId(): String
    fun isUserLoggedIn(): Boolean
    fun setCurrentUserId(id: String)
}