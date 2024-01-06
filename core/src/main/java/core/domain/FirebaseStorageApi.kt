package core.domain

import android.net.Uri

interface FirebaseStorageApi {

    suspend fun uploadProfilePictureToStorage(uri: Uri)
    suspend fun getProfilePictureUrl(): String
}