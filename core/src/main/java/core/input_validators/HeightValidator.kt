package core.input_validators

import javax.inject.Inject

class HeightValidator @Inject constructor() : InputValidator {
    override fun isValid(input: String): ValidationState {
        return when {
            input.isEmpty() -> ValidationState.Empty
            input.all { it.isDigit() } -> ValidationState.Valid
            else -> ValidationState.Invalid
        }
    }
}