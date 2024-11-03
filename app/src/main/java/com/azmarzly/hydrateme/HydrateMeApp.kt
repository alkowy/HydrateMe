package com.azmarzly.hydrateme

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.azmarzly.notifications.RemindersWorker.Companion.startRemindersWork
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HydrateMeApp() : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        startRemindersWork(applicationContext)
    }

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}