package com.azmarzly.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.DispatcherIO
import core.LocalPreferencesApi
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.model.FirestoreUserDataModel
import core.model.Resource
import core.model.UserDataModel
import core.util.doNothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
) : ViewModel() {

    private val _userDataState: MutableStateFlow<Resource<UserDataModel>> = MutableStateFlow(Resource.EmptyState)
    val userDataState: StateFlow<Resource<UserDataModel>> = _userDataState.asStateFlow()

    private val _updatingUserState: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(Resource.EmptyState)
    val updatingUserState: StateFlow<Resource<Boolean>> = _updatingUserState.asStateFlow()

    fun fetchCurrentUser() {
        viewModelScope.launch(dispatcherIO) {
            fetchCurrentUserUseCase().collect { user ->
                _userDataState.update { user }
            }
        }
    }

    fun updateFirestoreUser(updatedUserData: FirestoreUserDataModel) {
        viewModelScope.launch(dispatcherIO) {
            updateFirestoreUserUseCase(updatedUserData)
                .collect { updateResult ->
                    when (updateResult) {
                        is Resource.Error -> _updatingUserState.update { updateResult }
                        is Resource.Success -> _updatingUserState.update { updateResult }
                        Resource.Loading, Resource.EmptyState -> doNothing()
                    }
                }
        }
    }
}