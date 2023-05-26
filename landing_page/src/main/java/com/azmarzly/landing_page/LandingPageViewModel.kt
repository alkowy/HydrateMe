package com.azmarzly.landing_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import com.azmarzly.authentication.domain.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val userManager: UserManager,
    private val authenticationRepository: AuthenticationRepository, // remove
) : ViewModel() {

    val state: StateFlow<LandingPageState> = flow {
        delay(1500)
        emit(userManager.isUserLoggedIn().toLandingPageState())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LandingPageState.Loading
    )

    // remove
    fun login(email: String = "test@gmail.com", password: String = "12345qwerty") {
        viewModelScope.launch {
            authenticationRepository.loginWithEmailAndPassword(email, password).collect {
                Log.d("ANANAS", "login: $it")
                Log.d("ANANAS", "login: current User: ${userManager.getCurrentUserId()}")

            }

        }
    }

    fun signOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
            Log.d("ANANAS", "signOut: currentUser ${userManager.getCurrentUserId()}")
        }
    }

}

private fun Boolean.toLandingPageState(): LandingPageState {
    return when (this) {
        true -> LandingPageState.LoggedIn
        false -> LandingPageState.NotLoggedIn
    }
}