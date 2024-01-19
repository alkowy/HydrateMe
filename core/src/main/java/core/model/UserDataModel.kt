package core.model

import com.azmarzly.core.R
import core.util.isSameDayAs
import java.time.LocalDate

//User profile data setup after sign up - can be edited in the profile section
data class UserDataModel(
    val uid: String = "",
    val email: String,
    val name: String,
    val gender: Gender? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val profileImageUrl: String = "",
    val birthDate: LocalDate? = null,
    val hydrationGoalMillis: Int = 2000,
    val userActivity: UserActivityEnum = UserActivityEnum.EMPTY,
    val hydrationData: List<HydrationData> = emptyList(),
) {
    fun findHydrationDataForDate(date: LocalDate): HydrationData? {
        return hydrationData.find { it.date.isSameDayAs(date) }
    }

}

enum class Gender {
    MALE, FEMALE;
}

fun Gender.toNameResourceStringId(): Int {
    return when (this) {
        Gender.MALE -> R.string.gender_male
        Gender.FEMALE -> R.string.gender_female
    }
}

fun Gender.toDrawableIconRes(): Int {
    return when (this) {
        Gender.MALE -> R.drawable.ic_male
        Gender.FEMALE -> R.drawable.ic_female
    }
}