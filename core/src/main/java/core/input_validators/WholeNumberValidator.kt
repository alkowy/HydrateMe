package core.input_validators

import javax.inject.Inject

class WholeNumberValidator @Inject constructor() : InputValidator {
    override fun isValid(input: String): ValidationState {
        val wholeNumberRegex = Regex("^[+-]?[1-9][0-9]*|0\$")
        return when {
            input.isEmpty() -> ValidationState.Empty
            input.matches(wholeNumberRegex) -> ValidationState.Valid
            else -> ValidationState.Invalid
        }
    }
}