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
import core.util.RegistrationRoute
import core.util.Route
import core.util.doNothing
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _registrationState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState(currentStep = RegistrationRoute.INITIAL))
    val registrationState = _registrationState.asStateFlow()


    init {
        Log.d("ANANAS", "initialsed vm: ")
    }

    fun registerWithEmailAndPassword(email: String, password: String, userModel: UserDataModel) {
        viewModelScope.launch(dispatcherIO) {
            _registrationState.update {
                _registrationState.value.copy(
                    isLoading = true,
                    error = null,
                )
            }
            authRepository.registerWithEmailAndPassword(email, password, userModel)
                .collectLatest { registrationResult ->
                    when (registrationResult) {
                        Resource.Loading -> _registrationState.update {
                            _registrationState.value.copy(
                                isLoading = true
                            )
                        }

                        is Resource.Success -> {
                            _registrationState.update {
                                _registrationState.value.copy(
                                    userModel = registrationResult.data,
                                    isLoading = false,
                                    currentStep = it.nextStep as RegistrationRoute,
                                )
                            }
                        }

                        is Resource.Error -> _registrationState.update {
                            _registrationState.value.copy(
                                isLoading = false,
                                error = registrationResult.errorMessage
                            )
                        }

                        Resource.EmptyState -> doNothing()
                    }

                }
        }
    }

//    //to be called when closing error dialog?
//    fun tryToFetchUserData(step: RegistrationRoute) {
//        viewModelScope.launch(dispatcherIO) {
//            fetchCurrentUserUseCase().collect { fetchResult ->
//                when (fetchResult) {
//                    is Resource.Success -> {
//                        when (step) {
//                            RegistrationRoute.GENDER -> _registrationState.update { Resource.Success(RegistrationState.GenderInfo(fetchResult.data!!)) }
//                            RegistrationRoute.AGE -> _registrationState.update { Resource.Success(RegistrationState.AgeInfo(fetchResult.data!!)) }
//                            RegistrationRoute.MEASUREMENTS_HEIGHT -> _registrationState.update { Resource.Success(RegistrationState.HeightInfo(fetchResult.data!!)) }
//                            RegistrationRoute.MEASUREMENTS_WEIGHT -> _registrationState.update { Resource.Success(RegistrationState.WeightInfo(fetchResult.data!!)) }
//                            RegistrationRoute.ACTIVITY -> _registrationState.update { Resource.Success(RegistrationState.ActivityInfo(fetchResult.data!!)) }
//                            else -> doNothing()
//                        }
//                    }
//
//                    is Resource.Error -> _registrationState.update { Resource.Error(fetchResult.errorMessage) }
//                    Resource.Loading -> _registrationState.update { Resource.Loading }
//                    Resource.EmptyState -> doNothing()
//                }
//            }
//
//        }
//    }

    fun changeCurrentStep(step: RegistrationRoute) {
        _registrationState.update { it.copy(currentStep = step) }
    }

    fun updateUserDataAndMoveToStep(userModel: UserDataModel, nextStep: RegistrationRoute) {
        Log.d("ANANAS", "updateUserDataAndMoveToStep: $userModel, $nextStep")
        viewModelScope.launch(dispatcherIO) {
            _registrationState.update {
                _registrationState.value.copy(
                    isLoading = true,
                    error = null,
                )
            }
            updateFirestoreUserUseCase(userModel.toFirestoreUserDataModel()).collect { updateResult ->
                Log.d("ANANAS", "updateUserDataAndMoveToStep: updateresult $updateResult")
                when (updateResult) {
                    is Resource.Success -> {
                        _registrationState.update {
                            _registrationState.value.copy(
                                userModel = updateResult.data,
                                isLoading = false,
                                error = null,
                                currentStep = nextStep,
                                nextStep = nextStep.nextRegistrationStep(),
                                previousStep = nextStep.previousRegistrationStep(),
                            )
                        }
                    }

                    is Resource.Error -> _registrationState.update {
                        _registrationState.value.copy(
                            error = updateResult.errorMessage
                        )
                    }

                    Resource.Loading -> _registrationState.update {
                        _registrationState.value.copy(
                            error = null,
                            isLoading = true
                        )
                    }

                    Resource.EmptyState -> doNothing()
                }
            }

        }
    }
}


data class RegistrationState(
    val userModel: UserDataModel? = null,
    val currentStep: RegistrationRoute,
    val nextStep: Route = currentStep.nextRegistrationStep(),
    val previousStep: RegistrationRoute = currentStep.previousRegistrationStep(),
    val isLoading: Boolean = false,
    val error: String? = null,
)