package com.szyszkodar.szyszkomapka.domain.errorHandling

// Base interface for any Errors handled by this app
sealed interface Error {
    val message: String
}