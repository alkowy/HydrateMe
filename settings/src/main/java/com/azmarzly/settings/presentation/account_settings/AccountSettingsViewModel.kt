package com.azmarzly.settings.presentation.account_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.core.R
import com.azmarzly.settings.domain.ResetPasswordUseCase
import core.DispatcherIO
import core.domain.ResourceProvider
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.model.Resource
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val _state: MutableStateFlow<AccountSettingsState> = MutableStateFlow(AccountSettingsState())
    val state = _state.asStateFlow()

    init {
        initialiseState()
    }

    fun saveChanges() {
        viewModelScope.launch(dispatcherIO) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                (result as? Resource.Success)?.data?.let { userData ->
                    val updatedUserData = userData.copy(
                        name = _state.value.name,
                    )
                    updateFirestoreUserUseCase.invoke(user = updatedUserData.toFirestoreUserDataModel())
                }
            }
        }
    }

    fun updateUserName(name: String) {
        _state.update {
            it.copy(
                name = name
            )
        }
    }

    fun toggleResetPasswordDialogVisibility(isVisible: Boolean) {
        _state.update {
            it.copy(
                shouldShowResetPasswordDialog = isVisible
            )
        }
    }

    fun resetPassword() {
        viewModelScope.launch(dispatcherIO) {
            resetPasswordUseCase.invoke(_state.value.email).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                shouldShowResetPasswordDialog = false,
                                toastState = AccountSettingsState.ToastState.Success
                            )
                        }
                    }

                    else -> {
                        _state.update {
                            it.copy(
                                shouldShowResetPasswordDialog = false,
                                toastState = AccountSettingsState.ToastState.Error
                            )
                        }
                    }
                }
            }
            _state.update {
                it.copy(
                    toastState = AccountSettingsState.ToastState.None
                )
            }
        }
    }

    fun validateName(input: String) {
        _state.update {
            it.copy(
                isNewNameValid = input.isNotEmpty()
            )
        }
    }

    fun signOut() {

    }

    private fun initialiseState() {
        viewModelScope.launch(dispatcherIO) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { data ->
                            _state.update {
                                AccountSettingsState(
                                    name = data.name,
                                    email = data.email,
                                    emailSentMessage = resourceProvider.getString(R.string.reset_password_sent),
                                    emailSentErrorMessage = resourceProvider.getString(R.string.reset_password_could_not_sent)
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

}

data class AccountSettingsState(
    val name: String = "",
    val email: String = "",
    val isNewNameValid: Boolean = false,
    val shouldShowResetPasswordDialog: Boolean = false,
    val emailSentMessage: String = "",
    val emailSentErrorMessage: String = "",
    val toastState: ToastState = ToastState.None,
) {
    sealed interface ToastState {
        data object None : ToastState
        data object Success : ToastState
        data object Error : ToastState
    }
}