package com.azmarzly.sign_in

sealed interface SignInState {
    object EmptyState : SignInState
    object Loading : SignInState
    object Success : SignInState
    data class Error(val message: String) : SignInState
}

data class ForgotPasswordState(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val emailSent: Boolean = false,
    val email: String = "",
    val isVisible: Boolean = false,
    val emailSentMessage: String = ""
)