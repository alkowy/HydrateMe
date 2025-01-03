package com.azmarzly.authentication.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AuthenticationModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }
}