package core.model

import java.time.LocalDateTime

data class UserDataModel(
    val email: String,
    val name : String,
    val gender: String,
    val height: Double,
    val weight: Double,
    val birthDate: LocalDateTime,
    val userActivity: UserActivity,
)