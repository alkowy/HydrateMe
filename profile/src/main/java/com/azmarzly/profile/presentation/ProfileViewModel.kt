package com.azmarzly.profile.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.DispatcherIO
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.GetNewProfilePictureUrlUseCase
import core.domain.use_case.UpdateProfilePictureUseCase
import core.model.Resource
import core.model.UserActivityEnum
import core.model.UserDataModel
import core.util.roundToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase,
    private val getNewProfilePictureUrlUseCase: GetNewProfilePictureUrlUseCase,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val EMPTY_VALUE = "-"
        private val emptyProfileState = ProfileUiState(
            profileImageUrl = "",
            useLocalImageFromUri = false,
            name = EMPTY_VALUE,
            email = EMPTY_VALUE,
            age = EMPTY_VALUE,
            weight = EMPTY_VALUE,
            height = EMPTY_VALUE,
            activity = UserActivityEnum.EMPTY,
            dailyGoal = "2.2 L",
        )
    }

    private val _profileState: MutableStateFlow<ProfileUiState> = MutableStateFlow(emptyProfileState)
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()

    private var updateProfileJob: Job? = null

    init {
        initialiseProfileState()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateProfilePicture(uri: Uri) {
        updateProfileJob?.cancel()
        updateProfileJob = GlobalScope.launch(dispatcherIO) {
            var currentUser: UserDataModel? = null
            fetchCurrentUserUseCase.invoke().collectLatest {
                if (it is Resource.Success) {
                    currentUser = it.data
                }
            }
            currentUser?.let { userData ->
                _profileState.update {
                    it.copy(
                        useLocalImageFromUri = true
                    )
                }
                updateProfilePictureUseCase.invoke(uri = uri, currentUserData = userData)
            }
            _profileState.update {
                it.copy(
                    profileImageUrl = getNewProfilePictureUrlUseCase(),
                )
            }
        }
    }

    private fun initialiseProfileState() {
        val today = LocalDate.now()
        viewModelScope.launch(dispatcherIO) {
            fetchCurrentUserUseCase.invoke()
                .collectLatest { result ->
                    Log.d("ANANAS", "initialiseProfileState: result $result")
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _profileState.update {
                                    ProfileUiState(
                                        profileImageUrl = data.profileImageUrl,
                                        name = data.name,
                                        email = data.email,
                                        age = if (data.birthDate == null) EMPTY_VALUE else today.year.minus(data.birthDate!!.year).toString(),
                                        weight = if (data.weight == null) EMPTY_VALUE else data.weight.toString(),
                                        height = if (data.height == null) EMPTY_VALUE else data.weight.toString(),
                                        activity = data.userActivity,
                                        dailyGoal = data.hydrationGoalMillis.div(1000).toDouble().roundToString(decimals = 2) + " L",
                                        useLocalImageFromUri = false,
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