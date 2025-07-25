package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkomapka.data.remote.response.EmptyResponse
import com.szyszkodar.szyszkomapka.domain.remote.response.Response

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