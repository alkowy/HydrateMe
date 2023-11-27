package com.azmarzly.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.DispatcherIO
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.model.FirestoreUserDataModel
import core.model.HydrationData
import core.model.HydrationData.HydrationChunk
import core.model.Resource
import core.model.UserDataModel
import core.util.doNothing
import core.util.isSameDayAs
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class HomeViewModel @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
    @Named("WholeNumberValidator") private val numberValidator: InputValidator,
) : ViewModel() {

    companion object {
        const val DEFAULT_HYDRATION_GOAL = 2000
    }

    private val _userDataState: MutableStateFlow<Resource<UserDataModel>> = MutableStateFlow(Resource.EmptyState)
    val userDataState: StateFlow<Resource<UserDataModel>> = _userDataState.asStateFlow()

    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    fun fetchCurrentUser() {
        viewModelScope.launch(dispatcherIO) {
            fetchCurrentUserUseCase().collect { fetchResult ->
                _userDataState.update { fetchResult }
                when (fetchResult) {
                    is Resource.Error -> _homeState.update { _homeState.value.copy(error = (fetchResult.errorMessage ?: "Unexpected error")) }
                    Resource.Loading -> _homeState.update { _homeState.value.copy(isLoading = true) }
                    is Resource.Success -> _homeState.update {
                        val hydrationData = fetchResult.data?.hydrationData?.find { it.date.isSameDayAs(LocalDate.now()) }
                        _homeState.value.copy(
                            userData = fetchResult.data,
                            isLoading = false,
                            remainingHydrationMillis = hydrationData?.calculateRemaining() ?: fetchResult.data?.hydrationGoalMillis ?: DEFAULT_HYDRATION_GOAL,
                            hydrationProgressPercentage = hydrationData?.calculateProgress() ?: 0,
                            hydrationGoal = fetchResult.data?.hydrationGoalMillis ?: DEFAULT_HYDRATION_GOAL//hydrationData?.goalMillis ?: 0
                        )
                    }

                    Resource.EmptyState -> doNothing()
                }

            }
        }
    }

    fun validateNumber(amount: String): ValidationState {
        return numberValidator.isValid(amount)
    }

    fun addHydration(
        date: LocalDate = LocalDate.now(),
        amountOfWaterAdded: Int,
    ) {
        _homeState.update { currentState ->
            val userData = currentState.userData ?: return

            val updatedHydrationData = updateOrAddHydrationData(userData, date, amountOfWaterAdded)

            val updatedUserData = userData.copy(hydrationData = updatedHydrationData)

            val updatedHomeState = updateHomeStateWithNewUserDataModel(currentState, updatedUserData, date)

            updatedHomeState
        }

        _homeState.value.userData?.toFirestoreUserDataModel()?.let { updateFirestoreUser(it) }
    }

    private fun updateOrAddHydrationData(
        userData: UserDataModel,
        date: LocalDate,
        amountOfWaterAdded: Int,
    ): List<HydrationData> {
        val updatedHydrationData = userData.hydrationData.toMutableList()
        val existingData = updatedHydrationData.find { it.date.isSameDayAs(date) }

        Log.d("ANANAS", "updateOrAddHydrationData: 111 $updatedHydrationData")
        Log.d("ANANAS", "updateOrAddHydrationData: existingdata $existingData")
        if (existingData != null) {
            val hydrationChunksList = existingData.hydrationChunksList.toMutableList()
            hydrationChunksList.add(
                HydrationChunk(
                    dateTime = LocalDateTime.now(),
                    amount = amountOfWaterAdded
                )
            )
            existingData.progress += amountOfWaterAdded
            existingData.progressInPercentage = existingData.calculateProgress()
            existingData.hydrationChunksList = hydrationChunksList
            Log.d("ANANAS", "updateOrAddHydrationData: existingdata2222 $existingData")

        } else {
            val newEntry = HydrationData(
                date = date,
                goalMillis = userData.hydrationGoalMillis,
                progress = amountOfWaterAdded,
                progressInPercentage = (amountOfWaterAdded * 100) / userData.hydrationGoalMillis,
                hydrationChunksList = listOf(
                    HydrationChunk(
                        dateTime = LocalDateTime.now(),
                        amount = amountOfWaterAdded,
                    )
                )

            )
            updatedHydrationData.add(newEntry)
        }
        Log.d("ANANAS", "updateOrAddHydrationData: 222 $updatedHydrationData")

        return updatedHydrationData
    }

    private fun updateHomeStateWithNewUserDataModel(
        currentState: HomeState,
        updatedUserData: UserDataModel,
        date: LocalDate,
    ): HomeState {
        val hydrationData = updatedUserData.hydrationData.find { it.date.isSameDayAs(date) }
        val hydrationProgressPercentage = hydrationData?.calculateProgress() ?: 0
        val remainingHydrationMillis = hydrationData?.calculateRemaining() ?: 0

        return currentState.copy(
            userData = updatedUserData,
            remainingHydrationMillis = remainingHydrationMillis,
            hydrationProgressPercentage = hydrationProgressPercentage
        )
    }

    private fun updateFirestoreUser(updatedUserData: FirestoreUserDataModel) {
        Log.d("ANANAS", "updateFirestoreUser: $updatedUserData")
        viewModelScope.launch(dispatcherIO) {
            updateFirestoreUserUseCase(updatedUserData)
                .collect { updateResult ->
                    when (updateResult) {
                        is Resource.Error -> _userDataState.update { updateResult }
                        is Resource.Success -> _userDataState.update { updateResult }
                        Resource.Loading, Resource.EmptyState -> doNothing()
                    }
                }
        }
    }
}

data class HomeState(
    val userData: UserDataModel? = null,
    val hydrationProgressPercentage: Int = 0,
    var remainingHydrationMillis: Int = userData?.hydrationGoalMillis ?: 0,
    var hydrationGoal: Int = userData?.hydrationGoalMillis ?: 2000, //userData?.hydrationData?.find { it.date.isSameDayAs(LocalDate.now()) }?.goalMillis ?: 0,
    val isLoading: Boolean = false,
    val error: String = "",
) {

    // verify
    fun updateRemainingHydration() {
        if (userData != null) {
            val goal = userData.hydrationGoalMillis
            remainingHydrationMillis = goal.minus(remainingHydrationMillis)
        }
    }
}