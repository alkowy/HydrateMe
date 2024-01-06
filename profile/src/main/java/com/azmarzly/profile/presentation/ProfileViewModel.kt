package com.azmarzly.profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.authentication.domain.AuthenticationRepository
import core.DispatcherIO
import core.domain.use_case.FetchCurrentUserUseCase
import core.model.Resource
import core.model.UserActivityEnum
import core.util.round
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val authenticationRepository: AuthenticationRepository, // remove + remove the dependency from gradle or not
) : ViewModel() {

    companion object {
        private const val EMPTY_VALUE = "-"
        private val emptyProfileState = ProfileUiState(
            profileImageUrl = "",
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

    init {
        Log.d("ANANAS", "init profileviewmodel: ")
        initialiseProfileState()
    }

    // remove
    fun signOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
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
                                        dailyGoal = data.hydrationGoalMillis.div(1000).toDouble().round(decimals = 2).toString() + " L",
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