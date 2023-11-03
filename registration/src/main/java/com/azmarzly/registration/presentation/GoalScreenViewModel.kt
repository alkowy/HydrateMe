package com.azmarzly.registration.presentation

import androidx.lifecycle.ViewModel
import core.input_validators.InputValidator
import core.input_validators.ValidationState.Valid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class GoalScreenViewModel @Inject constructor(
    @Named("DecimalValidator") private val weightValidator: InputValidator,
) : ViewModel() {

    private val _isInputValid = MutableStateFlow<Boolean>(false)
    val isInputValid = _isInputValid.asStateFlow()

    fun validateGoal(input: String) {
        _isInputValid.update {
            weightValidator.isValid(input) == Valid
        }
    }
}