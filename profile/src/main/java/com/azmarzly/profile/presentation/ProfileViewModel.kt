package com.azmarzly.profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import com.azmarzly.authentication.domain.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userManager: UserManager, // remove
    private val authenticationRepository: AuthenticationRepository, // remove + remove the dependency from gradle or not
) : ViewModel() {

    // remove
    fun login(email: String = "test@gmail.com", password: String = "12345qwerty") {
        viewModelScope.launch {
            authenticationRepository.loginWithEmailAndPassword(email, password).collect {
                Log.d("ANANAS", "login: $it")
                Log.d("ANANAS", "login: current User: ${userManager.getCurrentUserId()}")

            }

        }
    }

    // remove
    fun signOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
            Log.d("ANANAS", "signOut: currentUser ${userManager.getCurrentUserId()}")
        }
    }
}