package com.azmarzly.settings.presentation.personal_data_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.settings.presentation.personal_data_settings.PersonalDataSettingsViewModel.Companion.EMPTY_VALUE
import core.DispatcherIO
import core.domain.ResourceProvider
import core.domain.use_case.FetchCurrentUserUseCase
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.model.Gender
import core.model.GenderState
import core.model.Resource
import core.model.toDrawableIconRes
import core.model.toNameResourceStringId
import core.util.millisToLocalDate
import core.util.toDoubleWithoutUnit
import core.util.toFirestoreUserDataModel
import core.util.toLocalDate
import core.util.toStringFormatted
import core.util.toStringWithUnit
import core.util.toTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PersonalDataSettingsViewModel @Inject constructor(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val updateFirestoreUserUseCase: UpdateFirestoreUserUseCase,
    @DispatcherIO private val ioDispatcherIO: CoroutineDispatcher,
    @Named("DecimalValidator") private val weightValidator: InputValidator,
    @Named("HeightValidator") private val heightValidator: InputValidator,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    companion object {
        const val EMPTY_VALUE = "-"
    }

    private val _state = MutableStateFlow(PersonalDataModel())
    val state = _state.asStateFlow()

    init {
        initialiseState()
    }

    fun updateGender(gender: Gender?) {
        gender?.let { gender ->
            _state.update {
                it.copy(
                    gender = GenderDisplayState(
                        gender = gender,
                        genderText = resourceProvider.getString(gender.toNameResourceStringId())
                    )
                )
            }
        }
    }

    fun handleDatePicked(datePickedMillis: Long?) {
        datePickedMillis?.let { date ->
            _state.update {
                it.copy(
                    birthDateMillis = datePickedMillis,
                    birthDate = date.millisToLocalDate()?.toStringFormatted() ?: EMPTY_VALUE
                )
            }
        }
    }

    fun onWeightChanged(input: String) {
        _state.update {
            it.copy(
                isWeightInputValid = weightValidator.isValid(input) == ValidationState.Valid
            )
        }
    }

    fun onHeightChanged(input: String) {
        _state.update {
            it.copy(
                isHeightInputValid = heightValidator.isValid(input) == ValidationState.Valid
            )
        }
    }

    fun updateWeight(weight: String) {
        _state.update {
            it.copy(
                weight = weight
            )
        }
    }

    fun updateHeight(height: String) {
        _state.update {
            it.copy(
                height = height
            )
        }
    }

    fun saveChanges() {
        viewModelScope.launch(ioDispatcherIO) {
            fetchCurrentUserUseCase.invoke().collectLatest { result ->
                Log.d("ANANAS", "saveChanges: personaldatasettingsviewmodel reuslt $result")
                (result as? Resource.Success)?.data?.let { userData ->
                    val updatedUserData = userData.copy(
                        gender = _state.value.gender.gender,
                        birthDate = _state.value.birthDate.toLocalDate(),
                        weight = _state.value.weight.toDoubleWithoutUnit(),
                        height = _state.value.height.toDoubleWithoutUnit()
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
                            Log.d("ANANAS", " personaldatasettingsivewmodel initialiseState: data ${data.toString()}")
                            _state.update {
                                val genderName = data.gender?.toNameResourceStringId()?.let { gender -> resourceProvider.getString(gender) } ?: EMPTY_VALUE
                                PersonalDataModel(
                                    gender = GenderDisplayState(
                                        gender = data.gender,
                                        genderText = genderName
                                    ),
                                    birthDate = data.birthDate?.toStringFormatted() ?: EMPTY_VALUE,
                                    birthDateMillis = data.birthDate?.toTimestamp() ?: 0,
                                    weight = data.weight?.toStringWithUnit(unit = null) ?: EMPTY_VALUE,
                                    height = data.height?.toStringWithUnit(unit = null) ?: EMPTY_VALUE,
                                    genders = Gender.entries.map {
                                        GenderState(
                                            name = resourceProvider.getString(it.toNameResourceStringId()),
                                            gender = it,
                                            genderIcon = it.toDrawableIconRes()
                                        )
                                    })
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

data class PersonalDataModel(
    val gender: GenderDisplayState = GenderDisplayState(),
    val birthDate: String = EMPTY_VALUE,
    val birthDateMillis: Long = 0,
    val weight: String = EMPTY_VALUE,
    val isWeightInputValid: Boolean = false,
    val height: String = EMPTY_VALUE,
    val isHeightInputValid: Boolean = false,
    val genders: List<GenderState> = emptyList(),
)

data class GenderDisplayState(
    val gender: Gender? = null,
    val genderText: String = EMPTY_VALUE,
)