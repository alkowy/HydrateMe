package core.domain.use_case

import core.domain.FirestoreRepository
import javax.inject.Inject

class PeriodicallyFetchUserDataModelUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) {
    operator fun invoke() = firestoreRepository.periodicallyGetUserDataModel()
}