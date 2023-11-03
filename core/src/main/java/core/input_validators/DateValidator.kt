package core.input_validators

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

open class DateValidator @Inject constructor() : InputValidator {

    private var simpleDateFormat = SimpleDateFormat("yyyy-M-dd", Locale.getDefault())

    init {
        simpleDateFormat.isLenient = false
    }

    override fun isValid(input: String): ValidationState {
        if (input.split("-").any { it.isEmpty() }) return ValidationState.Empty
        return try {
            simpleDateFormat.parse(input)
            if (input.split("-").first().toInt() < 1900) return ValidationState.Invalid
            ValidationState.Valid
        } catch (e: Exception) {
            ValidationState.Invalid
        }
    }
}