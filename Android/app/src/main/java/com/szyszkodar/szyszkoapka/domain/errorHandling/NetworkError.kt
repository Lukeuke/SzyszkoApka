package com.szyszkodar.szyszkoapka.domain.errorHandling

// Possible network errors
enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_CONNECTION,
    SERVER_ERROR,
    UNKNOWN_HOST,
    UNKNOWN
}