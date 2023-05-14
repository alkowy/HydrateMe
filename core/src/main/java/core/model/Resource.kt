package core.model

sealed class Resource<out T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error<T>(val errorMessage: String? = null) : Resource<T>()
    object Loading : Resource<Nothing>()
    object EmptyState: Resource<Nothing>()
}