package core.domain

import core.model.FirestoreUserDataModel
import core.model.Resource
import core.model.UserDataModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

   suspend fun updateUserInFirestore(firestoreUser: FirestoreUserDataModel): Flow<Resource<UserDataModel>>
    fun fetchUserFromFirestore(userId: String): Flow<Resource<UserDataModel>>

}