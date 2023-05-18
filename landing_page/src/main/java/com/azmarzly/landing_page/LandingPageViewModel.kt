package com.azmarzly.landing_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val userManager: UserManager,
) : ViewModel() {

    val state: StateFlow<LandingPageState> = flow {
        delay(1500)
        emit(userManager.isUserLoggedIn().toLandingPageState())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LandingPageState.Loading
    )

}

private fun Boolean.toLandingPageState(): LandingPageState {
    return when (this) {
        true -> LandingPageState.LoggedIn
        false -> LandingPageState.NotLoggedIn
    }
}