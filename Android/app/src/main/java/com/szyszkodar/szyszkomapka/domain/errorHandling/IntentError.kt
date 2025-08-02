package com.szyszkodar.szyszkomapka.domain.errorHandling

enum class IntentError(override val message: String): Error {
    ACTIVITY_NOT_FOUND("Nie znaleziono aplikacji"),
    SECURITY_EXCEPTION("Odmowa dostępu"),
    ILLEGAL_ARGUMENT_EXCEPTION("Wystąpił niespodziewany błąd..."),
    NULL_POINTER_EXCEPTION("Wystąpił niespodziewany błąd..."),
    UNKNOWN("Wystąpił niespodziewany błąd...")
}