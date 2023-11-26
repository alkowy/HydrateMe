package core.model

import core.util.toTimestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class HydrationData(
    val date: LocalDate,
    var goalMillis: Int,
    var progress: Int,
    var progressInPercentage: Int,
    var hydrationChunksList: List<HydrationChunk>,
) {

    fun calculateProgress(): Int = progress.times(100).div(goalMillis)
    fun calculateRemaining(): Int = goalMillis.minus(progress).coerceAtLeast(0)

    fun toFirestoreHydrationData(): FirestoreHydrationData {
        return FirestoreHydrationData(
            date = this.date.toTimestamp(),
            goalMillis = this.goalMillis,
            progress = this.progress,
            progressInPercentage = this.progressInPercentage,
            hydrationChunksList = this.hydrationChunksList.map { it.toFirestoreHydrationChunk() }
        )
    }

    data class HydrationChunk(
        val dateTime: LocalDateTime,
        val amount: Int,
    ) {
        fun toFirestoreHydrationChunk(): FirestoreHydrationChunkData {
            return FirestoreHydrationChunkData(
                dateTime = this.dateTime.toTimestamp(),
                amount = this.amount,
            )
        }
    }
}