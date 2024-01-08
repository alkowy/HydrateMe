package com.azmarzly.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.core.R
import com.azmarzly.settings.presentation.SettingsViewModel.Companion.EMPTY_VALUE
import core.DispatcherIO
import core.domain.ResourceProvider
import core.domain.use_case.FetchCurrentUserUseCase
import core.model.Resource
import core.model.toUserActivity
import core.util.roundAndAddUnit
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
    @DispatcherIO private val ioDispatcherIO: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        const val EMPTY_VALUE = "-"
    }

    private val _settingsState = MutableStateFlow(SettingsUiState())
    val settingsState = _settingsState.asStateFlow()

    init {
        initialiseSettingsState()
    }

    private fun initialiseSettingsState() {
        viewModelScope.launch(ioDispatcherIO) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { data ->
                            _settingsState.update {
                                SettingsUiState(
                                    gender = data.gender?.name ?: EMPTY_VALUE,
                                    birthDate = data.birthDate?.toStringFormatted() ?: EMPTY_VALUE,
                                    weight = data.weight?.toStringWithUnit(unit = resourceProvider.getString(R.string.unit_kg)) ?: EMPTY_VALUE,
                                    height = data.height?.toStringWithUnit(unit = resourceProvider.getString(R.string.unit_cm)) ?: EMPTY_VALUE,
                                    activity = data.userActivity.toUserActivity().name,
                                    hydrationGoal = data.hydrationGoalMillis.div(1000).toDouble()
                                        .roundAndAddUnit(decimals = 2, unit = resourceProvider.getString(R.string.unit_liter)),
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

data class SettingsUiState(
    val gender: String = EMPTY_VALUE,
    val birthDate: String = EMPTY_VALUE,
    val weight: String = EMPTY_VALUE,
    val height: String = EMPTY_VALUE,
    val activity: String = EMPTY_VALUE,
    val hydrationGoal: String = EMPTY_VALUE,
)