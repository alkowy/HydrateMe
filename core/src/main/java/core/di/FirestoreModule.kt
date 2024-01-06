package core.di

import core.data.FirebaseStorageImpl
import core.data.FirestoreRepositoryImpl
import core.domain.FirebaseStorageApi
import core.domain.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirestoreModule {

    @Binds
    @Singleton
    abstract fun bindFirestoreRepository(firestoreRepositoryImpl: FirestoreRepositoryImpl): FirestoreRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseStorage(firebaseStorage: FirebaseStorageImpl): FirebaseStorageApi
}