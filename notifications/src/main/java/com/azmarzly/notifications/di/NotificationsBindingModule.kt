package com.azmarzly.notifications.di

import com.azmarzly.notifications.ReminderNotificationApi
import com.azmarzly.notifications.ReminderNotificationService
import com.azmarzly.notifications.UserInactivityChecker
import com.azmarzly.notifications.UserInactivityCheckerApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsBindingModule {

    @Binds
    @Singleton
    abstract fun bindsUserInactivityChecker(service: UserInactivityChecker): UserInactivityCheckerApi

    @Binds
    @Singleton
    abstract fun provideReminderNotificationApi(service: ReminderNotificationService): ReminderNotificationApi
}