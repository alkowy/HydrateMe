package com.azmarzly.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.input_validators.InputValidator
import core.input_validators.ValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class RegistrationParametersViewModel @Inject constructor(
    @Named("WeightValidator") private val weightValidator: InputValidator,
    @Named("HeightValidator") private val heightValidator: InputValidator,
) : ViewModel() {

    private val _parametersState: MutableStateFlow<RegistrationParametersState> = MutableStateFlow(RegistrationParametersState())
    val paramatersState = _parametersState.asStateFlow()

    private var validationJob: Job? = null

    fun validateWeight(weight: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(250.milliseconds)
            _parametersState.update {
                it.copy(
                    weightValidationState = weightValidator.isValid(weight)
                )
            }
        }
    }

    fun validateHeight(height: String) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(250.milliseconds)
            _parametersState.update {
                it.copy(
                    heightValidationState = heightValidator.isValid(height)
                )
            }
        }
    }
}

data class RegistrationParametersState(
    val weightValidationState: ValidationState = ValidationState.Empty,
    val heightValidationState: ValidationState = ValidationState.Empty,
    val dateValidationState: ValidationState = ValidationState.Empty,
    val isNextButtonEnabled: Boolean = false,
)