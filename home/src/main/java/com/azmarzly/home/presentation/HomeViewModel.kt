package com.azmarzly.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.core.R
import com.azmarzly.home.components.HomeScreenBottomBarItem
import core.DispatcherIO
import core.LocalPreferencesApi
import core.domain.use_case.PeriodicallyFetchUserDataModelUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.model.FirestoreUserDataModel
import core.model.HydrationData
import core.model.HydrationData.HydrationChunk
import core.model.Resource
import core.model.UserDataModel
import core.util.HomeRoute
import core.util.doNothing
import core.util.isSameDayAs
import core.util.toFirestoreUserDataModel
import core.util.toTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
    private val periodicallyFetchUserDataModelUseCase: PeriodicallyFetchUserDataModelUseCase,
    @Named("WholeNumberValidator") private val numberValidator: InputValidator,
    private val localPreferencesApi: LocalPreferencesApi,
) : ViewModel() {

    companion object {
        const val DEFAULT_HYDRATION_GOAL = 2000
        val homeScreens = listOf(
            HomeScreenBottomBarItem(
                description = "Home",
                iconSelected = R.drawable.ic_home_selected,
                iconUnselected = R.drawable.ic_home_unselected,
                route = HomeRoute.HOME,
            ),
            HomeScreenBottomBarItem(
                description = "Calendar",
                iconSelected = R.drawable.ic_calendar_selected,
                iconUnselected = R.drawable.ic_calendar_unselected,
                route = HomeRoute.CALENDAR,
            ),
            HomeScreenBottomBarItem(description = "Empty", iconSelected = -1, iconUnselected = -1, route = HomeRoute.HOME_ROOT),
            HomeScreenBottomBarItem(
                description = "Profile",
                iconSelected = R.drawable.ic_profile_selected,
                iconUnselected = R.drawable.ic_profile_unselected,
                route = HomeRoute.PROFILE,
            ),
            HomeScreenBottomBarItem(
                description = "News",
                iconSelected = R.drawable.ic_news_selected,
                iconUnselected = R.drawable.ic_news_unselected,
                route = HomeRoute.NEWS,
            ),
        )
    }

    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _bottomBarVisibilityState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val bottomBarVisibilityState = _bottomBarVisibilityState.asStateFlow()

    init {
        periodicallyFetchUserDataAndMapToHomeState()
    }

    fun setBottomBarVisibilityState(shouldBeVisible: Boolean) {
        _bottomBarVisibilityState.update { shouldBeVisible }
    }

    private fun periodicallyFetchUserDataAndMapToHomeState() {
        viewModelScope.launch(dispatcherIO) {
            periodicallyFetchUserDataModelUseCase.invoke()
                .collectLatest { fetchResult ->
                    when (fetchResult) {
                        is Resource.Error -> _homeState.update {
                            _homeState.value.copy(
                                error = (fetchResult.errorMessage ?: "Unexpected error")
                            )
                        }

                        Resource.Loading -> _homeState.update { _homeState.value.copy(isLoading = true) }
                        is Resource.Success -> _homeState.update {
                            val hydrationData = fetchResult.data?.hydrationData?.find { it.date.isSameDayAs(LocalDate.now()) }
                            _homeState.value.copy(
                                userData = fetchResult.data,
                                isLoading = false,
                                remainingHydrationMillis = hydrationData?.calculateRemaining()
                                    ?: fetchResult.data?.hydrationGoalMillis ?: DEFAULT_HYDRATION_GOAL,
                                hydrationProgressPercentage = hydrationData?.calculateProgress() ?: 0,
                                todayHydrationChunks = hydrationData?.hydrationChunksList ?: emptyList(),
                                hydrationGoal = fetchResult.data?.hydrationGoalMillis ?: DEFAULT_HYDRATION_GOAL
                            )
                        }

                        else -> doNothing()
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
        localPreferencesApi.setLastLogTimestamp(LocalDateTime.now().toTimestamp())
    }

    private fun updateOrAddHydrationData(
        userData: UserDataModel,
        date: LocalDate,
        amountOfWaterAdded: Int,
    ): List<HydrationData> {
        val updatedHydrationData = userData.hydrationData.toMutableList()
        val existingData = updatedHydrationData.find { it.date.isSameDayAs(date) }

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
            hydrationProgressPercentage = hydrationProgressPercentage,
            todayHydrationChunks = hydrationData?.hydrationChunksList ?: emptyList()
        )
    }

    private fun updateFirestoreUser(updatedUserData: FirestoreUserDataModel) {
        viewModelScope.launch(dispatcherIO) {
            updateFirestoreUserUseCase(updatedUserData)
        }
    }
}

data class HomeState(
    val userData: UserDataModel? = null,
    val hydrationProgressPercentage: Int = 0,
    var remainingHydrationMillis: Int = userData?.hydrationGoalMillis ?: 0,
    val todayHydrationChunks: List<HydrationChunk> = emptyList(),
    var hydrationGoal: Int = userData?.hydrationGoalMillis
        ?: 2000, //userData?.hydrationData?.find { it.date.isSameDayAs(LocalDate.now()) }?.goalMillis ?: 0,
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