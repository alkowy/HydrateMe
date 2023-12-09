package core.model

import androidx.annotation.Keep
import core.model.HydrationData.HydrationChunk
import core.util.toLocalDate
import core.util.toLocalDateTime

@Keep
data class FirestoreUserDataModel @Keep constructor(
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
)

enum class UserActivityEnum {
    VERY_LOW, LOW, AVERAGE, HIGH, EMPTY;
}

@Keep
data class FirestoreHydrationData(
    val date: Long = -1,
    val goalMillis: Int = 2000,
    val progress: Int = 0,
    val progressInPercentage: Int = 0,
    val hydrationChunksList: List<FirestoreHydrationChunkData> = emptyList(),
) {
    fun toHydrationData(): HydrationData {
        return HydrationData(
            date = this.date.toLocalDate(),
            goalMillis = this.goalMillis,
            progress = this.progress,
            progressInPercentage = this.progressInPercentage,
            hydrationChunksList = this.hydrationChunksList.map { it.toHydrationChunk() },
        )
    }
}

@Keep
data class FirestoreHydrationChunkData(
    val dateTime: Long = -1,
    val amount: Int = 0,
) {
    fun toHydrationChunk(): HydrationChunk {
        return HydrationChunk(
            dateTime = this.dateTime.toLocalDateTime(),
            amount = this.amount,
        )
    }
}