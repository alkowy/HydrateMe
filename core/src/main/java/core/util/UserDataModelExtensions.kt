package core.util

import core.model.FirestoreUserDataModel
import core.model.UserDataModel
import core.model.toUserActivityEnum

fun UserDataModel.toFirestoreUserDataModel(): FirestoreUserDataModel {
    return FirestoreUserDataModel(
        uid = this.uid,
        email = this.email,
        birthDate = this.birthDate?.toTimestamp(),
        gender = this.gender,
        height = this.height,
        name = this.name,
        weight = this.weight,
        userActivity = this.userActivity,
        hydrationData = this.hydrationData.map { it.toFirestoreHydrationData() },
        urineScanData = this.urineScanData.map { it.toFirestoreUrineScanData() }
    )
}

fun FirestoreUserDataModel.toUserDataModel(): UserDataModel {
    return UserDataModel(
        uid = this.uid,
        email = this.email,
        birthDate = this.birthDate?.toLocalDateTime(),
        gender = this.gender,
        height = this.height,
        name = this.name,
        weight = this.weight,
        userActivity = this.userActivity,
        hydrationData = this.hydrationData.map { it.toHydrationData() },
        urineScanData = this.urineScanData.map { it.toUrineScanData() },
    )
}