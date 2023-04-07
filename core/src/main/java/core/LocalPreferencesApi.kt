package core

import core.model.UserDataModel


interface LocalPreferencesApi {

    fun setCurrentUser(userData: UserDataModel)
    fun getCurrentUser(): UserDataModel
}