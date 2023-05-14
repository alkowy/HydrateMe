package core.model

import java.time.LocalDateTime

//User profile data setup after sign up - can be edited in the profile section
data class UserDataModel(
    val uid: String = "",
    val email: String,
    val name: String,
    val gender: String? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val birthDate: LocalDateTime? = null,
    val userActivity: UserActivity = UserActivity.Empty,
    val hydrationData: List<HydrationData> = emptyList(),
    val urineScanData: List<UrineScanData> = emptyList(),
)