package core.input_validators

sealed interface ValidationState {
    object Empty : ValidationState
    object Valid : ValidationState
    object Invalid : ValidationState
}