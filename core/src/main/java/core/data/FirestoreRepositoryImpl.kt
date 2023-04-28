package core.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import core.domain.FirestoreRepository
import core.model.UserDataModel
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreRepository {

    override fun testfun(userModel: UserDataModel) {
        Log.d("ANANAS", "testfun: $userModel")
        firestore.collection("users")
            .add(userModel)
            .addOnSuccessListener { documentRef ->
                Log.d("ANANAS", "add success: $documentRef")
            }
            .addOnFailureListener { failure ->
                Log.d("ANANAS", "add failure: ${failure.message}")
            }
        firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach {
                    Log.d("ANANAS", "testfun getcollectionsuccess: $it")
                }
            }
            .addOnFailureListener { result ->
                Log.d("ANANAS", "testfuncollection failure: ${result.message}")
            }

        Log.d("ANANAS", "random firestore logs:\n${firestore.app.name}\n${firestore.firestoreSettings.host}")
    }
}