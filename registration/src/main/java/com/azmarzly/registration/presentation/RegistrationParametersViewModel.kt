package com.azmarzly.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.input_validators.ValidationState.Valid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegistrationParametersViewModel @Inject constructor(
    @Named("WeightValidator") private val weightValidator: InputValidator,
    @Named("HeightValidator") private val heightValidator: InputValidator,
    @Named("DateValidator") private val dateValidator: InputValidator,
) : ViewModel() {

    private val _parametersState: MutableStateFlow<RegistrationParametersState> = MutableStateFlow(RegistrationParametersState())
    val parametersState = _parametersState.asStateFlow()

    private var validationJob: Job? = null

    fun validateWeight(weight: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            _parametersState.update {
                it.copy(
                    weightValidationState = weightValidator.isValid(weight),
                    weight = weight.toDoubleOrNull()
                )
            }
            validateAndMaybeEnableNextButton()
        }
    }

    fun validateHeight(height: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            _parametersState.update {
                it.copy(
                    heightValidationState = heightValidator.isValid(height),
                    height = height.toDoubleOrNull()
                )
            }
            validateAndMaybeEnableNextButton()
        }
    }

    fun handleDatePickerStateChange(selectedDate: Long?) {
        selectedDate?.let { date ->
            _parametersState.update {
                it.copy(
                    selectedDate = LocalDateTime.ofEpochSecond(date.div(1000), 0, ZoneOffset.UTC)
                )
            }
            validateAndMaybeEnableNextButton()
        }
    }

    fun validateDate(input: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            _parametersState.update {
                it.copy(
                    dateValidationState = dateValidator.isValid(input),
                )
            }
            validateAndMaybeEnableNextButton()
        }
    }

    private fun validateAndMaybeEnableNextButton() {
        _parametersState.update {
            it.copy(
                isNextButtonEnabled = it.weightValidationState == Valid
                        && it.heightValidationState == Valid
                        && it.dateValidationState == Valid
            )
        }
    }
}

data class RegistrationParametersState(
    val weightValidationState: ValidationState = ValidationState.Empty,
    val heightValidationState: ValidationState = ValidationState.Empty,
    val dateValidationState: ValidationState = ValidationState.Empty,
    val selectedDate: LocalDateTime? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val isNextButtonEnabled: Boolean = false,
)