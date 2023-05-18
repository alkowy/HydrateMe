package com.azmarzly.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import core.DispatcherIO
import core.model.Resource
import core.model.UserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
) : ViewModel() {

    private val _authState: MutableStateFlow<Resource<UserDataModel>> =
        MutableStateFlow(Resource.EmptyState)
    val authState = _authState.asStateFlow()

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(dispatcherIO) {
            authRepository.loginWithEmailAndPassword(email, password)
                .collectLatest { loginResult ->
                    _authState.update { loginResult }
                }
        }
    }
}
