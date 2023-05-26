package core.model

import core.util.toTimestamp
import java.time.LocalDateTime

data class HydrationData(
    val date: LocalDateTime,
    var goal: Double = 2000.0,
    var progress: Double,
    var progressInPercentage: Int,
) {

    fun calculateProgress(): Int = progress.div(goal).times(10).toInt()

    fun toFirestoreHydrationData(): FirestoreHydrationData {
        return FirestoreHydrationData(
            date = this.date.toTimestamp(),
            goal = this.goal,
            progress = this.progress,
            progressInPercentage = this.progressInPercentage
        )
    }
}