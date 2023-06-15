package core.input_validators

import android.util.Patterns
import javax.inject.Inject

class EmailInputValidator @Inject constructor() : InputValidator {
    override fun isValid(input: String): ValidationState {
        return when {
            input.isEmpty() -> ValidationState.Empty
            Patterns.EMAIL_ADDRESS.matcher(input).matches() -> ValidationState.Valid
            else -> ValidationState.Invalid
        }
    }
}