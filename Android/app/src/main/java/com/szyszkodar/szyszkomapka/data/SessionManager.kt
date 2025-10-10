package com.szyszkodar.szyszkomapka.data

import kotlinx.coroutines.flow.MutableStateFlow

object SessionManager {
    private val token = MutableStateFlow<String?>(null)

    fun setToken(value: String) {
        token.value = value
    }

    fun getToken(): String? =
        token.value
}