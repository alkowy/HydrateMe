package core.domain

import androidx.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes stringResId: Int): String
}