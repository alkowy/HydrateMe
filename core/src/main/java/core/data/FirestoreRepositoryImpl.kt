package core.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import core.domain.FirestoreRepository
import core.model.FirestoreUserDataModel
import core.model.Resource
import core.model.UserDataModel
import core.util.toUserDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Dictionary
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : FirestoreRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    override suspend fun updateUserInFirestore(firestoreUser: FirestoreUserDataModel): Flow<Resource<UserDataModel>> = channelFlow<Resource<UserDataModel>> {
        send(Resource.Loading)
        val task = firestore.collection(USERS_COLLECTION)
            .document(firestoreUser.uid)
            .set(firestoreUser, SetOptions.merge())

        task.addOnCompleteListener {  }.await()

        Log.d("ANANAS", "updateUserInFirestore: $task \n isscucess${task.isSuccessful} iscomplete ${task.isComplete}")
        when {
            task.isSuccessful -> send(Resource.Success(firestoreUser.toUserDataModel()))
            task.isComplete && task.isSuccessful.not() -> send(Resource.Error(task.exception?.message))
        }
    }.catch { emit(Resource.Error(it.message)) }

    override fun fetchUserFromFirestore(userId: String) = flow {
        emit(Resource.Loading)
        val user = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(FirestoreUserDataModel::class.java)

        Log.d("ANANAS", "fetchUserFromFirestore: in firestorerepo: $user")
        if (user == null) {
            emit(Resource.Error("Error fetching the user data"))
        } else {
            emit(Resource.Success(user.toUserDataModel()))
        }
    }.catch {
        emit(Resource.Error(it.message))
    }
}