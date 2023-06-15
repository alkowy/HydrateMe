package core.input_validators

import javax.inject.Inject

class PasswordInputValidator @Inject constructor() : InputValidator {
    override fun isValid(input: String): ValidationState {
        return ValidationState.Empty
    }
}