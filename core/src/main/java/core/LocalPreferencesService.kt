package core

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import core.model.UserDataModel
import javax.inject.Inject

class LocalPreferencesService @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : LocalPreferencesApi {

    companion object {
        const val CURRENT_USER_KEY = "current_user"
        const val CURRENT_USER_ID_KEY = "current_user_id"
        const val DEFAULT_VALUE = ""
    }

    override fun setCurrentUser(userData: UserDataModel?) {
        Log.d("ANANAS", "setCurrentUser: $userData")
        sharedPreferences.edit()
            .putString(CURRENT_USER_KEY, gson.toJson(userData))
            .apply()
    }


    override fun getCurrentUser(): UserDataModel {
        val json = sharedPreferences.getString(CURRENT_USER_KEY, DEFAULT_VALUE)
        Log.d("ANANAS", "getCurrentUser: $json")
        return gson.fromJson(json, UserDataModel::class.java)
    }

    override fun clearUserData() {
        setCurrentUser(null)
    }

    override fun setCurrentUserId(id: String) {
        sharedPreferences.edit()
            .putString(CURRENT_USER_ID_KEY, id)
            .apply()
    }

    override fun getCurrentUserId(): String {
        return sharedPreferences.getString(CURRENT_USER_ID_KEY, DEFAULT_VALUE) ?: ""
    }

    override fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }
}