package core.model

import androidx.annotation.DrawableRes

data class GenderState(
    val name: String = "",
    val gender: Gender,
    @DrawableRes val genderIcon: Int,
)