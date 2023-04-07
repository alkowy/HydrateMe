package com.azmarzly.authentication.di

import com.azmarzly.authentication.data.AuthenticationRepositoryImpl
import com.azmarzly.authentication.domain.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationRepositoryModule {

    @Binds
    abstract fun bindAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository
}