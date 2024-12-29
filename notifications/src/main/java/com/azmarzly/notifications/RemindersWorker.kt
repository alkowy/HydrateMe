package com.azmarzly.notifications

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import core.LocalPreferencesApi
import core.domain.ResourceProvider
import core.util.doNothing
import core.util.toTimestamp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class RemindersWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val reminderNotificationApi: ReminderNotificationApi,
    private val userInactivityCheckerApi: UserInactivityCheckerApi,
    private val resourceProvider: ResourceProvider,
    private val localPreferencesApi: LocalPreferencesApi,
) : Worker(context, workerParams) {

    companion object {
        fun startRemindersWork(context: Context) {
            val periodicWorkRequest: PeriodicWorkRequest =
                PeriodicWorkRequest.Builder(RemindersWorker::class.java, 30, TimeUnit.MINUTES)
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "RemindersWork",
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest
            )
        }

    }

    override fun doWork(): Result {
        if (isWithinDayHours().not() || wasNotificationShownRecently()) return Result.success()
        when (userInactivityCheckerApi.checkUserInactivity()) {
            InactivityType.NONE -> doNothing()

            InactivityType.REGULAR -> reminderNotificationApi.sendNotification(
                message = resourceProvider.getString(com.azmarzly.core.R.string.notification_message_regular)
            )

            InactivityType.LONG -> reminderNotificationApi.sendNotification(
                message = resourceProvider.getString(com.azmarzly.core.R.string.notification_message_long)
            )
        }

        return Result.success()
    }

    private fun wasNotificationShownRecently(): Boolean {
        val lastNotificationTimestamp = localPreferencesApi.getLastNotificationTimestamp()
        if (lastNotificationTimestamp < 0) {
            return false
        }

        val currentTime = LocalDateTime.now().toTimestamp()
        val difference = currentTime.minus(lastNotificationTimestamp)
        val notificationInterval = TimeUnit.HOURS.toSeconds(3)

        return difference < notificationInterval
    }

    private fun isWithinDayHours(): Boolean {
        val startHour = 8  // 8 AM
        val endHour = 21   // 9 PM
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return currentHour in startHour until endHour
    }
}