package com.azmarzly.authentication.di

import com.azmarzly.authentication.data.AuthenticationRepositoryImpl
import com.azmarzly.authentication.data.UserManagerImpl
import com.azmarzly.authentication.domain.AuthenticationRepository
import com.azmarzly.authentication.domain.UserManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationBindingsModule {

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindUserManager(userManager: UserManagerImpl): UserManager
}