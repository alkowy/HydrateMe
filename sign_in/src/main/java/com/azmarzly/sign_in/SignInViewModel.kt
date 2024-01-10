package com.azmarzly.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import com.azmarzly.authentication.domain.UserManager
import core.DispatcherIO
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    @Named("EmailValidator") private val emailValidator: InputValidator,
    @Named("PasswordValidator") private val passwordValidator: InputValidator,
    private val userManager: UserManager,
) : ViewModel() {

    private val _authState: MutableStateFlow<SignInState> = MutableStateFlow(SignInState.EmptyState)
    val authState = _authState.asStateFlow()

    private val _emailValidationState: MutableStateFlow<ValidationState> = MutableStateFlow(ValidationState.Empty)
    val emailValidationState: StateFlow<ValidationState> = _emailValidationState.asStateFlow()

    private val _resetPasswordEmailValidationState: MutableStateFlow<ValidationState> = MutableStateFlow(ValidationState.Empty)
    val resetPasswordEmailValidationState: StateFlow<ValidationState> = _resetPasswordEmailValidationState.asStateFlow()

    private val _passwordValidationState: MutableStateFlow<ValidationState> = MutableStateFlow(ValidationState.Empty)
    val passwordValidationState: StateFlow<ValidationState> = _passwordValidationState.asStateFlow()

    private var validationJob: Job? = null

    fun requestPasswordReset(email: String) {
        authRepository.sendPasswordResetToEmail(email)
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(dispatcherIO) {
            _authState.update { SignInState.Loading }
            authRepository.loginWithEmailAndPassword(email, password)
                .collectLatest { loginResult ->
                    when (loginResult) {
                        is Resource.Success -> _authState.update { SignInState.Success }
                        is Resource.Error -> _authState.update { SignInState.Error(loginResult.errorMessage ?: "Unexpected error") }
                        Resource.Loading -> _authState.update { SignInState.Loading }
                        Resource.EmptyState -> _authState.update { SignInState.EmptyState }
                    }
                }
        }
    }

    fun validateEmail(email: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(250.milliseconds)
            _emailValidationState.update { emailValidator.isValid(email) }
        }
    }

    fun validateResetPasswordEmail(email: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(250.milliseconds)
            _resetPasswordEmailValidationState.update { emailValidator.isValid(email) }
        }
    }

    fun validatePassword(password: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(250.milliseconds)
            _passwordValidationState.update { passwordValidator.isValid(password) }
        }
    }

    fun isLoggedIn() = userManager.isUserLoggedIn()
}