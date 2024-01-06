package core.domain.use_case

import android.net.Uri
import core.domain.FirebaseStorageApi
import core.domain.FirestoreRepository
import core.model.UserDataModel
import core.util.toFirestoreUserDataModel
import javax.inject.Inject

class UpdateProfilePictureUseCase @Inject constructor(
    private val firebaseStorageApi: FirebaseStorageApi,
    private val firestoreRepository: FirestoreRepository,
) {

    suspend operator fun invoke(uri: Uri, currentUserData: UserDataModel) {
        firebaseStorageApi.uploadProfilePictureToStorage(uri)
        val newProfilePictureUrl = firebaseStorageApi.getProfilePictureUrl()

        firestoreRepository.updateUserInFirestore(
            currentUserData.toFirestoreUserDataModel().copy(
                profileImageUrl = newProfilePictureUrl
            )
        )
    }
}