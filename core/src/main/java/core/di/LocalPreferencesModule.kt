package core.di

import core.LocalPreferencesApi
import core.LocalPreferencesService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalPreferencesModule {

    @Binds
    abstract fun bindLocalPreferences(localPreferencesService: LocalPreferencesService): LocalPreferencesApi
}