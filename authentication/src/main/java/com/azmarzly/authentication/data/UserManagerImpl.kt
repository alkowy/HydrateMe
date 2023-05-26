package com.azmarzly.authentication.data

import com.azmarzly.authentication.domain.UserManager
import com.google.firebase.auth.FirebaseAuth
import core.LocalPreferencesApi
import core.model.UserDataModel
import javax.inject.Inject

class UserManagerImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val localPreferencesApi: LocalPreferencesApi,
) : UserManager {

    override fun getLoggedInUserFromLocalPreferences(): UserDataModel = localPreferencesApi.getCurrentUser()

    override fun saveLoggedInUserToLocalPreferences(user: UserDataModel?) = localPreferencesApi.setCurrentUser(user)

    override fun clearUserDataInLocalPreferences() = localPreferencesApi.clearUserData()

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    override fun saveUserId(id: String) = localPreferencesApi.setCurrentUserId(id)
}