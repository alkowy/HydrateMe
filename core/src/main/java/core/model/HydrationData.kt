package core.model

import core.util.toTimestamp
import java.time.LocalDateTime

data class HydrationData(
    val date: LocalDateTime,
    val goal: Double,
    val progress: Double,
    val progressInPercentage: Int,
){
    fun toFirestoreHydrationData(): FirestoreHydrationData{
        return FirestoreHydrationData(
            date = this.date.toTimestamp(),
            goal = this.goal,
            progress = this.progress,
            progressInPercentage = this.progressInPercentage
        )
    }
}