package com.azmarzly.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import core.DispatcherIO
import core.domain.FirestoreRepository
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.model.FirestoreUserDataModel
import core.model.Resource
import core.model.UserDataModel
import core.util.doNothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val firestore: FirestoreRepository,

    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase, // todo remove from this vm - for testing only
) : ViewModel() {

    private val _authState: MutableStateFlow<Resource<UserDataModel>> =
        MutableStateFlow(Resource.EmptyState)
    val authState = _authState.asStateFlow()

    val updateUserState: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(Resource.EmptyState)

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(dispatcherIO) {
            authRepository.loginWithEmailAndPassword(email, password)
                .collectLatest { loginResult ->
                    _authState.update { loginResult }
                }
        }
    }


    //todo to be moved somewhere else, definitely not authVM - here for testing. use case to be used probably in homeVM/profileVM when updating hydration/profiel data
    fun updateFirestoreUser(firestoreUser: FirestoreUserDataModel) {
        viewModelScope.launch(dispatcherIO) {
            updateFirestoreUserUseCase(firestoreUser)
                .collect { updateResult ->
                    when (updateResult) {
                        is Resource.Error -> updateUserState.update { updateResult }
                        is Resource.Success -> updateUserState.update { updateResult }
                        Resource.Loading, Resource.EmptyState -> doNothing()
                    }
                }
        }
    }

}