package core.input_validators

import javax.inject.Inject

class DecimalNumberValidator @Inject constructor() : InputValidator {
    override fun isValid(input: String): ValidationState {
        val decimalRegex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return when {
            input.isEmpty() -> ValidationState.Empty
            input.matches(decimalRegex) -> ValidationState.Valid
            else -> ValidationState.Invalid
        }
    }
}