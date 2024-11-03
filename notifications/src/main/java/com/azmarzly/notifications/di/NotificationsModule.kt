package com.azmarzly.notifications.di

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.WorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NotificationsModule {

    @Provides
    fun provideWorkerFactory(
        hiltWorkerFactory: HiltWorkerFactory,
    ): WorkerFactory = hiltWorkerFactory
}
