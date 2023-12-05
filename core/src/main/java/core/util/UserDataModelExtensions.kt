package core.util

import core.model.FirestoreUserDataModel
import core.model.UserDataModel

fun UserDataModel.toFirestoreUserDataModel(): FirestoreUserDataModel {
    return FirestoreUserDataModel(
        uid = this.uid,
        email = this.email,
        birthDate = this.birthDate?.toTimestamp(),
        gender = this.gender,
        height = this.height,
        name = this.name,
        weight = this.weight,
        hydrationGoalMillis = this.hydrationGoalMillis,
        userActivity = this.userActivity,
        hydrationData = this.hydrationData.map { it.toFirestoreHydrationData() },
    )
}

fun FirestoreUserDataModel.toUserDataModel(): UserDataModel {
    return UserDataModel(
        uid = this.uid,
        email = this.email,
        birthDate = this.birthDate?.toLocalDate(),
        gender = this.gender,
        height = this.height,
        name = this.name,
        weight = this.weight,
        hydrationGoalMillis = this.hydrationGoalMillis,
        userActivity = this.userActivity,
        hydrationData = this.hydrationData.map { it.toHydrationData() },
    )
}