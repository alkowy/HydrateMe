package com.azmarzly.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import core.LocalPreferencesApi
import core.domain.ResourceProvider
import core.util.toTimestamp
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

class ReminderNotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resourceProvider: ResourceProvider,
    private val localPreferencesApi: LocalPreferencesApi,
) : ReminderNotificationApi {

    private val channelId = "reminders_channel"
    private val channelName = "Reminder notifications"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for user inactivity reminders"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun sendNotification(message: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("hydrateme://launch")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(com.azmarzly.core.R.drawable.ic_app_icon)
            .setContentTitle(resourceProvider.getString(com.azmarzly.core.R.string.notification_title))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
        localPreferencesApi.setLastNotificationTimestamp(LocalDateTime.now().toTimestamp())
    }
}