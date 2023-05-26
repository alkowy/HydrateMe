package com.azmarzly.home.presentation

import android.util.Log
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
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
) : ViewModel() {

    private val _userDataState: MutableStateFlow<Resource<UserDataModel>> = MutableStateFlow(Resource.EmptyState)
    val userDataState: StateFlow<Resource<UserDataModel>> = _userDataState.asStateFlow()

    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    fun fetchCurrentUser() {
        viewModelScope.launch(dispatcherIO) {
            fetchCurrentUserUseCase().collect { fetchResult ->
                _userDataState.update { fetchResult }
                when (fetchResult) {
                    is Resource.Error -> _homeState.update { _homeState.value.copy(error = (fetchResult.errorMessage ?: "Unexpected error")) }
                    Resource.Loading -> _homeState.update { _homeState.value.copy(isLoading = true) }
                    is Resource.Success -> _homeState.update {
                        _homeState.value.copy(
                            userData = fetchResult.data,
                            isLoading = false,
                            hydrationProgress = calculateHydrationProgress()
                        )
                    }

                    Resource.EmptyState -> doNothing()
                }

            }
        }
    }

    private fun calculateHydrationProgress(): Int {
        val today = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val goal =
            _homeState.value.userData?.hydrationData?.find { it.date.atZone(ZoneId.systemDefault()).toLocalDateTime() == today }?.goal ?: 2000.0
        val progress =
            _homeState.value.userData?.hydrationData?.find { it.date.atZone(ZoneId.systemDefault()).toLocalDateTime() == today }?.progress?.toInt()
                ?: 0

        Log.d("ANANAS", "calculateHydrationProgress: ${progress.div(goal).times(10).toInt()}")
        return progress.div(goal).times(10).toInt()
    }

    //find today's hydration data and add amount to progress. Update progress in % too. If there is no hydration data for today, create new object with user's goal, this amount
    fun addWater(amount: Double) {
        val today = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val oldProgress =
            homeState.value.userData?.hydrationData?.find { it.date.atZone(ZoneId.systemDefault()).toLocalDateTime() == today }?.progress ?: 0.0
        val newProgress = oldProgress + amount

        val modifiedUserData = homeState.value.userData?.copy()

        val modifiedHydrationData = modifiedUserData?.hydrationData?.toMutableList()
        val hydrationDataToUpdate =
            modifiedHydrationData?.find { it.date.atZone(ZoneId.systemDefault()).toLocalDateTime() == today } ?: HydrationData(
                date = LocalDateTime.now(),
                progress = 0.0,
                progressInPercentage = 0,
                goal = 2000.0
            )

        hydrationDataToUpdate?.progress = hydrationDataToUpdate?.progress?.plus(amount) ?: amount
        val modifiedUserDataWithUpdatedHydration = modifiedUserData?.copy(hydrationData = modifiedHydrationData?.toList() ?: emptyList())
        Log.d("ANANAS", "addWater: $amount  ${modifiedUserDataWithUpdatedHydration}")


        val modifiedUserData111111 = homeState.value.userData?.copy(
            hydrationData = homeState.value.userData!!.hydrationData.map { hydrationData ->
                if (hydrationData.date.atZone(ZoneId.systemDefault()).toLocalDateTime() == today) {
                    hydrationData.copy(progress = hydrationData.progress + amount)
                } else {
                    hydrationData
                }
            }.let { modifiedHydrationData ->
                if (modifiedHydrationData.none { it.date.atZone(ZoneId.systemDefault()).toLocalDateTime() == today }) {
                    modifiedHydrationData + HydrationData(
                        date = LocalDateTime.now(),
                        progress = 0.0,
                        progressInPercentage = 0,
                        goal = 2000.0
                    )
                } else {
                    modifiedHydrationData
                }
            }
        )




        viewModelScope.launch(dispatcherIO) {
            updateFirestoreUserUseCase.invoke(modifiedUserDataWithUpdatedHydration!!.toFirestoreUserDataModel()).collect()
            _homeState.update { _homeState.value.copy(userData = modifiedUserData111111) }
        }
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
    val hydrationProgress: Int = 0,
    val isLoading: Boolean = false,
    val error: String = "",
)