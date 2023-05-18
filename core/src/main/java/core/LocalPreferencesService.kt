package core

import android.content.SharedPreferences
import core.model.UserDataModel
import com.google.gson.Gson
import javax.inject.Inject

class LocalPreferencesService @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : LocalPreferencesApi {

    companion object {
        const val CURRENT_USER_KEY = "current_user"
        const val DEFAULT_VALUE = ""
    }

    override fun setCurrentUser(userData: UserDataModel?) {
        sharedPreferences.edit()
            .putString(CURRENT_USER_KEY, gson.toJson(userData))
            .apply()
    }

    override fun getCurrentUser(): UserDataModel {
        val json = sharedPreferences.getString(CURRENT_USER_KEY, DEFAULT_VALUE)
        return gson.fromJson(json, UserDataModel::class.java)
    }

    override fun clearUserData() {
        setCurrentUser(null)
    }

    override fun getCurrentUserId(): String = getCurrentUser().uid

    override fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }
}