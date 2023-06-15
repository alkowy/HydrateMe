package core.input_validators

interface InputValidator {
    fun isValid(input: String): ValidationState
}