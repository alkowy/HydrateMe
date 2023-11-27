package core.input_validators

sealed interface ValidationState {
    data object Empty : ValidationState
    data object Valid : ValidationState
    data object Invalid : ValidationState
}