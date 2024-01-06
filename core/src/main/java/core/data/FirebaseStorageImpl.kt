package core.data

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import core.LocalPreferencesApi
import core.domain.FirebaseStorageApi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val localPreferencesApi: LocalPreferencesApi,
) : FirebaseStorageApi {

    companion object {
        private const val PROFILE_PICTURES = "profilePictures"
    }

    override suspend fun uploadProfilePictureToStorage(uri: Uri) {
        val userId = localPreferencesApi.getCurrentUserId()
        val userProfilePictureCollectionRef = firebaseStorage.reference.child("${PROFILE_PICTURES}/$userId")

        removeOldProfilePicture(userProfilePictureCollectionRef)

        userProfilePictureCollectionRef.child("${uri.lastPathSegment}").putFile(uri).await()
    }

    override suspend fun getProfilePictureUrl(): String {
        val userId = localPreferencesApi.getCurrentUserId()
        val userProfilePictureCollectionRef = firebaseStorage.reference.child("${PROFILE_PICTURES}/$userId")
        return userProfilePictureCollectionRef.list(1).await().items.firstOrNull()?.downloadUrl?.await().toString()
    }

    private suspend fun removeOldProfilePicture(ref: StorageReference) {
        ref.listAll().addOnSuccessListener { result ->
            Log.d("ANANAS", "removeOldProfilePicture: reuslt ${result.items.toString()}")
            result.items.forEach { item ->
                Log.d("ANANAS", "removeOldProfilePicture: item! ${item.downloadUrl.toString()}")
                item.delete()
            }
        }.await()
    }
}