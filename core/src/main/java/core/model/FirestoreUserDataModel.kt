package core.model

import core.util.toLocalDateTime

data class FirestoreUserDataModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val gender: String? = "",
    val height: Double? = 0.0,
    val weight: Double? = 0.0,
    val birthDate: Long? = -1, // in timestamp, epoch seconds
    val userActivity: UserActivityEnum = UserActivityEnum.EMPTY,
    val hydrationData: List<FirestoreHydrationData> = emptyList(),
    val urineScanData: List<FirestoreUrineScanData> = emptyList(),
)

enum class UserActivityEnum {
    VERY_LOW, LOW, AVERAGE, HIGH, EMPTY;

    fun toUserActivity(): UserActivity {
        return when (this) {
            VERY_LOW -> UserActivity.VeryLowActivity()
            LOW -> UserActivity.LowActivity()
            AVERAGE -> UserActivity.AverageActivity()
            HIGH -> UserActivity.HighActivity()
            EMPTY -> UserActivity.Empty
        }
    }
}

data class FirestoreHydrationData(
    val date: Long,
    val goal: Double,
    val progress: Double,
    val progressInPercentage: Int,
) {
    fun toHydrationData(): HydrationData {
        return HydrationData(
            date = this.date.toLocalDateTime(),
            goal = this.goal,
            progress = this.progress,
            progressInPercentage = this.progressInPercentage,
        )
    }
}

data class FirestoreUrineScanData(
    val date: Long,
    val scanInfo: List<ScanData>
) {
    fun toUrineScanData(): UrineScanData {
        return UrineScanData(
            date = this.date.toLocalDateTime(),
            scanInfo = this.scanInfo
        )
    }
}