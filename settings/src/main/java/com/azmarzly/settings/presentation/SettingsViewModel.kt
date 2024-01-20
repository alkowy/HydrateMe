package com.azmarzly.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.core.R
import com.azmarzly.settings.presentation.SettingsViewModel.Companion.EMPTY_VALUE
import com.azmarzly.settings.presentation.personal_data_settings.PersonalDataModel
import com.azmarzly.settings.presentation.personal_data_settings.PersonalDataSettingsViewModel
import core.DispatcherIO
import core.domain.ResourceProvider
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.PeriodicallyFetchUserDataModelUseCase
import core.model.Resource
import core.model.UserActivity
import core.model.UserDataModel
import core.model.toNameResourceStringId
import core.model.toUserActivity
import core.util.doNothing
import core.util.toStringFormatted
import core.util.toStringWithUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val resourceProvider: ResourceProvider,
    @DispatcherIO private val dispatcherIo: CoroutineDispatcher,
    private val periodicallyFetchUserDataModelUseCase: PeriodicallyFetchUserDataModelUseCase,
) : ViewModel() {

    companion object {
        const val EMPTY_VALUE = "-"
        const val DEFAULT_HYDRATION_GOAL = 2000.0
    }

    private val _settingsState = MutableStateFlow(SettingsUiState())
    val settingsState = _settingsState.asStateFlow()

    init {
        initialiseSettingsState()
        periodicallyFetchUserDataAndMapSettingsState()
    }

    private fun initialiseSettingsState() {
        viewModelScope.launch(dispatcherIo) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        updateUiState(userDataModel = result.data)
                    }

                    else -> {
                        doNothing()
                    }
                }
            }
        }
    }

    private fun periodicallyFetchUserDataAndMapSettingsState() {
        viewModelScope.launch(dispatcherIo) {
            periodicallyFetchUserDataModelUseCase.invoke()
                .collectLatest { fetchResult ->
                    Log.d("ANANAS", "periodicallyFetchUserDataAndMapSettingsState IN SETTINGS: $fetchResult")
                    when (fetchResult) {
                        is Resource.Success -> {
                            updateUiState(userDataModel = fetchResult.data)
                        }

                        else -> {
                            doNothing()
                        }
                    }


                }
        }
    }

    private fun updateUiState(userDataModel: UserDataModel?) {
        val fetchedHydrationGoal = userDataModel?.hydrationGoalMillis?.toDouble()?.div(1000) ?: DEFAULT_HYDRATION_GOAL
        val genderName = userDataModel?.gender?.toNameResourceStringId()?.let { gender -> resourceProvider.getString(gender) } ?: PersonalDataSettingsViewModel.EMPTY_VALUE

        _settingsState.update {
            SettingsUiState(
                PersonalDataModel(
                    gender = genderName,
                    birthDate = userDataModel?.birthDate?.toStringFormatted() ?: EMPTY_VALUE,
                    weight = userDataModel?.weight?.toStringWithUnit(unit = resourceProvider.getString(R.string.unit_kg)) ?: EMPTY_VALUE,
                    height = userDataModel?.height?.toStringWithUnit(unit = resourceProvider.getString(R.string.unit_cm)) ?: EMPTY_VALUE,
                ),
                accountPersonalisation = AccountPersonalisationModel(
                    activity = resourceProvider.getString(userDataModel?.userActivity?.toUserActivity()?.toNameResourceStringId() ?: R.string.activity_empty),
                    hydrationGoal = fetchedHydrationGoal.toStringWithUnit(unit = resourceProvider.getString(R.string.unit_liter)),
                )
            )
        }
    }
}

data class SettingsUiState(
    val personalData: PersonalDataModel = PersonalDataModel(),
    val accountPersonalisation: AccountPersonalisationModel = AccountPersonalisationModel(),
)

data class AccountPersonalisationModel(
    val activity: String = EMPTY_VALUE,
    val hydrationGoal: String = EMPTY_VALUE,
)