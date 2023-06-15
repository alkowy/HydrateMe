package com.azmarzly.sign_in

sealed interface SignInState {
    object EmptyState : SignInState
    object Loading : SignInState
    object Success : SignInState
    data class Error(val message: String) : SignInState
}