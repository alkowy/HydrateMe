package core.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import core.LocalPreferencesApi
import core.domain.FirestoreRepository
import core.model.FirestoreUserDataModel
import core.model.Resource
import core.model.UserDataModel
import core.util.toUserDataModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val localPreferencesApi: LocalPreferencesApi,
) : FirestoreRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    override suspend fun updateUserInFirestore(firestoreUser: FirestoreUserDataModel) {
        firestore.collection(USERS_COLLECTION)
            .document(firestoreUser.uid)
            .set(firestoreUser, SetOptions.merge())
            .await()
    }

    override fun fetchUserFromFirestore(userId: String) = channelFlow {
        send(Resource.Loading)
        val user = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(FirestoreUserDataModel::class.java)

        if (user == null) {
            send(Resource.Error("Error fetching the user data"))
        } else {
            send(Resource.Success(user.toUserDataModel()))
        }
    }.catch {
        emit(Resource.Error(it.message))
    }

    override fun periodicallyGetUserDataModel(): Flow<Resource<UserDataModel?>> = callbackFlow {
        firestore.collection(USERS_COLLECTION)
            .document(localPreferencesApi.getCurrentUserId())
            .addSnapshotListener { value, error ->
                error?.let {
                    trySend(Resource.Error(it.localizedMessage))
                }

                value?.let {
                    trySend(Resource.Success(it.toObject(FirestoreUserDataModel::class.java)?.toUserDataModel()))
                }
            }
        awaitClose { this.cancel() }
    }
}
