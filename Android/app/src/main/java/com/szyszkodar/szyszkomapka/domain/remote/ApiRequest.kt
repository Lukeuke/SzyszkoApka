package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody

// Request abstraction
sealed interface ApiRequest {
    data class CheckIdentity(
        val body: IdentityBody
    ): ApiRequest
    data class GetBookpoints(
        val queryMap: Map<String, String>
    ): ApiRequest
    data class DeleteBookpoints(
        val id: String,
        val bearerToken: String
    ): ApiRequest

}