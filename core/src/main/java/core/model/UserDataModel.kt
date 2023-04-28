package core.model

import java.time.LocalDateTime

//User profile data setup after sign up - can be edited in the profile section
data class UserDataModel(
    val uId: String,
    val email: String,
    val name : String,
    val gender: String,
    val height: Double,
    val weight: Double,
    val birthDate: LocalDateTime,
    val userActivity: UserActivity,
)