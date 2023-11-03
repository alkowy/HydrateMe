package com.azmarzly.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import core.input_validators.ValidationState.Valid
import core.util.millisToLocalDate
import core.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegistrationParametersViewModel @Inject constructor(
    @Named("DecimalValidator") private val decimalValidator: InputValidator,
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
                    weightValidationState = decimalValidator.isValid(weight),
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

    fun handleDatePickedStateChanged(selectedDate: Long?) {
        selectedDate?.let { date ->
            _parametersState.update {
                it.copy(
                    selectedDate = date.millisToLocalDate()
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
                    selectedDate = parseDateFromString(input)
                )
            }
            validateAndMaybeEnableNextButton()
        }
    }

    private fun parseDateFromString(input: String): LocalDate? {
        if (input.split("-").any { it.isEmpty() }) return null
        return try {
            input.toLocalDate()
        } catch (_: Exception) {
            null
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
    val selectedDate: LocalDate? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val isNextButtonEnabled: Boolean = false,
)