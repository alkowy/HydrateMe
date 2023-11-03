package core.model

import core.util.toLocalDateTime

data class FirestoreUserDataModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val gender: Gender? = null,
    val height: Double? = 0.0,
    val weight: Double? = 0.0,
    val birthDate: Long? = -1, // in timestamp, epoch seconds
    val hydrationGoalMillis: Int = 0,
    val userActivity: UserActivityEnum = UserActivityEnum.EMPTY,
    val hydrationData: List<FirestoreHydrationData> = emptyList(),
    val urineScanData: List<FirestoreUrineScanData> = emptyList(),
)

enum class UserActivityEnum {
    VERY_LOW, LOW, AVERAGE, HIGH, EMPTY;
}

data class FirestoreHydrationData(
    val date: Long = -1,
    val goalMillis: Int = 2000,
    val progress: Int = 0,
    val progressInPercentage: Int = 0,
) {
    fun toHydrationData(): HydrationData {
        return HydrationData(
            date = this.date.toLocalDateTime(),
            goalMillis = this.goalMillis,
            progress = this.progress,
            progressInPercentage = this.progressInPercentage,
        )
    }
}

data class FirestoreUrineScanData(
    val date: Long,
    val scanInfo: List<ScanData>,
) {
    fun toUrineScanData(): UrineScanData {
        return UrineScanData(
            date = this.date.toLocalDateTime(),
            scanInfo = this.scanInfo
        )
    }
}