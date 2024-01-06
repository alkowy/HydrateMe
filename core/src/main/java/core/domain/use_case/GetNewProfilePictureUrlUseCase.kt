package core.domain.use_case

import core.domain.FirebaseStorageApi
import javax.inject.Inject

class GetNewProfilePictureUrlUseCase @Inject constructor(
    private val firebaseStorageApi: FirebaseStorageApi,
) {

    suspend operator fun invoke() = firebaseStorageApi.getProfilePictureUrl()
}