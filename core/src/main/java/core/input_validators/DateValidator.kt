package core.input_validators

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class DateValidator @Inject constructor() : InputValidator {

    private var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    init {
        simpleDateFormat.isLenient = false
    }

    override fun isValid(input: String): ValidationState {
        if (input.split("-").isEmpty()) return ValidationState.Empty
        return try {
            simpleDateFormat.parse(input)
            ValidationState.Valid
        } catch (e: Exception) {
            ValidationState.Invalid
        }
    }
}