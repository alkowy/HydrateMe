package core.model

import core.util.toTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class HydrationData(
    val date: LocalDate,
    var goalMillis: Int,
    var hydrationChunksList: List<HydrationChunk>,
) {

    fun calculateProgressInPercents(): Int = getProgressMillis().times(100).div(goalMillis)
    fun calculateRemaining(): Int = goalMillis.minus(getProgressMillis()).coerceAtLeast(0)
    fun getProgressMillis(): Int = hydrationChunksList.sumOf { it.amount }

    fun toFirestoreHydrationData(): FirestoreHydrationData {
        return FirestoreHydrationData(
            date = this.date.toTimestamp(),
            goalMillis = this.goalMillis,
            hydrationChunksList = this.hydrationChunksList.map { it.toFirestoreHydrationChunk() }
        )
    }

    data class HydrationChunk(
        val uuid: String = UUID.randomUUID().toString(),
        val dateTime: LocalDateTime,
        val amount: Int,
    ) {
        fun toFirestoreHydrationChunk(): FirestoreHydrationChunkData {
            return FirestoreHydrationChunkData(
                uuid = this.uuid,
                dateTime = this.dateTime.toTimestamp(),
                amount = this.amount,
            )
        }

        fun amountToDrinkType(): DrinkType {
            return when (this.amount) {
                DrinkType.CUP.amountOfWater -> DrinkType.CUP
                DrinkType.BIG_CUP.amountOfWater -> DrinkType.BIG_CUP
                DrinkType.BOTTLE.amountOfWater -> DrinkType.BOTTLE
                else -> DrinkType.CUSTOM
            }
        }
    }
}