package core.domain.use_case

import core.domain.FirestoreRepository
import core.model.FirestoreUserDataModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class UpdateFirestoreUserUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(user: FirestoreUserDataModel) = firestoreRepository.updateUserInFirestore(user)
}