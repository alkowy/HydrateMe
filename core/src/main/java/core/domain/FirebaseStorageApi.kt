package core.domain

import android.net.Uri
import core.model.HydrationHubContentModel

interface FirebaseStorageApi {

    suspend fun uploadProfilePictureToStorage(uri: Uri)
    suspend fun getProfilePictureUrl(): String
    suspend fun getHydrationHubContent(locale: String): HydrationHubContentModel?
}