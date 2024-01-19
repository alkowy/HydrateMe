package com.azmarzly.settings.presentation.personalisation_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.settings.presentation.personalisation_settings.AccountPersonalisationSettingsViewModel.Companion.EMPTY_VALUE
import core.DispatcherIO
import core.domain.ResourceProvider
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.model.Resource
import core.model.UserActivity
import core.model.UserActivityEnum
import core.model.UserActivityState
import core.model.toDescriptionResourceStringId
import core.model.toNameResourceStringId
import core.model.toUserActivity
import core.model.toUserActivityEnum
import core.util.isSameDayAs
import core.util.toFirestoreUserDataModel
import core.util.toStringWithUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AccountPersonalisationSettingsViewModel @Inject constructor(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
    @DispatcherIO private val ioDispatcherIO: CoroutineDispatcher,
    @Named("DecimalValidator") private val decimalValidator: InputValidator,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    companion object {
        const val EMPTY_VALUE = "-"
    }

    private val _state = MutableStateFlow(AccountPersonalisationState())
    val state = _state.asStateFlow()

    init {
        initialiseState()
    }

    fun updateUserActivity(activity: UserActivity?) {
        activity?.let { activity ->
            _state.update {
                it.copy(
                    userActivity = activity
                )
            }
        }
    }

    fun onHydrationGoalChanged(input: String) {
        _state.update {
            it.copy(
                isHydrationGoalInputValid = decimalValidator.isValid(input) == ValidationState.Valid
            )
        }
    }

    fun updateHydrationGoal(goal: String) {
        _state.update {
            it.copy(
                hydrationGoalUi = goal,
                hydrationGoalInMillis = goal.toDouble().times(1000).toInt(),
            )
        }
    }

    fun saveChanges() {
        viewModelScope.launch(ioDispatcherIO) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                (result as? Resource.Success)?.data?.let { userData ->
                    val originalHydrationData = userData.hydrationData.toMutableList()
                    val hydrationDataToday = originalHydrationData.find { it.date.isSameDayAs(LocalDate.now()) }
                    hydrationDataToday?.goalMillis = _state.value.hydrationGoalInMillis
                    hydrationDataToday?.progressInPercentage = hydrationDataToday?.calculateProgress() ?: 0
                    
                    val updatedUserData = userData.copy(
                        userActivity = _state.value.userActivity.toUserActivityEnum(),
                        hydrationGoalMillis = _state.value.hydrationGoalInMillis,
                        hydrationData = originalHydrationData
                    )
                    updateFirestoreUserUseCase.invoke(user = updatedUserData.toFirestoreUserDataModel())
                }
            }
        }
    }

    private fun initialiseState() {
        viewModelScope.launch(ioDispatcherIO) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { data ->
                            val fetchedHydrationGoal = data.hydrationGoalMillis.toDouble().div(1000)

                            _state.update {
                                AccountPersonalisationState(
                                    userActivity = data.userActivity.toUserActivity(),
                                    hydrationGoalInMillis = data.hydrationGoalMillis,
                                    hydrationGoalUi = fetchedHydrationGoal.toStringWithUnit(
                                        unit = null,
                                    ),
                                    userActivitiesState = (UserActivityEnum.entries.map {
                                        UserActivityState(
                                            userActivity = it.toUserActivity(),
                                            name = resourceProvider.getString(it.toUserActivity().toNameResourceStringId()),
                                            description = resourceProvider.getString(it.toUserActivity().toDescriptionResourceStringId()),
                                        )
                                    })
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

data class AccountPersonalisationState(
    val userActivity: UserActivity = UserActivity.Empty(),
    val hydrationGoalUi: String = EMPTY_VALUE,
    val hydrationGoalInMillis: Int = 2000,
    val isHydrationGoalInputValid: Boolean = false,
    val userActivitiesState: List<UserActivityState> = emptyList(),
)