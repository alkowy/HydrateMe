package core

import android.content.SharedPreferences
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
        const val LAST_LOG_TIMESTAMP = "last_log_timestamp"
        const val LAST_NOTIFICATION_TIMESTAMP = "last_notification_timestamp"
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

    override fun setCurrentUserId(id: String) {
        sharedPreferences.edit()
            .putString(CURRENT_USER_ID_KEY, id)
            .apply()
    }

    override fun getCurrentUserId(): String {
        return sharedPreferences.getString(CURRENT_USER_ID_KEY, DEFAULT_VALUE) ?: ""
    }

    override fun setLastLogTimestamp(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(LAST_LOG_TIMESTAMP, timestamp)
            .apply()
    }

    override fun getLastLogTimestamp(): Long {
        return try {
            sharedPreferences.getLong(LAST_LOG_TIMESTAMP, -1)
        } catch (e: Exception) {
            -1
        }
    }

    override fun setLastNotificationTimestamp(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(LAST_NOTIFICATION_TIMESTAMP, timestamp)
            .apply()
    }

    override fun getLastNotificationTimestamp(): Long {
        return try {
            sharedPreferences.getLong(LAST_NOTIFICATION_TIMESTAMP, -1)
        } catch (e: Exception) {
            -1
        }
    }
}