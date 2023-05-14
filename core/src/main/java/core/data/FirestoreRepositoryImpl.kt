package core.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import core.domain.FirestoreRepository
import core.model.FirestoreUserDataModel
import core.model.Resource
import core.util.toUserDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : FirestoreRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    override fun updateUserInFirestore(firestoreUser: FirestoreUserDataModel): Flow<Resource<Boolean>> = channelFlow<Resource<Boolean>> {
        send(Resource.Loading)
        firestore.collection(USERS_COLLECTION)
            .document(firestoreUser.uid)
            .set(firestoreUser, SetOptions.merge())
            .addOnCompleteListener {
                launch {
                    if (it.isSuccessful) send(Resource.Success(null)) else send(Resource.Error(it.exception?.message))
                }
            }

    }.catch { emit(Resource.Error(it.message)) }

    override fun fetchUserFromFirestore(userId: String) = flow {
        emit(Resource.Loading)
        val user = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(FirestoreUserDataModel::class.java)
        if (user == null) {
            emit(Resource.Error("Error fetching the user data"))
        } else {
            emit(Resource.Success(user.toUserDataModel()))
        }
    }.catch {
        emit(Resource.Error(it.message))
    }
}