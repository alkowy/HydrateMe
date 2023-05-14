package core.domain.use_case

import core.LocalPreferencesApi
import core.domain.FirestoreRepository
import javax.inject.Inject

class FetchCurrentUserUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val localPreferencesApi: LocalPreferencesApi,
) {

    operator fun invoke() = firestoreRepository.fetchUserFromFirestore(localPreferencesApi.getCurrentUserId())
}