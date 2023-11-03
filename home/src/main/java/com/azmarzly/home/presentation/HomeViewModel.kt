package com.azmarzly.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.DispatcherIO
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.model.FirestoreUserDataModel
import core.model.HydrationData
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


@HiltViewModel
class HomeViewModel @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
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
                        )
                    }

                    Resource.EmptyState -> doNothing()
                }

            }
        }
    }

    private fun calculateHydrationProgress(): Int {
        val today = LocalDateTime.now()
        return _homeState.value.userData?.hydrationData?.find { it.date.isSameDayAs(today) }?.calculateProgress() ?: 0
    }

    fun updateHydrationData(date: LocalDateTime = LocalDateTime.now(), amountOfWaterAdded: Int) {
        _homeState.update { currentState ->
            val userData = currentState.userData ?: return
            val hydrationDataList = userData.hydrationData.toMutableList()

            val existingData = hydrationDataList.find { it.date.isSameDayAs(date) }

            if (existingData != null) {
                // Update an existing entry
                existingData.progress += amountOfWaterAdded
                existingData.progressInPercentage = existingData.calculateProgress()
            } else {
                // Create a new entry
                val newEntry = HydrationData(
                    date = date,
                    goalMillis = userData.hydrationGoalMillis,
                    progress = amountOfWaterAdded,
                    progressInPercentage = (amountOfWaterAdded * 100) / userData.hydrationGoalMillis
                )
                hydrationDataList.add(newEntry)
            }

            val updatedUserData = userData.copy(hydrationData = hydrationDataList)


            var updatedHomeState = currentState.copy(
                userData = updatedUserData,
//                remainingHydrationMillis = updatedUserData.hydrationGoalMillis.minus(
//                    updatedUserData.hydrationData.find { it.date.isSameDayAs(date) }?.progress ?: amountOfWaterAdded
//                ),
//                hydrationProgressPercentage = calculateHydrationProgress()
            )
            val hydrationData = updatedUserData.hydrationData.find { it.date.isSameDayAs(date) }
            hydrationData?.let { data ->
                updatedHomeState = updatedHomeState.copy(
                    remainingHydrationMillis = data.calculateRemaining(),
                    hydrationProgressPercentage = data.calculateProgress()
                )
            }

            updatedHomeState
        }
        _homeState.value.userData?.toFirestoreUserDataModel()?.let { updateFirestoreUser(it) }
    }


//    private fun calculateHydrationProgress(): Int {
//        val today = LocalDateTime.now()
//        val goal =
//            _homeState.value.userData?.hydrationData?.find { it.date.isSameDayAs(today) }?.goal ?: 2000.0
//        val progress =
//            _homeState.value.userData?.hydrationData?.find { it.date.isSameDayAs(today) }?.progress?.toInt()
//                ?: 0
//
//        return progress.div(goal).times(10).toInt()
//    }

    fun addWater2(amount: Double) {
        val today = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime()
//        val oldProgress =
//            homeState.value.userData?.hydrationData?.find { it.date.atZone(ZoneId.systemDefault()).toLocalDateTime() == today }?.progress ?: 0.0
        val newProgressPercentage =
            _homeState.update {
                _homeState.value.copy(
//                    hydrationProgressPercentage = it.hydrationProgressPercentage.plus(amount)
                )
            }


    }

//    private fun findTodaysGoal(): Double {
//        val goal = _homeState.value.userData?.hydrationData?.find { it.date.isSameDayAs(LocalDateTime.now()) } ?: HydrationData(
//            date = LocalDateTime.now(),
//            progress = 0.0,
//            progressInPercentage = 0,
//            goal = 2000.0
//        )
//    }

    //find today's hydration data and add amount to progress. Update progress in % too. If there is no hydration data for today, create new object with user's goal, this amount
    fun addWater(amount: Double) {
        val today = LocalDateTime.now()
//        val oldProgress =
//            homeState.value.userData?.hydrationData?.find { it.date.isSameDayAs(today) }?.progress ?: 0.0
//        val newProgress = oldProgress + amount
//
//
//        Log.d("ANANAS", "HomeViewModel, addWater: oldProgress $oldProgress ")
//
//        val modifiedUserData = homeState.value.userData?.copy()

//        val modifiedHydrationData = modifiedUserData?.hydrationData?.toMutableList()
//        val hydrationDataToUpdate =
//            modifiedHydrationData?.find { it.date.isSameDayAs(today) } ?: HydrationData(
//                date = LocalDateTime.now(),
//                progress = 0.0,
//                progressInPercentage = 0,
//                goal = 2000.0
//            )
//        Log.d("ANANAS", "HomeViewModel, addWater: hydrationDataToUpdate $hydrationDataToUpdate ")

//        hydrationDataToUpdate.progress = hydrationDataToUpdate.progress.plus(amount)
//        val modifiedUserDataWithUpdatedHydration = modifiedUserData?.copy(hydrationData = modifiedHydrationData?.toList() ?: emptyList())
//
//        Log.d("ANANAS", "HomeViewModel, addWater: modifiedUserDataWithUpdatedHydration $modifiedUserDataWithUpdatedHydration ")
//
//        val modifiedUserData111111 = homeState.value.userData?.copy(
//            hydrationData = homeState.value.userData!!.hydrationData.map { hydrationData ->
//                if (hydrationData.date.isSameDayAs(today)) {
//                    hydrationData.copy(
//                        progress = hydrationData.progress + amount,
//                        progressInPercentage = calculateHydrationProgress()
//                    )
//                } else {
//                    hydrationData
//                }
//            }.let { m33odifiedHydrationData ->
//                if (m33odifiedHydrationData.none { it.date.isSameDayAs(today) }) {
//                    m33odifiedHydrationData + HydrationData(
//                        date = LocalDateTime.now(),
//                        progress = 0.0,
//                        progressInPercentage = 0,
//                        goal = 2000.0
//                    )
//                } else {
//                    m33odifiedHydrationData
//                }
//            }
//        )
//        Log.d("ANANAS", "HomeViewModel, addWater: modifiedUserData111111 $modifiedUserData111111 ")

//
//        viewModelScope.launch(dispatcherIO) {
//            updateFirestoreUserUseCase.invoke(modifiedUserDataWithUpdatedHydration!!.toFirestoreUserDataModel()).collect()
//            _homeState.update {
//                _homeState.value.copy(
//                    userData = modifiedUserData111111,
//                    hydrationProgressPercentage = calculateHydrationProgress()
//                )
//            }
//        }

//        Log.d("ANANAS", "HomeViewModel, addWater: _homeState ${_homeState.value} ")

    }

    fun updateFirestoreUser(updatedUserData: FirestoreUserDataModel) {
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