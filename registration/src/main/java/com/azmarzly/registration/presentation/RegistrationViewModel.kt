package com.azmarzly.registration.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import core.DispatcherIO
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.input_validators.InputValidator
import core.model.Resource
import core.model.UserDataModel
import core.util.RegistrationRoute
import core.util.doNothing
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val authRepository: AuthenticationRepository,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
) : ViewModel() {

    private val _registrationState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState(currentStep = RegistrationRoute.INITIAL))
    val registrationState = _registrationState.asStateFlow()

    private val _registrationBottomBarState: MutableStateFlow<RegistrationBottomBarState> = MutableStateFlow(RegistrationBottomBarState())
    val bottomNavigationBarState = _registrationBottomBarState.asStateFlow()

    fun updateBottomBarButtonStatus(isButtonEnabled: Boolean) {
        _registrationBottomBarState.update {
            it.copy(
                isNextButtonEnabled = isButtonEnabled
            )
        }
    }

    fun registerWithEmailAndPassword(email: String, password: String, userModel: UserDataModel) {
        viewModelScope.launch(dispatcherIO) {
            _registrationState.update {
                _registrationState.value.copy(
                    isLoading = true,
                    error = null,
                )
            }
            _registrationBottomBarState.update {
                _registrationBottomBarState.value.copy(
                    isNextButtonEnabled = false,
                )
            }
            authRepository.registerWithEmailAndPassword(email, password, userModel)
                .collectLatest { registrationResult ->
                    when (registrationResult) {
                        Resource.Loading -> {
                            _registrationState.update {
                                _registrationState.value.copy(
                                    isLoading = true,
                                    error = null,
                                )
                            }
                        }

                        is Resource.Success -> {
                            _registrationState.update {
                                _registrationState.value.copy(
                                    userModel = registrationResult.data,
                                    isLoading = false,
                                    isRegistrationSuccessful = true,
                                )
                            }
                        }

                        is Resource.Error -> {
                            _registrationState.update {
                                _registrationState.value.copy(
                                    isLoading = false,
                                    error = registrationResult.errorMessage
                                )
                            }
                            _registrationBottomBarState.update {
                                _registrationBottomBarState.value.copy(
                                    isNextButtonEnabled = true,
                                )
                            }
                        }

                        Resource.EmptyState -> {
                            doNothing()
                        }
                    }

                }
        }
    }

    fun changeCurrentStep(step: RegistrationRoute) {
        _registrationState.update { it.copy(currentStep = step) }
    }

    fun updateUserData(userModel: UserDataModel) {
        Log.d("ANANAS", "updateUserData:  $userModel")
        viewModelScope.launch(dispatcherIO) {
            _registrationState.update {
                _registrationState.value.copy(
                    isLoading = true,
                    error = null,
                )
            }
            updateFirestoreUserUseCase(userModel.toFirestoreUserDataModel()).collect { updateResult ->
                Log.d("ANANAS", "updateUserData: result $updateResult")
                when (updateResult) {
                    is Resource.Success -> {
                        _registrationState.update {
                            _registrationState.value.copy(
                                userModel = updateResult.data,
                                isLoading = false,
                                error = null,
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
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false,
)

data class RegistrationBottomBarState(
    val isNextButtonEnabled: Boolean = false,
    val currentStep: RegistrationRoute = RegistrationRoute.INITIAL,
)