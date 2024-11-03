package com.azmarzly.notifications

import core.LocalPreferencesApi
import core.util.toTimestamp
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class UserInactivityChecker @Inject constructor(
    private val localPreferencesApi: LocalPreferencesApi,
) : UserInactivityCheckerApi {

    override fun checkUserInactivity(): InactivityType {
        val lastLogTime = localPreferencesApi.getLastLogTimestamp()
        val currentTime = LocalDateTime.now().toTimestamp()
        val difference = currentTime.minus(lastLogTime)
        return when {
            lastLogTime == -1L -> InactivityType.NONE
            difference in 4.hours.inWholeMilliseconds..8.hours.inWholeMilliseconds -> InactivityType.REGULAR
            difference >= 8.hours.inWholeMilliseconds -> InactivityType.LONG
            else -> InactivityType.NONE
        }
    }
}

enum class InactivityType {
    NONE, // if active
    REGULAR, // 4 hours
    LONG, // 8 hours or more
}