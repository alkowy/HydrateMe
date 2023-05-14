package core.domain

import core.model.FirestoreUserDataModel
import core.model.Resource
import core.model.UserDataModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    fun updateUserInFirestore(firestoreUser: FirestoreUserDataModel): Flow<Resource<Boolean>>
    fun fetchUserFromFirestore(userId: String): Flow<Resource<UserDataModel>>

}