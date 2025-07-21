package com.szyszkodar.szyszkomapka.domain.errorHandling

typealias RootError = Error

// Interface for a result error handling approach
sealed interface Result<out T, out E: RootError> {
    data class Success<out T, out E: RootError>(val data: T): Result<T, E>
    data class Error<out T, out E: RootError>(val error: E): Result<T, E>
}