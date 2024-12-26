package core.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import core.LocalPreferencesApi
import core.domain.FirebaseStorageApi
import core.model.HydrationHubContentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import javax.inject.Inject

class FirebaseStorageImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val localPreferencesApi: LocalPreferencesApi,
    private val gson: Gson,
) : FirebaseStorageApi {

    companion object {
        private const val PROFILE_PICTURES = "profilePictures"
        private const val HYDRATION_HUB_CONTENT = "hydrationHubContent"
        private const val HYDRATION_HUB_CONTENT_FILE_JSON = "hydrationContent.json"
        private const val PL_LOCALE = "pl"
        private const val EN_LOCALE = "en"
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

    override suspend fun getHydrationHubContent(locale: String): HydrationHubContentModel? {
        return try {
            val language = if (locale == PL_LOCALE) PL_LOCALE else EN_LOCALE
            val contentReference = firebaseStorage.reference.child("${HYDRATION_HUB_CONTENT}/$language/$HYDRATION_HUB_CONTENT_FILE_JSON")
            val fileBytes = contentReference.getBytes(Long.MAX_VALUE).await()

            val contentString = fileBytes.decodeToString()

            gson.fromJson(contentString, HydrationHubContentModel::class.java)
        } catch (e: Exception) {
            null
        }

    }

    private suspend fun removeOldProfilePicture(ref: StorageReference) {
        ref.listAll().addOnSuccessListener { result ->
            result.items.forEach { item ->
                item.delete()
            }
        }.await()
    }
}