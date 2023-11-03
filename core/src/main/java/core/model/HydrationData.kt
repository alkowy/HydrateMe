package core.model

import core.util.toTimestamp
import java.time.LocalDateTime

data class HydrationData(
    val date: LocalDateTime,
    var goalMillis: Int,
    var progress: Int,
    var progressInPercentage: Int,
) {

    fun calculateProgress(): Int = progress.times(100).div(goalMillis)
    fun calculateRemaining(): Int = goalMillis.minus(progress).coerceAtLeast(0)

    fun toFirestoreHydrationData(): FirestoreHydrationData {
        return FirestoreHydrationData(
            date = this.date.toTimestamp(),
            goalMillis = this.goalMillis,
            progress = this.progress,
            progressInPercentage = this.progressInPercentage
        )
    }
}