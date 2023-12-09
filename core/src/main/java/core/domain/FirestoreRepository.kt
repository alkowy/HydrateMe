package core.domain

import core.model.FirestoreUserDataModel
import core.model.Resource
import core.model.UserDataModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun updateUserInFirestore(firestoreUser: FirestoreUserDataModel)
    fun fetchUserFromFirestore(userId: String): Flow<Resource<UserDataModel>>
    fun periodicallyGetUserDataModel(): Flow<Resource<UserDataModel?>>
}