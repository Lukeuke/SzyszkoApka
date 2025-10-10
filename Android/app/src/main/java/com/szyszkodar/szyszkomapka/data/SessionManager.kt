package com.szyszkodar.szyszkomapka.data

import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.internal.userAgent

object SessionManager {
    private val token = MutableStateFlow<String?>(null)
    private var cachedUserId: String? = null

    fun init(userIdProvider: () -> String) {
        if (cachedUserId == null) {
            cachedUserId = userIdProvider()
        }
    }

    fun getUserId(userIdProvider: () -> String): String {
        return cachedUserId ?: synchronized(this) {
            cachedUserId ?: userIdProvider().also { cachedUserId = it }
        }
    }

    fun setToken(value: String) {
        token.value = value
    }

    fun getToken(): String? =
        token.value
}