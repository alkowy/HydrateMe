package com.azmarzly.registration.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import core.DispatcherIO
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.model.Resource
import core.model.UserDataModel
import core.util.doNothing
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val authRepository: AuthenticationRepository,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
) : ViewModel() {

    private val _registrationState: MutableStateFlow<Resource<RegistrationState>> = MutableStateFlow(Resource.EmptyState)
    val registrationState = _registrationState.asStateFlow()

    init {
        Log.d("ANANAS", "initialsed vm: ")
    }

    fun registerWithEmailAndPassword(email: String, password: String, userModel: UserDataModel) {
        viewModelScope.launch(dispatcherIO) {
            authRepository.registerWithEmailAndPassword(email, password, userModel)
                .collectLatest { registrationResult ->
                    when (registrationResult) {
                        Resource.Loading -> _registrationState.update { Resource.Loading }
                        is Resource.Success -> {
                            _registrationState.update {
                                Resource.Success(
                                    RegistrationState.InitialInfoAndRegistration(
                                        userModel = registrationResult.data,
                                    )
                                )
                            }
                        }

                        is Resource.Error -> _registrationState.update { Resource.Error(registrationResult.errorMessage) }
                        Resource.EmptyState -> doNothing()
                    }

                }
        }
    }

    //to be called when closing error dialog?
    fun tryToFetchUserData(step: RegistrationStep) {
        viewModelScope.launch(dispatcherIO) {
            fetchCurrentUserUseCase().collect { fetchResult ->
                when (fetchResult) {
                    is Resource.Success -> {
                        when (step) {
                            RegistrationStep.INITIAL -> doNothing()
                            RegistrationStep.GENDER -> _registrationState.update { Resource.Success(RegistrationState.GenderInfo(fetchResult.data!!)) }
                            RegistrationStep.MEASUREMENTS -> _registrationState.update { Resource.Success(RegistrationState.MeasurementsInfo(fetchResult.data!!)) }
                            RegistrationStep.ACTIVITY -> _registrationState.update { Resource.Success(RegistrationState.ActivityInfo(fetchResult.data!!)) }
                        }
                    }

                    is Resource.Error -> _registrationState.update { Resource.Error(fetchResult.errorMessage) }
                    Resource.Loading -> _registrationState.update { Resource.Loading }
                    Resource.EmptyState -> doNothing()
                }
            }

        }
    }

    fun updateUserDataAndMoveToStep(userModel: UserDataModel, nextStep: RegistrationStep) {
        viewModelScope.launch(dispatcherIO) {
            updateFirestoreUserUseCase(userModel.toFirestoreUserDataModel()).collect { updateResult ->
                when (updateResult) {
                    is Resource.Success -> {
                        when (nextStep) {
                            RegistrationStep.INITIAL -> doNothing()
                            RegistrationStep.GENDER -> _registrationState.update { Resource.Success(RegistrationState.GenderInfo(userModel)) }
                            RegistrationStep.MEASUREMENTS -> _registrationState.update { Resource.Success(RegistrationState.MeasurementsInfo(userModel)) }
                            RegistrationStep.ACTIVITY -> _registrationState.update { Resource.Success(RegistrationState.ActivityInfo(userModel)) }
                        }
                    }

                    is Resource.Error -> _registrationState.update { Resource.Error(updateResult.errorMessage) }
                    Resource.Loading -> _registrationState.update { Resource.Loading }
                    Resource.EmptyState -> doNothing()
                }
            }

        }
    }
}

// 1 basic; 2 gender; 3 height & weight; 4 activity -> display goal -> proceed to home
sealed interface RegistrationState {
    data class InitialInfoAndRegistration(val userModel: UserDataModel?) : RegistrationState
    data class GenderInfo(val userModel: UserDataModel) : RegistrationState
    data class MeasurementsInfo(val userModel: UserDataModel) : RegistrationState
    data class ActivityInfo(val userModel: UserDataModel) : RegistrationState
}

enum class RegistrationStep {
    INITIAL, GENDER, MEASUREMENTS, ACTIVITY;
}