package core.model

import core.util.toTimestamp
import java.time.LocalDateTime

data class UrineScanData(
    val date: LocalDateTime,
    val scanInfo: List<ScanData>,
) {
    fun toFirestoreUrineScanData(): FirestoreUrineScanData {
        return FirestoreUrineScanData(
            date = this.date.toTimestamp(),
            scanInfo = this.scanInfo
        )
    }
}