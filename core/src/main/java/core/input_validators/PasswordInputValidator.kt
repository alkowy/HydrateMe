package core.input_validators

import javax.inject.Inject

class PasswordInputValidator @Inject constructor() : InputValidator {

    companion object {
        const val PASSWORD_PATTERN = "(?=.*[0-9a-zA-Z]).{6,}"
    }

    override fun isValid(input: String): ValidationState {
        return when {
            input.isEmpty() -> ValidationState.Empty
            input.matches(Regex(PASSWORD_PATTERN)) -> ValidationState.Valid
            else -> ValidationState.Invalid
        }
    }
}