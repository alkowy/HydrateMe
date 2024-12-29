package core

import core.model.UserDataModel

interface LocalPreferencesApi {

    fun setCurrentUser(userData: UserDataModel?)
    fun getCurrentUser(): UserDataModel
    fun clearUserData()
    fun getCurrentUserId(): String
    fun setCurrentUserId(id: String)
    fun setLastLogTimestamp(timestamp: Long)
    fun getLastLogTimestamp(): Long
    fun setLastNotificationTimestamp(timestamp: Long)
    fun getLastNotificationTimestamp(): Long
}