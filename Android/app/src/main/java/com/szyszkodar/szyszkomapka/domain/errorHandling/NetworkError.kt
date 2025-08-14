package com.szyszkodar.szyszkomapka.domain.errorHandling

// Possible network errors
enum class NetworkError(override val message: String): Error {
    REQUEST_TIMEOUT("Przekroczono limit czasu połączenia!"),
    TOO_MANY_REQUESTS("Zbyt dużo zapytań!"),
    NO_CONNECTION("Brak połączenia internetowego!"),
    SERVER_ERROR("Wystąpił błąd po stronie serwera. Właśnie trwają prace nad jego rozwiązaniem."),
    IDENTITY_ERROR("Nieprawidłowe dane logowania"),
    UNKNOWN_HOST("Wystąpił błąd hosta. Spróbuj ponownie później."),
    UNKNOWN("Wystąpił niespodziewany błąd... Spróbuj ponownie później")
}